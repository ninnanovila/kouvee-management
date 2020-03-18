package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.supplier.SupplierManagementActivity
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class SupplierRecyclerViewAdapter(private val suppliers: MutableList<Supplier>, private val listener: (Supplier) -> Unit) : RecyclerView.Adapter<SupplierRecyclerViewAdapter.ViewHolder>() {

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

    fun filterSupplier(input: String){
        SupplierManagementActivity.suppliers.clear()
        if (input.isEmpty()){
            SupplierManagementActivity.suppliers.addAll(suppliers)
        }else{
            var i = 0
            while (i < suppliers.size){
                if (suppliers[i].name?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                    SupplierManagementActivity.suppliers.add(suppliers[i])
                }
                i++
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var name: TextView = itemView.findViewById(R.id.name)
        private var phoneNumber: TextView = itemView.findViewById(R.id.phone_number)

        fun bindItem(supplier: Supplier, listener: (Supplier) -> Unit) {
            name.text = supplier.name
            phoneNumber.text = supplier.phone_number

            containerView.setOnClickListener {
                listener(supplier)
            }
        }
    }

}