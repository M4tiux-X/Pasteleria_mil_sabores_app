package com.example.pasteleria_mil_sabores_app.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val usuario: Usuario?
)
