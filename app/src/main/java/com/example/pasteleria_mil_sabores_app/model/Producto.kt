package com.example.pasteleria_mil_sabores_app.model

import android.media.Image
import kotlinx.serialization.Serializable

@Serializable
data class Producto (
    val id_producto: Long?=null,
    val id_categoria: Int,
    val nombre_produ: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val personalizable: Boolean,
    val imagen: Image
)


