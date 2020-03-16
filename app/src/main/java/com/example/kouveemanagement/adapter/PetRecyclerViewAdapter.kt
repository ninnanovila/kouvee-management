package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetType
import com.example.kouveemanagement.pet.PetManagementActivity
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class PetRecyclerViewAdapter(private val id: String, private val petTypes: MutableList<PetType>, private val listenerT: (PetType) -> Unit, private val petSizes: MutableList<PetSize>, private val listenerS: (PetSize) -> Unit) : RecyclerView.Adapter<PetRecyclerViewAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        if (id == "size"){
            return petSizes.size
        }
        return petTypes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (id == "size")
            holder.bindSize(position+1, petSizes[position], listenerS)
        else
            holder.bindType(position+1, petTypes[position], listenerT)
    }

    fun filterPet(input: String){
        if (id == "size"){
            PetManagementActivity.petSizes.clear()
            if (input.isEmpty()){
                PetManagementActivity.petSizes.addAll(petSizes)
            }else{
                var i = 0
                while (i < petSizes.size){
                    if (petSizes[i].name?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                        PetManagementActivity.petSizes.add(petSizes[i])
                    }
                    i++
                }
            }
        }else if (id == "type"){
            PetManagementActivity.petTypes.clear()
            if (input.isEmpty()){
                PetManagementActivity.petTypes.addAll(petTypes)
            }else{
                var i = 0
                while (i < petTypes.size){
                    if (petTypes[i].name?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                        PetManagementActivity.petTypes.add(petTypes[i])
                    }
                    i++
                }
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        private var number: TextView = itemView.findViewById(R.id.number)
        private var name: TextView = itemView.findViewById(R.id.name)

        fun bindSize(iS: Int, petSize: PetSize, listener: (PetSize) -> Unit) {
            number.text = iS.toString()
            name.text = petSize.name
            containerView.setOnClickListener {
                listener(petSize)
            }
        }

        fun bindType(iT: Int, petType: PetType, listener: (PetType) -> Unit) {
            number.text = iT.toString()
            name.text = petType.name
            containerView.setOnClickListener {
                listener(petType)
            }

        }

    }

}