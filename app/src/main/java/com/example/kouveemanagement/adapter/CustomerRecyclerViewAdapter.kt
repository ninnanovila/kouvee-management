package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import kotlinx.android.extensions.LayoutContainer

class CustomerRecyclerViewAdapter(private val customers: MutableList<Customer>, private val listener: (Customer) -> Unit) : RecyclerView.Adapter<CustomerRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = customers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(customers[position], listener)
    }

    class ViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var id: TextView = itemView.findViewById(R.id.id)
        private var name: TextView = itemView.findViewById(R.id.name)
        private var phone_number: TextView = itemView.findViewById(R.id.phone_number)
        private var birthdate: TextView = itemView.findViewById(R.id.birthdate)

        fun bindItem(customer: Customer, listener: (Customer) -> Unit) {

            id.text = customer.id
            name.text = customer.name
            phone_number.text = customer.phone_number
            birthdate.text = customer.birthdate

            containerView.setOnClickListener {
                listener(customer)
            }
        }
    }
}