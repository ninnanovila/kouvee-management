package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.Transaction
import com.example.kouveemanagement.transaction.ProductTransactionActivity
import com.example.kouveemanagement.transaction.ServiceTransactionActivity
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class TransactionRecyclerViewAdapter(private val type: String, private val customerPets: MutableList<CustomerPet>, private val transactions: MutableList<Transaction>, private val listener: (Transaction) -> Unit) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(viewHolder)    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(customerPets, transactions[position], listener)
    }

    fun filterTransaction(input: String){
        if (type == "service"){
            ServiceTransactionActivity.transactions.clear()
            if (input.isEmpty()){
                ServiceTransactionActivity.transactions.addAll(transactions)
            }else{
                var i = 0
                while (i < transactions.size){
                    if (transactions[i].id?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                        ServiceTransactionActivity.transactions.add(transactions[i])
                    }
                    i++
                }
            }
            notifyDataSetChanged()
        }else if (type == "product"){
            ProductTransactionActivity.transactions.clear()
            if (input.isEmpty()){
                ProductTransactionActivity.transactions.addAll(transactions)
            }else{
                var i = 0
                while (i < transactions.size){
                    if (transactions[i].id?.toLowerCase(Locale.getDefault())?.contains(input)!!){
                        ProductTransactionActivity.transactions.add(transactions[i])
                    }
                    i++
                }
            }
            notifyDataSetChanged()
        }
    }

    class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        private var id: TextView = itemView.findViewById(R.id.id)
        private var discount: TextView = itemView.findViewById(R.id.discount)
        private var totalPrice: TextView = itemView.findViewById(R.id.total_price)
        private var payment: TextView = itemView.findViewById(R.id.payment)
        private var idPet: TextView = itemView.findViewById(R.id.id_customer_pet)

        fun bindItem(pets: MutableList<CustomerPet>, transaction: Transaction, listener: (Transaction) -> Unit){
            for (pet in pets){
                if (pet.id == transaction.id_customer_pet){
                    idPet.text = pet.name.toString()
                }else if (transaction.id_customer_pet == "1"){
                    idPet.text = "Guest"
                }
            }
            id.text = transaction.id
            val discountTotal = transaction.discount.toString()
            discount.text = "( " + CustomFun.changeToRp(discountTotal.toDouble()) + " )"
            val priceTotal = transaction.total_price.toString()
            totalPrice.text = CustomFun.changeToRp(priceTotal.toDouble())
            val paidOff = "Paid Off"
            val notYetPaidOff = "Not Yet Paid Off"
            if (transaction.payment.equals("1")){
                payment.text = paidOff
            }else{
                payment.text = notYetPaidOff
            }
            containerView.setOnClickListener {
                listener(transaction)
            }
        }
    }

}