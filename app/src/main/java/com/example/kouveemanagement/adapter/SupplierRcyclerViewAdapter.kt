package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Supplier
import kotlinx.android.extensions.LayoutContainer

class SupplierRcyclerViewAdapter(private val suppliers: MutableList<Supplier>, private val listener: (Supplier) -> Unit) : RecyclerView.Adapter<SupplierRcyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_supplier, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = suppliers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(suppliers[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var id: TextView = itemView.findViewById(R.id.id)
        private var name: TextView = itemView.findViewById(R.id.name)
        private var phone_number: TextView = itemView.findViewById(R.id.phone_number)

        fun bindItem(supplier: Supplier, listener: (Supplier) -> Unit) {
            id.text = supplier.id
            name.text = supplier.name
            phone_number.text = supplier.phone_number

            containerView.setOnClickListener {
                listener(supplier)
            }
        }
    }

}