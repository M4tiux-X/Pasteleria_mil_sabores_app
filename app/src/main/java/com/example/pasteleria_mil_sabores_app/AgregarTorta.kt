package com.example.pasteleria_mil_sabores_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.dao.ProductoDAO
import com.example.pasteleria_mil_sabores_app.model.Producto
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AgregarTorta : AppCompatActivity() {


    private lateinit var imgPreview: ImageView
    private lateinit var btnTomarFoto: Button
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var spCategoria: Spinner
    private lateinit var swPersonalizable: Switch
    private lateinit var btnGuardar: Button
    private lateinit var dao: ProductoDAO

    private var imagePath: String? = null

    private val REQUEST_CAMERA = 100
    private val PERMISSION_CAMERA_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_torta)

        dao= ProductoDAO(this)
        initViews()
        cargarCategorias()

        btnTomarFoto.setOnClickListener {
            abrirCamara()
        }

        btnGuardar.setOnClickListener {
            guardarTorta()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initViews() {
        imgPreview = findViewById(R.id.imgPreview)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        spCategoria = findViewById(R.id.spCategoria)
        swPersonalizable = findViewById(R.id.swPersonalizable)
        btnGuardar = findViewById(R.id.btnGuardar)
    }

    /** ============================
     *      Cargar Categorías
     *  ============================ */
    private fun cargarCategorias() {
        // Aquí puedes cargar desde SQLite si ya tienes una tabla categoría.
        // Por ahora dejaremos algo de prueba:
        val categorias = listOf(
            "Tortas Cuadradas",
            "Tortas Circulares",
            "Postres Individuales",
            "Productos Sin Azúcar",
            "Pastelería Tradicional",
            "Productos sin gluten",
            "Productos Veganos",
            "Tortas Especiales"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter
    }

    /** ============================
     *      Abrir Cámara
     *  ============================ */
    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA_CODE
            )
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }
    /** ============================
     *      RESULTADO PERMISO
     *  ============================ */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                Toast.makeText(this, "Debes aceptar el permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** ============================
     *      RESULTADO DE FOTO
     *  ============================ */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val foto = data?.extras?.get("data") as Bitmap
            imgPreview.setImageBitmap(foto)

            imagePath = guardarImagenInterna(foto)
        }
    }

    /** ============================
     *  Guardar imagen en memoria
     *  ============================ */
    private fun guardarImagenInterna(bitmap: Bitmap): String {
        val filename = "torta_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, filename)

        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.flush()
        stream.close()

        return file.absolutePath
    }

    /** ============================
     *      Guardar Torta SQLite
     *  ============================ */
    private fun guardarTorta() {
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precio = etPrecio.text.toString().toIntOrNull()
        val stock = etStock.text.toString().toIntOrNull()
        val categoriaIndex = spCategoria.selectedItemPosition
        val personalizable = swPersonalizable.isChecked

        if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || stock == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(
            id_producto = null,
            id_categoria = categoriaIndex,
            nombre_produ = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            personalizable = personalizable,
            imagen = imagePath
        )

        val result = dao.insertar(producto)

        if (result > 0) {
            Toast.makeText(this, "Torta registrada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
        }
    }

}