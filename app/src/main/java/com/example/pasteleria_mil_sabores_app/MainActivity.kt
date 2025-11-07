package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn_ingresar: Button=findViewById(R.id.btnIngresar)
        val btn_Crear: Button=findViewById(R.id.btnCrear)
        val btn_catalogo: Button=findViewById(R.id.btnVerCatalogo)
        val btn_perfiles: Button=findViewById(R.id.btnPerfiles)
        val btn_editarTorta: Button=findViewById(R.id.btnEditarTorta)
        val btn_agregar: Button=findViewById(R.id.btnAgregarTorta)

        btn_ingresar.setOnClickListener {
            val ingresar= Intent(this, IngresarCuentaActivity::class.java)
            startActivity(ingresar)
        }

        btn_Crear.setOnClickListener {
            val crear= Intent(this, CreacionCuentasActivity::class.java)
            startActivity(crear)
        }

        btn_catalogo.setOnClickListener {
            val catalogo= Intent(this, CatalogoActivity::class.java)
            startActivity(catalogo)
        }

        btn_perfiles.setOnClickListener {
            val perfiles= Intent(this, AdministracionActivity::class.java)
            startActivity(perfiles)
        }

        btn_editarTorta.setOnClickListener {
            val editar= Intent(this, TortaEditActivity::class.java)
            startActivity(editar)
        }

        btn_agregar.setOnClickListener {
            val agregar= Intent(this, AgregarTorta::class.java)
            startActivity(agregar)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}