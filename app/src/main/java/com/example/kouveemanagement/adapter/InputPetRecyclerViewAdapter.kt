package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerPet
import kotlinx.android.extensions.LayoutContainer

class InputPetRecyclerViewAdapter(private val customers: MutableList<Customer>, private val customerPets: MutableList<CustomerPet>): RecyclerView.Adapter<InputPetRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InputPetRecyclerViewAdapter.ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet_and_customer, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return customerPets.size
    }

    override fun onBindViewHolder(holder: InputPetRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItem(customers, customerPets[position])
    }

    class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var petName: TextView = itemView.findViewById(R.id.pet_name)
        private var customerName: TextView = itemView.findViewById(R.id.customer_name)

        fun bindItem(customers: MutableList<Customer>, customerPet: CustomerPet){
            petName.text = customerPet.name
            for (customer in customers){
                if (customer.id.equals(customerPet.id_customer)){
                    customerName.text = customer.name.toString()
                }
            }
        }

    }
}