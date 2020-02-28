package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.CustomerPet
import kotlinx.android.extensions.LayoutContainer

class CustomerPetRecyclerViewAdapter(private val customerpet: MutableList<CustomerPet>, private val listener: (CustomerPet) -> Unit) : RecyclerView.Adapter<CustomerPetRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_pet, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = customerpet.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindItem(customerpet[position], listener)
    }

    class ViewHolder (override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer{

        private var id: TextView = itemView.findViewById(R.id.id)
        private var name: TextView = itemView.findViewById(R.id.name)
        private var birthdate: TextView = itemView.findViewById(R.id.birthdate)

        fun bindItem(customerPet: CustomerPet, listener: (CustomerPet) -> Unit){
            id.text = customerPet.id
            name.text = customerPet.name
            birthdate.text = customerPet.birthdate

            containerView.setOnClickListener {
                listener(customerPet)
            }
        }
    }

}