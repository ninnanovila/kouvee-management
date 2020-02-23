package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Menu
import kotlinx.android.extensions.LayoutContainer

class MenuRecyclerViewAdapter (private val menu: MutableList<Menu>, private val listener: (Menu) -> Unit) : RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = menu.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(menu[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var name: TextView = itemView.findViewById(R.id.name)
        private var desc: TextView = itemView.findViewById(R.id.desc)

        fun bindItem(menu: Menu, listener: (Menu) -> Unit) {
            name.text = menu.name
            desc.text = menu.desc

            containerView.setOnClickListener{
                listener(menu)
            }
        }

    }

}