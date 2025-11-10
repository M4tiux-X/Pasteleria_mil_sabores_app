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
import com.example.pasteleria_mil_sabores_app.model.Producto
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class AgregarTorta : AppCompatActivity() {
    private lateinit var imgPreview: ImageView
    private lateinit var btnTomarFoto: Button
    private lateinit var btnGuardar: Button
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var spCategoria: Spinner
    private lateinit var swPersonalizable: Switch


    private var imageBitmap: Bitmap? = null
    private var imageUrl: String? = null
    private var categoriaSeleccionadaId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_torta)

        imgPreview = findViewById(R.id.imgPreview)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        btnGuardar = findViewById(R.id.btnGuardar)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        spCategoria = findViewById(R.id.spCategoria)
        swPersonalizable = findViewById(R.id.swPersonalizable)

        val categorias = listOf(
            "Seleccione una categoría",
            "Tortas Cuadradas",
            "Tortas Circulares",
            "Postres Individuales",
            "Productos Sin Azúcar",
            "Pastelería Tradicional",
            "Productos sin gluten",
            "Productos Veganos",
            "Tortas Especiales"
        )
        val categoriaIds = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        spCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                categoriaSeleccionadaId = categoriaIds[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        btnTomarFoto.setOnClickListener { mostrarOpcionesImagen() }

        // --- Botón guardar producto ---
        btnGuardar.setOnClickListener { guardarProducto() }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarOpcionesImagen() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar imagen")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> abrirCamara()
                    1 -> abrirGaleria()
                }
            }
            .show()
    }

    private val tomarFotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as Bitmap
            imageBitmap = bitmap
            imgPreview.setImageBitmap(bitmap)
            subirImagenAImgur(bitmap)
        }
    }

    private val seleccionarGaleriaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = it.data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageBitmap = bitmap
            Glide.with(this).load(uri).into(imgPreview)
            subirImagenAImgur(bitmap)
        }
    }

    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            tomarFotoLauncher.launch(intent)
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccionarGaleriaLauncher.launch(intent)
    }

    // --- Subir imagen a Imgur (sin cuenta, modo anónimo) ---
    private fun subirImagenAImgur(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val client = OkHttpClient()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", imageBase64)
            .build()

        val request = Request.Builder()
            .url("https://api.imgur.com/3/image")
            .addHeader("Authorization", "Client-ID 1379d1f9c1a5a7f") // puedes registrar uno propio en Imgur
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AgregarTorta, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body?.string() ?: "")
                imageUrl = json.getJSONObject("data").getString("link")
                runOnUiThread {
                    Toast.makeText(this@AgregarTorta, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun guardarProducto() {
        val nombre = etNombre.text.toString()
        val descripcion = etDescripcion.text.toString()
        val precio = etPrecio.text.toString().toInt()
        val stock = etStock.text.toString().toIntOrNull() ?: 0
        val personalizable = swPersonalizable.isChecked

        if (nombre.isEmpty() || descripcion.isEmpty() || imageUrl == null || categoriaSeleccionadaId == 0) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(
            nombre_produ = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            id_categoria = categoriaSeleccionadaId,
            imagen = imageUrl!!,
            personalizable = personalizable
        )

        RetrofitClient.instance.crearProducto(producto).enqueue(object : retrofit2.Callback<Producto> {
            override fun onResponse(call: retrofit2.Call<Producto>, response: retrofit2.Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AgregarTorta, "Torta guardada correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AgregarTorta, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Producto>, t: Throwable) {
                Toast.makeText(this@AgregarTorta, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}