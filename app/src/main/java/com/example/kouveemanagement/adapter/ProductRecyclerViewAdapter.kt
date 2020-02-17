package com.example.kouveemanagement.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.model.Product
import kotlinx.android.extensions.LayoutContainer

class ProductRecyclerViewAdapter (private val products : List<Product>, private val listener: (Product) -> Unit) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    }
})