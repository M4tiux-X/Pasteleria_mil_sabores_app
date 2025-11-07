package com.example.pasteleria_mil_sabores_app.model

data class Usuario (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val contraseña: String,
    val fec_nac: String,
    val direccion: String,
    val telefono: String,
    val cod_registro: String
)