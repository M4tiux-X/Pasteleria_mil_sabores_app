package com.example.pasteleria_mil_sabores_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.model.Producto
import java.io.File

class ProductoAdapter (
    private val productos: List<Producto>,
    private val onEditarClick: (Producto)-> Unit
): RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>(){
    class ProductoViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarProducto)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ProductoViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.tvNombre.text = producto.nombre_produ
        holder.tvDescripcion.text = producto.descripcion
        holder.tvPrecio.text = "$${producto.precio}"

        /** IMAGEN */
        if (producto.imagen != null) {
            Glide.with((holder.itemView.context)).load(File(producto.imagen!!)).into(holder.imgProducto)
        }else{
            holder.imgProducto.setImageResource(R.drawable.ic_launcher_background)
        }

        /** BOTÓN EDITAR */
        holder.btnEditar.setOnClickListener {
            onEditarClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size

}