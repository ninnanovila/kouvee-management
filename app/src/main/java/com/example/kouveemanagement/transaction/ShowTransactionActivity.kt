package com.example.kouveemanagement.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailTransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_show_transaction.*

class ShowTransactionActivity : AppCompatActivity(), ProductView, ServiceView, TransactionView, DetailProductTransactionView, DetailServiceTransactionView {

    private lateinit var presenterP: ProductPresenter
    private var products: MutableList<Product> = mutableListOf()
    private lateinit var presenterS: ServicePresenter
    private var services: MutableList<Service> = mutableListOf()

    private lateinit var presenter: TransactionPresenter
    private lateinit var transaction: Transaction
    private lateinit var detailProductPresenter: DetailProductTransactionPresenter
    private var detailProducts: MutableList<DetailProductTransaction> = mutableListOf()
    private lateinit var detailServicePresenter: DetailServiceTransactionPresenter
    private var detailServices: MutableList<DetailServiceTransaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_transaction)
        transaction = TransactionActivity.transaction
        val idTransaction = transaction.id.toString()
        val type = intent?.getStringExtra("type").toString()
        Toast.makeText(this, "TYPE : $type", Toast.LENGTH_LONG).show()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        presenterS = ServicePresenter(this, Repository())
        presenterS.getAllService()
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
    }

    private fun setData(input: String){
        id.text = transaction.id
        id_customer_pet.text = transaction.id_customer_pet
        if (input == "service"){
            status.text = transaction.status
        }else{
            status.visibility = View.GONE
        }
        total_price.text = transaction.total_price.toString()
    }

    override fun showProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            products.addAll(temp)
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed Product", Toast.LENGTH_SHORT).show()
    }

    override fun showServiceLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideServiceLoading() {
        progressbar.visibility = View.GONE
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            services.addAll(temp)
        }
    }

    override fun serviceFailed() {
        Toast.makeText(this, "Failed Service", Toast.LENGTH_SHORT).show()
    }

    override fun showTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun transactionSuccess(data: TransactionResponse?) {
        transaction = data?.transactions?.get(0)!!
    }

    override fun transactionFailed() {
        Toast.makeText(this, "Failed Transaction", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            detailProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("product", detailProducts, {
                Toast.makeText(this, it.id_transaction, Toast.LENGTH_LONG).show()
            }, products, mutableListOf(), {}, mutableListOf())
        }
    }

    override fun detailProductTransactionFailed() {
        Toast.makeText(this, "Failed Detail Product Transaction", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            detailServices.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("service", mutableListOf(), {}, mutableListOf(), detailServices, {
                Toast.makeText(this, it.id_transaction, Toast.LENGTH_LONG).show()
            }, services)
        }
    }

    override fun detailServiceTransactionFailed() {
        Toast.makeText(this, "Failed Detail Service Transaction", Toast.LENGTH_SHORT).show()
    }


}
