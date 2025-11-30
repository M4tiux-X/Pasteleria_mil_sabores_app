package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.model.Usuario
import retrofit2.*

class CreacionCuentasActivity : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var correo: EditText
    private lateinit var pass: EditText
    private lateinit var btnCrear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creacion_cuentas)

        nombre = findViewById<EditText>(R.id.ed_nombre)
        apellido = findViewById<EditText>(R.id.ed_apellido)
        correo = findViewById<EditText>(R.id.ed_email)
        pass = findViewById<EditText>(R.id.ed_pass)
        btnCrear = findViewById<Button>(R.id.btn_crear)


        btnCrear.setOnClickListener {
            registraUsuario()
        }

        //iniciar sesion
        val btn_ini: Button=findViewById(R.id.btn_ingresar)
        btn_ini.setOnClickListener {
            val ini= Intent(this, IngresarCuentaActivity::class.java)
            startActivity(ini)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun registraUsuario(){
        val usuario = Usuario(
            nombre = nombre.text.toString(),
            apellido = apellido.text.toString(),
            correo = correo.text.toString(),
            pass = pass.text.toString()
        )

        RetrofitClient.usuarioApi.crearUsuario(usuario).enqueue(object : retrofit2.Callback<Usuario>{
            override fun onResponse(call: Call<Usuario?>, response: Response<Usuario?>) {
                if(response.isSuccessful){
                    Toast.makeText(this@CreacionCuentasActivity,"Usuario creado", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@CreacionCuentasActivity,"Error al registrar", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Usuario?>, t: Throwable) {
                Toast.makeText(this@CreacionCuentasActivity,"Error: ${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }

}