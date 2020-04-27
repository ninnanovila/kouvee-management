package com.example.kouveemanagement.transaction

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        var idOfSize = "-1"
        lateinit var idTransaction: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        type = intent?.getStringExtra("type").toString()
        if (ServiceTransactionActivity.idOfSize != "-1") idOfSize = ServiceTransactionActivity.idOfSize
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
            showAlertCancel()
        }
        btn_edit.setOnClickListener {
            state = "edit"
            Toast.makeText(this, "Edit Transaction ?", Toast.LENGTH_LONG).show()
        }
        btn_status.setOnClickListener {
            state = "status"
            if (type == "product"){
                CustomFun.warningSnackBar(container, baseContext, "Feature for service transaction")
            }else{
                showAlertChangeStatus()
            }
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        swipe_rv.setOnRefreshListener {
            if (type == "service"){
                detailServicePresenter = DetailServiceTransactionPresenter(this, Repository())
                detailServicePresenter.getDetailServiceTransactionByIdTransaction(idTransaction)
            }else if (type == "product"){
                detailProductPresenter = DetailProductTransactionPresenter(this, Repository())
                detailProductPresenter.getDetailProductTransactionByIdTransaction(idTransaction)
            }
        }
        CustomFun.setSwipe(swipe_rv)
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
        swipe_rv.isRefreshing = true
    }

    override fun hideTransactionLoading() {
        swipe_rv.isRefreshing = false
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
        swipe_rv.isRefreshing = true
    }

    override fun hideDetailProductTransactionLoading() {
        swipe_rv.isRefreshing = false
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
        swipe_rv.isRefreshing = true
    }

    override fun hideDetailServiceTransactionLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        val temp: List<DetailServiceTransaction> = data?.detailServiceTransactions ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty detail")
        }else{
            for (detail in temp){
                detailServices.add(detail)
                setIdOfSize(detail.id_service.toString())
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

    private fun setIdOfSize(id: String){
        lateinit var temp: Service
        for (service in ServiceTransactionActivity.services){
            if (service.id == id){
                temp = service
            }
        }
        for(size in ServiceTransactionActivity.petSizes){
            if (size.id == temp.id_size.toString()){
                idOfSize = size.id.toString()
            }
        }
    }

    private fun showAlertCancel(){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to $state this transaction ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                presenter.cancelTransaction(idTransaction)
            }
            .setNegativeButton("NO"){ dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                CustomFun.warningSnackBar(container, baseContext, "Process canceled")
            }
            .setCancelable(false)
            .show()
    }

    private fun showAlertChangeStatus(){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to change the status $state of this transaction ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                val newTransaction = Transaction(last_cs = lastEmp)
                presenter.editStatusTransaction(idTransaction, newTransaction)
            }
            .setNegativeButton("NO"){ dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                CustomFun.warningSnackBar(container, baseContext, "Process canceled")
            }
            .setCancelable(false)
            .show()
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
