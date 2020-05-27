package com.example.kouveemanagement.transaction

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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
    private lateinit var petId: String

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var lastEmp: String

    private lateinit var presenter: TransactionPresenter
    private lateinit var transaction: Transaction
    private lateinit var detailProductPresenter: DetailProductTransactionPresenter
    private var detailProducts: MutableList<DetailProductTransaction> = mutableListOf()
    private lateinit var detailServicePresenter: DetailServiceTransactionPresenter
    private var detailServices: MutableList<DetailServiceTransaction> = mutableListOf()

    companion object{
        var idOfSize = ServiceTransactionActivity.idOfSize
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
            if (transaction.id_customer_pet == "1"){
                CustomFun.warningSnackBar(container, baseContext, "Edit pet, feature for customer")
            }else{
                chooseCustomerPet()
            }
        }
        btn_status.setOnClickListener {
            state = "status"
            if (type == "product"){
                CustomFun.warningSnackBar(container, baseContext, "Change statue, feature for service transaction")
            }else{
                if (transaction.status == "Done"){
                    CustomFun.warningSnackBar(container, baseContext, "Transaction done, needs payment")
                }else{
                    showAlertChangeStatus()
                }
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
        btn_log.setOnClickListener {
            showLog(transaction)
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun setData(){
        id.text = transaction.id
        petId = transaction.id_customer_pet.toString()
        id_customer_pet.text = petName(transaction.id_customer_pet.toString())
        if (type == "service"){
            status.text = transaction.status
        }else{
            status.text = "-"
        }
        if (transaction.discount.isNullOrEmpty()){
            discount.text = CustomFun.changeToRp(0.0)
        }else{
            discount.text = CustomFun.changeToRp(transaction.discount.toString().toDouble())
        }
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
            detailProducts.clear()
            for (detail in temp){
                detailProducts.add(detail)
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("product", detailProducts.asReversed(), {
                val fragment = EditDetailProductTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }, ProductTransactionActivity.products, mutableListOf(), {}, mutableListOf(), mutableListOf())
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
            detailServices.clear()
            for (detail in temp){
                detailServices.add(detail)
                setIdOfSize(detail.id_service.toString())
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailTransactionRecyclerViewAdapter("service", mutableListOf(), {}, mutableListOf(), detailServices.asReversed(), {
                val fragment = EditDetailServiceTransactionFragment.newInstance(it)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }, ServiceTransactionActivity.services, ServiceTransactionActivity.petSizes)
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
            .setIcon(R.drawable.delete)
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
            .setIcon(R.drawable.update)
            .setTitle("Confirmation")
            .setMessage("Are you sure to change the status of this transaction ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                val newTransaction = Transaction(id = idTransaction, last_cs = lastEmp)
                presenter.editStatusTransaction(idTransaction, newTransaction)
            }
            .setNegativeButton("NO"){ dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                CustomFun.warningSnackBar(container, baseContext, "Process canceled")
            }
            .setCancelable(false)
            .show()
    }

    private fun chooseCustomerPet() {
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_pet, null)
        var name: MutableList<String> = mutableListOf()
        var id: MutableList<String> = mutableListOf()
        Toast.makeText(this, "Type : $type", Toast.LENGTH_LONG).show()
        if (type == "product"){
            name = ProductTransactionActivity.customerPetName
            id = ProductTransactionActivity.customerPetId
        }else if (type == "service"){
            name = ServiceTransactionActivity.customerPetName
            id = ServiceTransactionActivity.customerPetId
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, name)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        dropdown.setText(petName(transaction.id_customer_pet.toString()))
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            petId = id[position]
            checkCustomer(petId)
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnAdd.text = getString(R.string.save)

        btnAdd.setOnClickListener {
            if (petId == transaction.id_customer_pet){
                CustomFun.failedSnackBar(container, baseContext, "Pet same as before")
            }else{
                transaction = Transaction(id = transaction.id, id_customer_pet = petId)
                presenter.editPetTransaction(transaction.id.toString(), transaction)
                infoDialog.dismiss()
            }
        }
    }

    private fun checkCustomer(petId: String){
        var customers: MutableList<Customer> = mutableListOf()
        var pets: MutableList<CustomerPet> = mutableListOf()
        if (type == "product"){
            customers = ProductTransactionActivity.customers
            pets = ProductTransactionActivity.customersPet
        }else if (type == "service"){
            customers = ServiceTransactionActivity.customers
            pets = ServiceTransactionActivity.customersPet
        }
        var idCustomer = "-1"
        for(customerPet in pets){
            if (customerPet.id.equals(petId)){
                idCustomer = customerPet.id_customer.toString()
            }
        }
        for (customer in customers){
            if (customer.id.equals(idCustomer)){
                Toast.makeText(this, "Customer : ${customer.name}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (type == "product"){
            startActivity<ProductTransactionActivity>()
        }else if (type == "service"){
            startActivity<ServiceTransactionActivity>()
        }
    }

    private fun showLog(input: Transaction){
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_show_log_transaction, null)

        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val lastCs = dialog.findViewById<TextView>(R.id.last_cs)
        val lastCr = dialog.findViewById<TextView>(R.id.last_cashier)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        createdAt.text = input.created_at
        if (input.updated_at == null){
            updatedAt.text = "-"
        }else{
            updatedAt.text = input.updated_at
        }
        if (input.last_cr == null){
            lastCr.text = "-"
        }else{
            lastCr.text = input.last_cr
        }
        lastCs.text = input.last_cs

        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .show()

        btnClose.setOnClickListener{
            infoDialog.dismiss()
        }
    }
}
