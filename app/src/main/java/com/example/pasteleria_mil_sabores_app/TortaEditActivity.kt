package com.example.pasteleria_mil_sabores_app


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.dao.ProductoDAO
import com.example.pasteleria_mil_sabores_app.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TortaEditActivity : AppCompatActivity() {

    private lateinit var dao: ProductoDAO

    private lateinit var imgTorta: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var chkPersonalizable: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnVolver: Button

    private var idProducto: Long = -1L
    private var imagenGuardada: String? = null   // base64 o ruta
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_torta_edit)

        dao = ProductoDAO(this)

        imgTorta = findViewById(R.id.imgTortaEditar1)
        etNombre = findViewById(R.id.etNombreTorta1)
        etDescripcion = findViewById(R.id.etDescripcionTorta1)
        etPrecio = findViewById(R.id.etPrecioTorta1)
        etStock = findViewById(R.id.etStockTorta1)
        chkPersonalizable = findViewById(R.id.chkPersonalizable1)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVolver = findViewById(R.id.btnVolver)

        idProducto = intent.getLongExtra("id_producto", -1L)

        if (idProducto == -1L) {
            Toast.makeText(this, "Error: no se recibió el ID del producto", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDatos(idProducto)

        btnGuardar.setOnClickListener {
            actualizarProducto()
        }

        btnCancelar.setOnClickListener { finish() }
        btnVolver.setOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun cargarDatos(id: Long) {
        // Como no tenemos un método obtenerProductoPorId(), lo buscamos desde listar()
        val producto = dao.listar().find { it.id_producto == id }

        if (producto != null) {
            etNombre.setText(producto.nombre_produ)
            etDescripcion.setText(producto.descripcion)
            etPrecio.setText(producto.precio.toString())
            etStock.setText(producto.stock.toString())
            chkPersonalizable.isChecked = producto.personalizable
            imagenGuardada = producto.imagen

            if (!imagenGuardada.isNullOrEmpty()) {
                try {
                    val file = File(imagenGuardada!!)
                    if (file.exists()) {
                        Glide.with(this)
                            .load(file)
                            .into(imgTorta)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "Producto no encontrado en SQLite", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun actualizarProducto() {

        val nombre = etNombre.text.toString()
        val descripcion = etDescripcion.text.toString()
        val precio = etPrecio.text.toString().toIntOrNull() ?: 0
        val stock = etStock.text.toString().toIntOrNull() ?: 0
        val personalizable = chkPersonalizable.isChecked

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val productoActualizado = Producto(
            id_producto = idProducto,
            id_categoria = 1,  // aquí puedes cambiar la categoría si usas spinner luego
            nombre_produ = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            personalizable = personalizable,
            imagen = imagenGuardada
        )

        val result = dao.actualizar(productoActualizado)

        if (result > 0) {
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

}