package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailTransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_transaction.*
import org.jetbrains.anko.startActivity

class AddTransactionActivity : AppCompatActivity(), ProductView, ServiceView, TransactionView, DetailProductTransactionView, DetailServiceTransactionView {

    private var state: String = ""
    private lateinit var type: String

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

    companion object{
        lateinit var idTransaction: String
        var nameProductDropdown: MutableList<String> = arrayListOf()
        var idProductDropdown: MutableList<String> = arrayListOf()
        var nameServiceDropdown: MutableList<String> = arrayListOf()
        var idServiceDropdown: MutableList<String> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        transaction = TransactionActivity.transaction
        idTransaction = transaction.id.toString()
        type = intent?.getStringExtra("type").toString()
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
        setData()
        fab_add.setOnClickListener {
            if (type == "service"){
                val fragment: Fragment = AddDetailServiceTransactionFragment.newInstance()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }else if (type == "product"){
                val fragment: Fragment = AddDetailProductTransactionFragment.newInstance()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }
        }
        btn_cancel.setOnClickListener {
            state = "cancel"
            presenter.cancelTransaction(idTransaction)
        }
        btn_edit.setOnClickListener {
            state = "edit"
            Toast.makeText(this, "Edit Transaction ?", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(){
        id.setText(transaction.id)
        id_customer_pet.setText(transaction.id_customer_pet)
        if (type == "service"){
            status.setText(transaction.status)
        }else{
            status.visibility = View.GONE
        }
        total_price.setText(transaction.total_price.toString())
    }

    override fun showProductLoading() {
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        products.addAll(temp)
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            if (nameProductDropdown.isNotEmpty()){
                nameProductDropdown.clear()
                idProductDropdown.clear()
                for (i in temp.indices){
                    nameProductDropdown.add(i, temp[i].name.toString())
                    idProductDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    nameProductDropdown.add(i, temp[i].name.toString())
                    idProductDropdown.add(i, temp[i].id.toString())
                }
            }
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed Product", Toast.LENGTH_SHORT).show()
    }

    override fun showServiceLoading() {
    }

    override fun hideServiceLoading() {
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        services.addAll(temp)
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            if (nameServiceDropdown.isNotEmpty()){
                nameServiceDropdown.clear()
                idServiceDropdown.clear()
                for (i in temp.indices){
                    nameServiceDropdown.add(i, temp[i].name.toString())
                    idServiceDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    nameServiceDropdown.add(i, temp[i].name.toString())
                    idServiceDropdown.add(i, temp[i].id.toString())
                }
            }
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
        id_customer_pet.setText(transaction.id_customer_pet)
        total_price.setText(transaction.total_price.toString())
        when(state){
            "edit" -> {
                Toast.makeText(this, "Success Edit Transaction", Toast.LENGTH_SHORT).show()
            }
            "cancel" -> {
                Toast.makeText(this, "Success Cancel Transaction", Toast.LENGTH_SHORT).show()
                startActivity<TransactionActivity>()
            }
            else -> {
                Toast.makeText(this, "Success Transaction", Toast.LENGTH_SHORT).show()
            }
        }
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
            for (i in temp.indices){
                detailProducts.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("product", detailProducts, {
                val fragment = EditDetailProductTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
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
            for (i in temp.indices){
                detailServices.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("service", mutableListOf(), {}, mutableListOf(), detailServices, {
                val fragment = EditDetailServiceTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
                Toast.makeText(this, it.id_transaction, Toast.LENGTH_LONG).show()
            }, services)
        }
    }

    override fun detailServiceTransactionFailed() {
        Toast.makeText(this, "Failed Detail Service Transaction", Toast.LENGTH_SHORT).show()
    }
}
