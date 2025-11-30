package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.adapter.UsuarioAdapter
import com.example.pasteleria_mil_sabores_app.model.Usuario
import retrofit2.*


class AdministracionActivity : AppCompatActivity() {

    private lateinit var rvUsuarios: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_administracion)

        rvUsuarios = findViewById(R.id.rvUsuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(this)

        cargarUsuarios()

        //volever
        val btn_volver: Button=findViewById(R.id.btnVolver2)

        btn_volver.setOnClickListener {
            val volver = Intent(this, MainActivity::class.java)
            startActivity(volver)
        }

    }
    private fun cargarUsuarios() {
        RetrofitClient.usuarioApi.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(
                call: Call<List<Usuario>>,
                response: Response<List<Usuario>>
            ) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    rvUsuarios.adapter = UsuarioAdapter(lista)
                } else {
                    Toast.makeText(
                        this@AdministracionActivity,
                        "Error al obtener usuarios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(
                    this@AdministracionActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}