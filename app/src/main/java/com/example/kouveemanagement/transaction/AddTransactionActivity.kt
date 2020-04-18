package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailTransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_transaction.*
import org.jetbrains.anko.startActivity

class AddTransactionActivity : AppCompatActivity(), TransactionView, DetailProductTransactionView, DetailServiceTransactionView {

    private var state: String = ""
    private lateinit var type: String

    private lateinit var lastEmp: String

    private lateinit var presenter: TransactionPresenter
    private lateinit var transaction: Transaction
    private lateinit var detailProductPresenter: DetailProductTransactionPresenter
    private var detailProducts: MutableList<DetailProductTransaction> = mutableListOf()
    private lateinit var detailServicePresenter: DetailServiceTransactionPresenter
    private var detailServices: MutableList<DetailServiceTransaction> = mutableListOf()

    companion object{
        lateinit var idTransaction: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        transaction = ServiceTransactionActivity.transaction
        idTransaction = transaction.id.toString()
        type = intent?.getStringExtra("type").toString()
        Toast.makeText(this, "TYPE : $type", Toast.LENGTH_LONG).show()
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
        btn_status.setOnClickListener {
            state = "status"
            val newTransaction = Transaction(last_cs = lastEmp)
            presenter.editStatusTransaction(idTransaction, newTransaction)
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setData(){
        id.text = transaction.id
        id_customer_pet.text = transaction.id_customer_pet
        if (type == "service"){
            status.text = transaction.status
        }else{
            status.visibility = View.GONE
            btn_status.visibility = View.GONE
        }
        val price = transaction.total_price.toString()
        total_price.text = CustomFun.changeToRp(price.toDouble())
    }

    override fun showTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun transactionSuccess(data: TransactionResponse?) {
        transaction = data?.transactions?.get(0)!!
        status.text = transaction.status
        id_customer_pet.text = transaction.id_customer_pet
        val price = transaction.total_price.toString()
        total_price.text = CustomFun.changeToRp(price.toDouble())
        when(state){
            "edit" -> {
                Toast.makeText(this, "Success Edit Transaction", Toast.LENGTH_SHORT).show()
            }
            "cancel" -> {
                Toast.makeText(this, "Success Cancel Transaction", Toast.LENGTH_SHORT).show()
                startActivity<ServiceTransactionActivity>()
            }
            "status" -> {
                Toast.makeText(this, "Success Update Status Transaction", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Success Transaction", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun transactionFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
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
            }, ProductTransactionActivity.products, mutableListOf(), {}, mutableListOf())
        }
    }

    override fun detailProductTransactionFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
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
            }, ServiceTransactionActivity.services)
        }
    }

    override fun detailServiceTransactionFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }
}
