package com.example.pasteleria_mil_sabores_app.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id_usuario: Long?=null,
    val id_tipo_usu: Int=2,//usuario normal predeterminado
    val nombre: String,
    val apellido: String,
    val correo: String,
    val pass: String
)
