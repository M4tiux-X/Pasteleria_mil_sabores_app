package com.example.pasteleria_mil_sabores_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasteleria_mil_sabores_app.R
import com.example.pasteleria_mil_sabores_app.model.Usuario

class UsuarioAdapter(
    private var lista: List<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreUsuario)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreoUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = lista[position]
        holder.txtNombre.text = "${usuario.nombre} ${usuario.apellido}"
        holder.txtCorreo.text = usuario.correo
    }

    override fun getItemCount(): Int = lista.size
}
