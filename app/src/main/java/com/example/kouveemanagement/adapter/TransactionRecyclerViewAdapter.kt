package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Transaction
import kotlinx.android.extensions.LayoutContainer

class TransactionRecyclerViewAdapter(private val transactions: MutableList<Transaction>, private val listener: (Transaction) -> Unit) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(viewHolder)    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(transactions[position], listener)
    }

    class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var id: TextView = itemView.findViewById(R.id.id)
        private var discount: TextView = itemView.findViewById(R.id.discount)
        private var total_price: TextView = itemView.findViewById(R.id.total_price)
        private var payment: TextView = itemView.findViewById(R.id.payment)

        fun bindItem(transaction: Transaction, listener: (Transaction) -> Unit){
            id.text = transaction.id
            discount.text = transaction.discount
            total_price.text = transaction.total_price.toString()
            payment.text = transaction.payment
        }
    }

}