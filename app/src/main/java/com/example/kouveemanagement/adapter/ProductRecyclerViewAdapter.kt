package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer

class ProductRecyclerViewAdapter (private val products : MutableList<Product>, private val listener: (Product) -> Unit) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(products[position],listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var base_url: String = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"

        private var photo = itemView.findViewById<ImageView>(R.id.photo)
        private var name = itemView.findViewById<TextView>(R.id.name)
        private var price = itemView.findViewById<TextView>(R.id.price)
        private var stock = itemView.findViewById<TextView>(R.id.stock)

        fun bindItem(product: Product, listener: (Product) -> Unit){

            product.photo.let { Picasso.get().load(base_url+it).fit().into(photo) }
            name.text = product.name
            price.text = product.price.toString()
            stock.text = product.stock.toString()

            containerView.setOnClickListener {
                listener(product)
            }
        }
    }
}