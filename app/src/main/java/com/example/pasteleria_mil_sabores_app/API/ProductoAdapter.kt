package com.example.pasteleria_mil_sabores_app.API

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.pasteleria_mil_sabores_app.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pasteleria_mil_sabores_app.model.Producto

class ProductoAdapter(private val listaProductos: List<Producto>,private val onItemClick: (Producto) -> Unit):
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgProducto: ImageView= itemView.findViewById(R.id.imgProducto)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvDescripcionProducto: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
        val tvPrecioProducto: TextView = itemView.findViewById(R.id.tvPrecioProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.activity_item,parent,false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto=listaProductos[position]

        holder.tvNombreProducto.text=producto.nombre_produ
        holder.tvDescripcionProducto.text=producto.descripcion
        holder.tvPrecioProducto.text="$${producto.precio}"

        Glide.with(holder.itemView.context)
            .load(producto.imagen)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgProducto)

        holder.itemView.setOnClickListener {
            onItemClick(producto)
        }

    }

    override fun getItemCount(): Int =listaProductos.size
}