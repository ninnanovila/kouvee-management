package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.OrderProduct
import com.example.kouveemanagement.orderproduct.OrderProductActivity
import kotlinx.android.extensions.LayoutContainer
import java.util.*

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

    fun filterOrderProduct(input: String){
        OrderProductActivity.orderProducts.clear()
        if (input.isEmpty()){
            OrderProductActivity.orderProducts.addAll(orderProducts)
        }else{
            var i = 0
            while (i < orderProducts.size){
                if (orderProducts[i].id?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                    OrderProductActivity.orderProducts.add(orderProducts[i])
                }
                i++
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var id = itemView.findViewById<TextView>(R.id.id)
        private var status = itemView.findViewById<TextView>(R.id.status)
        private var total = itemView.findViewById<TextView>(R.id.total)

        fun bindItem(orderProduct: OrderProduct, listener: (OrderProduct) -> Unit){
            id.text = orderProduct.id
            status.text = orderProduct.status
            val priceTotal = orderProduct.total.toString()
            val rp = "Rp. $priceTotal"
            total.text = rp

            containerView.setOnClickListener {
                listener(orderProduct)
            }
        }
    }

}