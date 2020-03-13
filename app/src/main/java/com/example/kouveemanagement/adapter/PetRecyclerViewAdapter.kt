package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetType
import kotlinx.android.extensions.LayoutContainer

class PetRecyclerViewAdapter(private val id: String, private val type: MutableList<PetType>, private val listenerT: (PetType) -> Unit, private val size: MutableList<PetSize>, private val listenerS: (PetSize) -> Unit) : RecyclerView.Adapter<PetRecyclerViewAdapter.ViewHolder>()  {

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
            return size.size
        }
        return type.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (id == "size")
            holder.bindSize(size[position], listenerS)
        else
            holder.bindType(type[position], listenerT)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var name: TextView = itemView.findViewById(R.id.name)

        fun bindSize(petSize: PetSize, listener: (PetSize) -> Unit) {
            name.text = petSize.name

            containerView.setOnClickListener {
                listener(petSize)
            }
        }

        fun bindType(petType: PetType, listener: (PetType) -> Unit) {
            name.text = petType.name

            containerView.setOnClickListener {
                listener(petType)
            }

        }

    }

}