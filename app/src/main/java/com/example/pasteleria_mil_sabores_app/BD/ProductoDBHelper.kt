package com.example.pasteleria_mil_sabores_app.BD

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductoDBHelper (context: Context) : SQLiteOpenHelper(context,"productos.db",null,1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                
            CREATE TABLE producto(
                id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
                id_categoria INTEGER NOT NULL,
                nombre_produ TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                precio INTEGER NOT NULL,
                stock INTEGER NOT NULL,
                personalizable INTEGER NOT NULL,
                imagen TEXT
            ); 
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}