package com.example.pasteleria_mil_sabores_app.dao

import android.content.ContentValues
import android.content.Context
import com.example.pasteleria_mil_sabores_app.BD.ProductoDBHelper
import com.example.pasteleria_mil_sabores_app.model.Producto

class ProductoDAO (context: Context) {

    private val dbHelper= ProductoDBHelper(context)

    fun insertar (producto: Producto): Long{
        val db =dbHelper.writableDatabase

        val values= ContentValues().apply {
            put("id_categoria", producto.id_categoria)
            put("nombre_produ", producto.nombre_produ)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("personalizable", if (producto.personalizable) 1 else 0)// 1= si 2=no
            put("imagen", producto.imagen)
        }
        return  db.insert("producto", null, values)
    }

    fun actualizar(producto: Producto): Int{
        val db= dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_categoria", producto.id_categoria)
            put("nombre_produ", producto.nombre_produ)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("personalizable", if (producto.personalizable) 1 else 0)// 1= si 2=no
            put("imagen", producto.imagen)
        }
        return db.update(
            "producto",
            values,
            "id_producto=?",
            arrayOf(producto.id_producto.toString())
        )
    }

    fun desactivarProducto(id: Long):Int{
        val db= dbHelper.writableDatabase

        val values= ContentValues().apply {
            put("Stock",0)//stock 0 no se visualizara
        }
        return db.update("producto",values,"id_producto=?",arrayOf(id.toString()))
    }

    fun listar(): List<Producto>{
        val db=dbHelper.readableDatabase
        val lista=mutableListOf<Producto>()

        val cursor=db.rawQuery(
            "SELECT * FROM producto WHERE stock > 0",  //solo listaremos los que tiene stock
            null
        )
        if(cursor.moveToFirst()){
            do {
                lista.add(
                    Producto(
                        id_producto = cursor.getLong(0),
                        id_categoria = cursor.getInt(1),
                        nombre_produ = cursor.getString(2),
                        descripcion = cursor.getString(3),
                        precio = cursor.getInt(4),
                        stock = cursor.getInt(5),
                        personalizable = cursor.getInt(6) == 1,
                        imagen = cursor.getString(7)
                    )
                )
            }while (cursor.moveToNext())
        }
        cursor.close()
        return  lista
    }
}