package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.customer.CustomerManagementActivity
import com.example.kouveemanagement.model.Customer
import kotlinx.android.extensions.LayoutContainer
import java.util.*

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

    fun filterCustomer(input: String){
        CustomerManagementActivity.customers.clear()
        if (input.isEmpty()){
            CustomerManagementActivity.customers.addAll(customers)
        }else{
            var i = 0
            while (i < customers.size){
                if (customers[i].name?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                    CustomerManagementActivity.customers.add(customers[i])
                }
                i++
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var name: TextView = itemView.findViewById(R.id.name)
        private var phoneNumber: TextView = itemView.findViewById(R.id.phone_number)
        private var birthdate: TextView = itemView.findViewById(R.id.birthdate)

        fun bindItem(customer: Customer, listener: (Customer) -> Unit) {

            name.text = customer.name
            phoneNumber.text = customer.phone_number
            birthdate.text = customer.birthdate

            containerView.setOnClickListener {
                listener(customer)
            }
        }
    }
}