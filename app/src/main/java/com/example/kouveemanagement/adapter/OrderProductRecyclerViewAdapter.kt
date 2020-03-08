package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.OrderProduct
import kotlinx.android.extensions.LayoutContainer

class OrderProductRecyclerViewAdapter (private val orderProducts : MutableList<OrderProduct>, private val listener: (OrderProduct) -> Unit) : RecyclerView.Adapter<OrderProductRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_product, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = orderProducts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(orderProducts[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var id = itemView.findViewById<TextView>(R.id.id)
        private var status = itemView.findViewById<TextView>(R.id.status)
        private var total = itemView.findViewById<TextView>(R.id.total)

        fun bindItem(orderProduct: OrderProduct, listener: (OrderProduct) -> Unit){
            id.text = orderProduct.id
            status.text = orderProduct.status
            total.text = orderProduct.total.toString()

            containerView.setOnClickListener {
                listener(orderProduct)
            }
        }
    }

}