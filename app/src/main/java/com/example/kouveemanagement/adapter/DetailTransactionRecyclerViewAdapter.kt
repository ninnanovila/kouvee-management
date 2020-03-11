package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailProductTransaction
import com.example.kouveemanagement.model.DetailServiceTransaction
import kotlinx.android.extensions.LayoutContainer

class DetailTransactionRecyclerViewAdapter(private val id: String, private val detailProducts: MutableList<DetailProductTransaction>, private val listenerP: (DetailProductTransaction) -> Unit, private val detailServices: MutableList<DetailServiceTransaction>, private val listenerS: (DetailServiceTransaction) -> Unit) : RecyclerView.Adapter<DetailTransactionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_transaction, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        if (id == "service"){
            return detailServices.size
        }
        return  detailProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (id == "service"){
            holder.bindService(detailServices[position], listenerS)
        }else{
            holder.bindProduct(detailProducts[position], listenerP)
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var id: TextView = itemView.findViewById(R.id.id)
        private var quantity: TextView = itemView.findViewById(R.id.quantity)
        private var subtotal: TextView = itemView.findViewById(R.id.subtotal)

        fun bindProduct(detailProductTransaction: DetailProductTransaction, listener: (DetailProductTransaction) -> Unit){
            id.text = detailProductTransaction.id_product
            quantity.text = detailProductTransaction.quantity.toString()
            subtotal.text = detailProductTransaction.subtotal_price.toString()
            containerView.setOnClickListener {
                listener(detailProductTransaction)
            }
        }

        fun bindService(detailServiceTransaction: DetailServiceTransaction, listener: (DetailServiceTransaction) -> Unit){
            id.text = detailServiceTransaction.id_service
            quantity.text = detailServiceTransaction.quantity.toString()
            subtotal.text = detailServiceTransaction.subtotal_price.toString()
            containerView.setOnClickListener {
                listener(detailServiceTransaction)
            }
        }

    }


}