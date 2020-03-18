package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.Product
import kotlinx.android.extensions.LayoutContainer

class DetailOrderProductRecyclerViewAdapter(private val products: MutableList<Product>, private val detailOrderProduct: MutableList<DetailOrderProduct>, private val listener: (DetailOrderProduct) -> Unit) : RecyclerView.Adapter<DetailOrderProductRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_order_product, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = detailOrderProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(products,detailOrderProduct[position], listener)
    }

    class ViewHolder(override val containerView : View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private var id = itemView.findViewById<TextView>(R.id.id)
        private var quantity = itemView.findViewById<TextView>(R.id.quantity)
        private var subtotal = itemView.findViewById<TextView>(R.id.subtotal)

        fun bindItem(products: MutableList<Product>, detailOrderProduct: DetailOrderProduct, listener: (DetailOrderProduct) -> Unit){
            for (product in products){
                if (detailOrderProduct.id_product.equals(product.id)){
                    id.text = product.name
                }
            }
            quantity.text = detailOrderProduct.quantity.toString()
            val total = detailOrderProduct.subtotal.toString()
            val rp = "Rp. $total"
            subtotal.text = rp

            containerView.setOnClickListener {
                listener(detailOrderProduct)
            }
        }

    }

}