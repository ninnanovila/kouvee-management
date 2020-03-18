package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Transaction
import com.example.kouveemanagement.transaction.TransactionActivity
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class TransactionRecyclerViewAdapter(private val transactions: MutableList<Transaction>, private val listener: (Transaction) -> Unit) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(viewHolder)    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(transactions[position], listener)
    }

    fun filterTransaction(input: String){
        TransactionActivity.transactions.clear()
        if (input.isEmpty()){
            TransactionActivity.transactions.addAll(transactions)
        }else{
            var i = 0
            while (i < transactions.size){
                if (transactions[i].id?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                    TransactionActivity.transactions.add(transactions[i])
                }
                i++
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var id: TextView = itemView.findViewById(R.id.id)
        private var discount: TextView = itemView.findViewById(R.id.discount)
        private var totalPrice: TextView = itemView.findViewById(R.id.total_price)
        private var payment: TextView = itemView.findViewById(R.id.payment)

        fun bindItem(transaction: Transaction, listener: (Transaction) -> Unit){
            id.text = transaction.id
            val discountTotal = transaction.discount.toString()
            val rpD = "(Rp. +$discountTotal)"
            discount.text = rpD
            val priceTotal = transaction.total_price.toString()
            val rpT = "Rp. +$priceTotal"
            totalPrice.text = rpT
            if (transaction.payment.equals("1")){
                payment.text = R.string.paid_off.toString()
            }else{
                payment.text = R.string.not_yet_paid_off.toString()
            }
            containerView.setOnClickListener {
                listener(transaction)
            }
        }
    }

}