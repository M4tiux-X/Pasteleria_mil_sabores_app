package com.example.pasteleria_mil_sabores_app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pasteleria_mil_sabores_app.API.RetrofitClient
import com.example.pasteleria_mil_sabores_app.API.UsuarioApi
import com.example.pasteleria_mil_sabores_app.model.LoginRequest
import com.example.pasteleria_mil_sabores_app.model.LoginResponse
import com.example.pasteleria_mil_sabores_app.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngresarCuentaActivity : AppCompatActivity() {

    private lateinit var correoInput: EditText
    private lateinit var passInput: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingresar_cuenta)

        correoInput = findViewById<EditText>(R.id.ed_correo)
        passInput = findViewById<EditText>(R.id.ed_pass)
        btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            iniciarSesion()
        }

        //Crear secion
        val btn_crear: Button=findViewById(R.id.btn_CrearCuenta)
        btn_crear.setOnClickListener {
            val crear= Intent(this, CreacionCuentasActivity::class.java)
            startActivity(crear)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun iniciarSesion(){
        val correo = correoInput.text.toString().trim()
        val pass = passInput.text.toString().trim()

        if (correo.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(correo, pass)

        RetrofitClient.usuarioApi.login(request).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if(response.isSuccessful && response.body()!= null){
                    val loginResponse = response.body()!!

                    if(loginResponse.success){
                        guardarSesion(loginResponse.usuario!!)

                        if (loginResponse.usuario.id_tipo_usu==1) {
                            Toast.makeText(
                                this@IngresarCuentaActivity,
                                "Bienvenido ${loginResponse.usuario.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@IngresarCuentaActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(
                                this@IngresarCuentaActivity,
                                "Acceso denegado: solo administradores pueden ingresar.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }else{
                        Toast.makeText(
                            this@IngresarCuentaActivity,
                            loginResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        this@IngresarCuentaActivity,
                        "Error en las credenciales",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@IngresarCuentaActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
    private fun guardarSesion(usuario: Usuario) {
        val prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("correo", usuario.correo)
            putString("nombre", usuario.nombre)
            putInt("tipo", usuario.id_tipo_usu)
            apply()
        }
    }
}