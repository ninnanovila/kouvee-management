package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.customerpet.CustomerPetManagementActivity
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.PetType
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class CustomerPetRecyclerViewAdapter(private val petTypes: MutableList<PetType>, private val customerPets: MutableList<CustomerPet>, private val listener: (CustomerPet) -> Unit) : RecyclerView.Adapter<CustomerPetRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_pet, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = customerPets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(petTypes, customerPets[position], listener)
    }

    fun filterCustomerPet(input: String){
        CustomerPetManagementActivity.customerPets.clear()
        if (input.isEmpty()){
            CustomerPetManagementActivity.customerPets.addAll(customerPets)
        }else{
            var i = 0
            while (i < customerPets.size){
                if (customerPets[i].name?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                    CustomerPetManagementActivity.customerPets.add(customerPets[i])
                }
                i++
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder (override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer{

        private var name: TextView = itemView.findViewById(R.id.name)
        private var idType: TextView = itemView.findViewById(R.id.id_type)
        private var birthdate: TextView = itemView.findViewById(R.id.birthdate)

        fun bindItem(petType: MutableList<PetType>, customerPet: CustomerPet, listener: (CustomerPet) -> Unit){
            for (type in petType){
                if (type.id.equals(customerPet.id_type)){
                    idType.text = type.name.toString()
                }
            }
            name.text = customerPet.name
            birthdate.text = customerPet.birthdate

            containerView.setOnClickListener {
                listener(customerPet)
            }
        }
    }

}