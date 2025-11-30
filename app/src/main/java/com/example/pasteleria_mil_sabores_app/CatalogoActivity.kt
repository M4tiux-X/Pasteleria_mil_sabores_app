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
import com.example.pasteleria_mil_sabores_app.ProductoAdapter
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.model.Producto
import com.example.pasteleria_mil_sabores_app.TortaEditActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.pasteleria_mil_sabores_app.dao.ProductoDAO

class CatalogoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dao: ProductoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo)

        dao= ProductoDAO(this)
        recyclerView=findViewById(R.id.recyclerTortas)

        cargarProductos()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun cargarProductos() {
        val productos = dao.listar()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductoAdapter(productos) { producto ->
            abrirEditarProducto(producto.id_producto!!)
        }
    }
    private fun abrirEditarProducto(id: Long) {
        val intent = Intent(this, TortaEditActivity::class.java)
        intent.putExtra("id_producto", id)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        cargarProductos() // para refrescar después de editar
    }

}