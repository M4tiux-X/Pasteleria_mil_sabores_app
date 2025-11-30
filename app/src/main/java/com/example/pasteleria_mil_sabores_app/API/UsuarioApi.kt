package com.example.pasteleria_mil_sabores_app.API

import com.example.pasteleria_mil_sabores_app.model.LoginRequest
import com.example.pasteleria_mil_sabores_app.model.LoginResponse
import com.example.pasteleria_mil_sabores_app.model.Usuario
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {

    @GET("usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @POST("usuario")
    fun crearUsuario(@Body usuario: Usuario): Call<Usuario>

    @POST("usuario/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @PUT("usuario/{id}")
    fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: Usuario
    ): Call<Usuario>
}