package com.example.pasteleria_mil_sabores_app.API

import com.example.pasteleria_mil_sabores_app.model.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApi {

    @GET("productos")
    fun  getProductos(): Call<List<Producto>>

    @POST("productos")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("productos/{id}")
    fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: Producto
    ): Call<Producto>
}