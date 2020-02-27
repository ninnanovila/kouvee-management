package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Service
import kotlinx.android.extensions.LayoutContainer


class ServiceRecyclerViewAdapter(private val services: MutableList<Service>, private val listener: (Service) -> Unit) : RecyclerView.Adapter<ServiceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = services.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(services[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var id: TextView = itemView.findViewById(R.id.id)
        private var name: TextView = itemView.findViewById(R.id.name)
        private var price: TextView = itemView.findViewById(R.id.price)

        fun bindItem(service: Service, listener: (Service) -> Unit) {
            id.text = service.id
            name.text = service.name
            price.text = service.price.toString()

            containerView.setOnClickListener {
                listener(service)
            }
        }
    }
}