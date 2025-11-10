package com.example.pasteleria_mil_sabores_app


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TortaEditActivity : AppCompatActivity() {
    private lateinit var imgTortaEditar: ImageView
    private lateinit var etNombreTorta: EditText
    private lateinit var etDescripcionTorta: EditText
    private lateinit var etPrecioTorta: EditText
    private lateinit var etStockTorta: EditText
    private lateinit var chkPersonalizable: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnVolver: Button

    private var idProducto: Long = 0
    private var imagenUrl: String? = null
    private var idCategoria: Int = 1 // Valor por defecto, puedes ajustarlo según tu API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_torta_edit)

        // Vincular vistas
        imgTortaEditar = findViewById(R.id.imgTortaEditar1)
        etNombreTorta = findViewById(R.id.etNombreTorta1)
        etDescripcionTorta = findViewById(R.id.etDescripcionTorta1)
        etPrecioTorta = findViewById(R.id.etPrecioTorta1)
        etStockTorta = findViewById(R.id.etStockTorta1)
        chkPersonalizable = findViewById(R.id.chkPersonalizable1)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVolver = findViewById(R.id.btnVolver)

        // Recibir datos del intent
        idProducto = intent.getLongExtra("id", 0)
        etNombreTorta.setText(intent.getStringExtra("nombre"))
        etDescripcionTorta.setText(intent.getStringExtra("descripcion"))
        etPrecioTorta.setText(intent.getDoubleExtra("precio", 0.0).toString())
        etStockTorta.setText(intent.getIntExtra("stock", 0).toString())
        imagenUrl = intent.getStringExtra("imagen")
        idCategoria = intent.getIntExtra("categoria", 1)
        chkPersonalizable.isChecked = intent.getBooleanExtra("personalizable", false)

        // Mostrar imagen con Glide
        imagenUrl.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgTortaEditar)
        }

        // Botón: Guardar cambios
        btnGuardar.setOnClickListener {
            actualizarProducto()
        }

        // Botón: Cancelar → limpia los campos
        btnCancelar.setOnClickListener {
            limpiarCampos()
        }

        // Botón: Volver → cierra la activity
        btnVolver.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun actualizarProducto() {
        val nombre = etNombreTorta.text.toString().trim()
        val descripcion = etDescripcionTorta.text.toString().trim()
        val precio = etPrecioTorta.text.toString().toInt()
        val stock = etStockTorta.text.toString().toIntOrNull() ?: 0
        val personalizable = chkPersonalizable.isChecked

        if (nombre.isEmpty() || descripcion.isEmpty() || precio <= 0) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val productoActualizado = Producto(
            id_producto = idProducto,
            nombre_produ = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            id_categoria = idCategoria,
            imagen = imagenUrl,
            personalizable = personalizable
        )

        val api = RetrofitClient.instance
        api.actualizarProducto(idProducto, productoActualizado)
            .enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TortaEditActivity, "Torta actualizada correctamente", Toast.LENGTH_SHORT).show()
                        finish() // vuelve al catálogo
                    } else {
                        Toast.makeText(this@TortaEditActivity, "Error al actualizar (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Producto>, t: Throwable) {
                    Toast.makeText(this@TortaEditActivity, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun limpiarCampos() {
        etNombreTorta.text.clear()
        etDescripcionTorta.text.clear()
        etPrecioTorta.text.clear()
        etStockTorta.text.clear()
        chkPersonalizable.isChecked = false
    }
}