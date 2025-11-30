package com.example.pasteleria_mil_sabores_app.API

import android.se.omapi.Session
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    /*CAMBIO DE IP
    * en la CMD escribir: ipconfig, copiar y pegar la direcion del IPv4
    *  y correr la api ocupando el siguiente comando:
    * mvn spring-boot:run -Dspring-boot.run.arguments="--server.address=0.0.0.0"*/
    private const val URL_USUARIO="http://192.168.0.19:8090/api/"
    private val logging= HttpLoggingInterceptor().apply {
        level= HttpLoggingInterceptor.Level.BODY
    }

    private val clientUsu = OkHttpClient.Builder().addInterceptor(logging).build()


    val usuarioApi: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_USUARIO)
            .client(clientUsu)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApi::class.java)
    }

}