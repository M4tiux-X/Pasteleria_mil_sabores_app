package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.API.ProductoApi
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.model.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TortaEditActivity : AppCompatActivity() {

    private lateinit var imgTortaEditar: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var chkPersonalizable: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnVolver: Button

    private var idTorta: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_torta_edit)

        imgTortaEditar = findViewById(R.id.imgTortaEditar)
        etNombre = findViewById(R.id.etNombreTorta)
        etDescripcion = findViewById(R.id.etDescripcionTorta)
        etPrecio = findViewById(R.id.etPrecioTorta)
        etStock = findViewById(R.id.etStockTorta)
        chkPersonalizable = findViewById(R.id.chkPersonalizable)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVolver = findViewById(R.id.btnVolver)

        // --- Recibir datos del intent (asegúrate que las keys coincidan con las que usas al lanzar el intent)
        idTorta =
            intent.getLongExtra("id_pedido", 0L) // verifica la key: "id_pedido" o "id_producto"
        // Manejo seguro de posibles nulls:
        etNombre.setText(intent.getStringExtra("nombre_prod") ?: "")
        etDescripcion.setText(intent.getStringExtra("descripcion") ?: "")
        // parseo seguro: si no hay número, poner 0 en el campo visible (o preferir dejar vacío)
        val precioIntent = intent.getIntExtra("precio", -1)
        if (precioIntent >= 0) etPrecio.setText(precioIntent.toString()) else etPrecio.setText("")
        val stockIntent = intent.getIntExtra("stock", -1)
        if (stockIntent >= 0) etStock.setText(stockIntent.toString()) else etStock.setText("")
        chkPersonalizable.isChecked = intent.getBooleanExtra("personalizable", false)

        val urlImagen = intent.getStringExtra("imagen")
        if (!urlImagen.isNullOrEmpty()) {
            Glide.with(this).load(urlImagen).into(imgTortaEditar)
        } else {
            // opcional: imagen por defecto
            imgTortaEditar.setImageResource(android.R.drawable.ic_menu_report_image)
// asegúrate de tener este recurso
        }

                // BOTON VOLVER (fuera del onClick de guardar)
                btnVolver.setOnClickListener {
                    val volver = Intent(this, MainActivity::class.java)
                    startActivity(volver)
                    finish()
                }

        btnGuardar.setOnClickListener {
            // validaciones básicas antes de construir el objeto
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precio = etPrecio.text.toString().toIntOrNull() ?: 0
            val stock = etStock.text.toString().toIntOrNull() ?: 0
            val personalizable = chkPersonalizable.isChecked

            if (nombre.isEmpty()) {
                etNombre.error = "Ingrese nombre"
                etNombre.requestFocus()
                return@setOnClickListener
            }

            // Construir producto (ajusta tipos según tu modelo)
            val tortaActualizada = Producto(
                id_producto = idTorta,
                id_categoria = 1, // ajusta si corresponde
                nombre_produ = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = stock,
                personalizable = personalizable,
                imagen = urlImagen ?: "" // si tu modelo espera String esto está bien
            )

            // Obtener la interfaz API correctamente desde Retrofit
            val api = RetrofitClient.instance
            api.actualizarProducto(idTorta, tortaActualizada).enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Torta actualizada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al actualizar: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Producto>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}