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

    private var idTorta: Long = 0

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

        // Recibir datos del intent (producto seleccionado)
        idTorta = intent.getLongExtra("id_pedido", 0)
        etNombre.setText(intent.getStringExtra("nombre_prod"))
        etDescripcion.setText(intent.getStringExtra("descripcion"))
        etPrecio.setText(intent.getIntExtra("precio", 0).toString())
        etStock.setText(intent.getIntExtra("stock", 0).toString())
        chkPersonalizable.isChecked = intent.getBooleanExtra("personalizable", false)

        val urlImagen = intent.getStringExtra("imagen")
        if (!urlImagen.isNullOrEmpty()) {
            Glide.with(this).load(urlImagen).into(imgTortaEditar)
        }

        btnGuardar.setOnClickListener {
            val tortaActualizada = Producto(
                id_producto = idTorta,
                id_categoria = 1, // puedes ajustar esto si tienes categorías
                nombre_produ = etNombre.text.toString(),
                descripcion = etDescripcion.text.toString(),
                precio = etPrecio.text.toString().toInt(),
                stock = etStock.text.toString().toInt(),
                personalizable = chkPersonalizable.isChecked,
                imagen = urlImagen ?: "" /*se agrega la imagen selecionada*/
            )


        //boton volver
        val btn_volver: Button=findViewById(R.id.btnVolver)

        btn_volver.setOnClickListener {
            val volver = Intent(this, MainActivity::class.java)
            startActivity(volver)
        }

            val api = RetrofitClient.instance.actualizarProducto(Producto::class.java)
            api.actualizarProducto(idTorta, tortaActualizada).enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Torta actualizada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Producto>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
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