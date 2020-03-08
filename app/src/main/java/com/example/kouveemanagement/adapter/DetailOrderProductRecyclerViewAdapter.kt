package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import kotlinx.android.extensions.LayoutContainer

class DetailOrderProductRecyclerViewAdapter(private val detailOrderProduct: MutableList<DetailOrderProduct>, private val listener: (DetailOrderProduct) -> Unit) : RecyclerView.Adapter<DetailOrderProductRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_order_product, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = detailOrderProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(detailOrderProduct[position], listener)
    }

    class ViewHolder(override val containerView : View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private var id = itemView.findViewById<TextView>(R.id.id)
        private var quantity = itemView.findViewById<TextView>(R.id.quantity)
        private var subtotal = itemView.findViewById<TextView>(R.id.subtotal)

        fun bindItem(detailOrderProduct: DetailOrderProduct, listener: (DetailOrderProduct) -> Unit){
            id.text = detailOrderProduct.id_product
            quantity.text = detailOrderProduct.quantity.toString()
            subtotal.text = detailOrderProduct.subtotal.toString()

            containerView.setOnClickListener {
                listener(detailOrderProduct)
            }
        }

    }

}