package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasteleria_mil_sabores_app.API.ProductoAdapter
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.model.Producto
import com.example.pasteleria_mil_sabores_app.TortaEditActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class CatalogoActivity : AppCompatActivity() {

    private lateinit var recyclerTortas: RecyclerView
    private lateinit var  adapter: ProductoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo)

        recyclerTortas=findViewById(R.id.recyclerTortas)
        recyclerTortas.layoutManager= LinearLayoutManager(this)

        obtenerProductos()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun obtenerProductos(){
        val api= RetrofitClient.instance

        api.getProductos().enqueue(object : Callback<List<Producto>>{
            override fun onResponse(call: Call<List<Producto>>,response: Response<List<Producto>>){
                if (response.isSuccessful){
                    val lista=response.body() ?: emptyList()
                    recyclerTortas.adapter = ProductoAdapter(lista) { productoSeleccionado ->
                        val intent = Intent(this@CatalogoActivity, TortaEditActivity::class.java)
                        intent.putExtra("id", productoSeleccionado.id_producto)
                        intent.putExtra("nombre", productoSeleccionado.nombre_produ)
                        intent.putExtra("descripcion", productoSeleccionado.descripcion)
                        intent.putExtra("precio", productoSeleccionado.precio)
                        intent.putExtra("stock", productoSeleccionado.stock)
                        intent.putExtra("categoria", productoSeleccionado.id_categoria)
                        intent.putExtra("imagen", productoSeleccionado.imagen)
                        intent.putExtra("personalizable", productoSeleccionado.personalizable)
                        startActivity(intent)
                    }
                }else{
                    Log.e("API","Error al obtener productos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Log.e("API", "Fallo de conexión: ${t.message}")
            }
        })

    }

}