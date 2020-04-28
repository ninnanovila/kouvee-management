package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.*
import kotlinx.android.extensions.LayoutContainer

class DetailTransactionRecyclerViewAdapter(private val id: String,
                                           private val detailProducts: MutableList<DetailProductTransaction>, private val listenerP: (DetailProductTransaction) -> Unit, private val products: MutableList<Product>,
                                           private val detailServices: MutableList<DetailServiceTransaction>, private val listenerS: (DetailServiceTransaction) -> Unit, private val services: MutableList<Service>, private val petSizes: MutableList<PetSize>)
    : RecyclerView.Adapter<DetailTransactionRecyclerViewAdapter.ViewHolder>() {

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
            holder.bindService(detailServices[position], listenerS, services, petSizes)
        }else{
            holder.bindProduct(detailProducts[position], listenerP, products)
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var id: TextView = itemView.findViewById(R.id.id)
        private var quantity: TextView = itemView.findViewById(R.id.quantity)
        private var subtotal: TextView = itemView.findViewById(R.id.subtotal)

        fun bindProduct(detailProductTransaction: DetailProductTransaction, listener: (DetailProductTransaction) -> Unit, products: MutableList<Product>){
            var unit = ""
            for (product in products){
                if (detailProductTransaction.id_product.equals(product.id)){
                    id.text = product.name
                    unit = product.unit.toString()
                }
            }
            quantity.text = detailProductTransaction.quantity.toString()+ " " + unit
            val total = detailProductTransaction.subtotal_price.toString()
            subtotal.text = CustomFun.changeToRp(total.toDouble())
            containerView.setOnClickListener {
                listener(detailProductTransaction)
            }
        }

        fun bindService(detailServiceTransaction: DetailServiceTransaction, listener: (DetailServiceTransaction) -> Unit, services: MutableList<Service>, petSizes: MutableList<PetSize>){
            var sizeFirst = "-1"
            var serviceFirst = ""
            for (service in services){
                if (detailServiceTransaction.id_service.equals(service.id)){
                    serviceFirst = service.name.toString()
                    sizeFirst = service.id_size.toString()
                }
            }
            for (size in petSizes){
                if (size.id == sizeFirst){
                    sizeFirst = size.name.toString()
                }
            }
            id.text = serviceFirst + " " + sizeFirst
            quantity.text = detailServiceTransaction.quantity.toString()
            val total = detailServiceTransaction.subtotal_price.toString()
            subtotal.text = CustomFun.changeToRp(total.toDouble())
            containerView.setOnClickListener {
                listener(detailServiceTransaction)
            }
        }

    }


}