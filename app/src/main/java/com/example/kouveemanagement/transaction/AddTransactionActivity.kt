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
        type = intent?.getStringExtra("type").toString()
        if(type == "service"){
            transaction = ServiceTransactionActivity.transaction
        }else if (type == "product"){
            transaction = ProductTransactionActivity.transaction
        }
        idTransaction = transaction.id.toString()
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
        if (type == "service"){
            status.text = transaction.status
        }else{
            status.visibility = View.GONE
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
        setData()
        when(state){
            "edit" -> {
                CustomFun.successSnackBar(container, baseContext, "Success edit")
            }
            "cancel" -> {
                if (type == "service"){
                    startActivity<ServiceTransactionActivity>()
                }else if (type == "product"){
                    startActivity<ProductTransactionActivity>()
                }
            }
            "status" -> {
                CustomFun.successSnackBar(container, baseContext, "Success update status")
            }
            else -> {
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }
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
            CustomFun.warningSnackBar(container, baseContext, "Empty detail")
        }else{
            for (detail in temp){
                detailProducts.add(detail)
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("product", detailProducts, {
                val fragment = EditDetailProductTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
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
            CustomFun.warningSnackBar(container, baseContext, "Empty detail")
        }else{
            for (detail in temp){
                detailServices.add(detail)
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("service", mutableListOf(), {}, mutableListOf(), detailServices, {
                val fragment = EditDetailServiceTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
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
