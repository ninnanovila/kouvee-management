package com.example.kouveemanagement.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailTransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_show_transaction.*
import org.jetbrains.anko.startActivity

class ShowTransactionActivity : AppCompatActivity(), TransactionView, DetailProductTransactionView, DetailServiceTransactionView {

    private lateinit var presenter: TransactionPresenter
    private lateinit var transaction: Transaction
    private lateinit var detailProductPresenter: DetailProductTransactionPresenter
    private var detailProducts: MutableList<DetailProductTransaction> = mutableListOf()
    private lateinit var detailServicePresenter: DetailServiceTransactionPresenter
    private var detailServices: MutableList<DetailServiceTransaction> = mutableListOf()
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_transaction)
        type = intent?.getStringExtra("type").toString()
        if(type == "service"){
            transaction = ServiceTransactionActivity.transaction
        }else if (type == "product"){
            transaction = ProductTransactionActivity.transaction
        }
        val idTransaction = transaction.id.toString()
        presenter = TransactionPresenter(this, Repository())
        presenter.editTotalTransaction(idTransaction)
        if (type == "service"){
            detailServicePresenter = DetailServiceTransactionPresenter(this, Repository())
            detailServicePresenter.getDetailServiceTransactionByIdTransaction(idTransaction)
        }else if (type == "product"){
            detailProductPresenter = DetailProductTransactionPresenter(this, Repository())
            detailProductPresenter.getDetailProductTransactionByIdTransaction(idTransaction)
        }
        setData(type)
        btn_home.setOnClickListener{
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setData(input: String){
        created_at.text = transaction.created_at
        if (transaction.updated_at == null){
            updated_at.text = "-"
        }else{
            updated_at.text = transaction.updated_at
        }
        if (transaction.last_cr == null){
            last_cr.text = "-"
        }else{
            last_cr.text = transaction.last_cr
        }
        last_cs.text = transaction.last_cs
        id.text = transaction.id
        id_customer_pet.text = petName(transaction.id_customer_pet.toString())
        if (input == "service"){
            status.text = transaction.status
        }else{
            status.text = "-"
        }
        discount.text = CustomFun.changeToRp(transaction.discount.toString().toDouble())
        total_price.text = CustomFun.changeToRp(transaction.total_price.toString().toDouble())
        if (transaction.payment == "0"){
            payment.text = getString(R.string.not_yet_paid_off)
        }else{
            payment.text = getString(R.string.paid_off)
        }
    }

    override fun showTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun transactionSuccess(data: TransactionResponse?) {
        transaction = data?.transactions?.get(0)!!
        CustomFun.successSnackBar(container, baseContext, "Ok, success")
    }

    override fun transactionFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    override fun showDetailProductTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailProductTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        val temp: List<DetailProductTransaction> = data?.detailProductTransactions ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
        }else{
            detailProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("product", detailProducts.asReversed(), {
                Toast.makeText(this, it.id_transaction, Toast.LENGTH_LONG).show()
            }, ProductTransactionActivity.products, mutableListOf(), {}, mutableListOf())
        }
    }

    override fun detailProductTransactionFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    override fun showDetailServiceTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailServiceTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        val temp: List<DetailServiceTransaction> = data?.detailServiceTransactions ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
        }else{
            detailServices.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("service", mutableListOf(), {}, mutableListOf(), detailServices.asReversed(), {
                Toast.makeText(this, it.id_transaction, Toast.LENGTH_LONG).show()
            }, ServiceTransactionActivity.services)
        }
    }

    override fun detailServiceTransactionFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun petName(id: String):String{
        val temp: MutableList<CustomerPet> = if (type == "product"){
            ProductTransactionActivity.customersPet
        }else {
            ServiceTransactionActivity.customersPet
        }

        for (pet in temp){
            if (pet.id.equals(id)){
                return pet.name.toString()
            }
        }
        return "Guest"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (type == "product"){
            startActivity<ProductTransactionActivity>()
        }else if (type == "service"){
            startActivity<ServiceTransactionActivity>()
        }
    }


}
