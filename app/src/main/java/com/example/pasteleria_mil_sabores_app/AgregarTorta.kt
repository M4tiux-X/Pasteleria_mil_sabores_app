package com.example.pasteleria_mil_sabores_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts

class AgregarTorta : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnTomarFoto: Button
    private var imagenTorta: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_torta)

        imgPreview = findViewById(R.id.imgPreview)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)

        // Abrir cámara
        val tomarFoto = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imagenTorta = data?.extras?.get("data") as Bitmap
                imgPreview.setImageBitmap(imagenTorta)
            }
        }

        btnTomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            tomarFoto.launch(intent)
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val descripcion = etDescripcion.text.toString()
            val precio = etPrecio.text.toString()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || imagenTorta == null) {
                Toast.makeText(this, "Por favor completa todos los campos y toma una foto", Toast.LENGTH_SHORT).show()
            } else {
                // Aquí iría la lógica para guardar en la API o base de datos local
                Toast.makeText(this, "Torta guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}