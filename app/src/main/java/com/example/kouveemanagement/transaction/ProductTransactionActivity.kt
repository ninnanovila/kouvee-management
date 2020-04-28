package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.TransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_product_transaction.*
import org.jetbrains.anko.startActivity

class ProductTransactionActivity : AppCompatActivity(), TransactionView, CustomerPetView, ProductView, CustomerView {

    private var transactionsList: MutableList<Transaction> = mutableListOf()
    private val transactionsTemp = ArrayList<Transaction>()
    private var temps = ArrayList<Transaction>()

    private lateinit var presenter: TransactionPresenter
    private lateinit var transactionAdapter: TransactionRecyclerViewAdapter

    private lateinit var lastEmp: String

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var dialogAlert: AlertDialog

    private lateinit var petId: String
    private lateinit var presenterC: CustomerPetPresenter
    private lateinit var presenterP: ProductPresenter
    private lateinit var presenterCu: CustomerPresenter

    private var add = "0"
    private var type = "product"

    companion object{
        var transaction: Transaction = Transaction()
        var customerPetName: MutableList<String> = arrayListOf()
        var customerPetId: MutableList<String> = arrayListOf()
        var customersPet: MutableList<CustomerPet> = arrayListOf()
        var productName: MutableList<String> = arrayListOf()
        var productId: MutableList<String> = arrayListOf()
        var products: MutableList<Product> = arrayListOf()
        var transactions: MutableList<Transaction> = mutableListOf()
        var customers: MutableList<Customer> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        presenterCu = CustomerPresenter(this, Repository())
        presenterCu.getAllCustomer()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        presenterC = CustomerPetPresenter(this, Repository())
        presenterC.getAllCustomerPet()
        presenter = TransactionPresenter(this, Repository())
        presenter.getAllProductTransaction()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        transactionAdapter = TransactionRecyclerViewAdapter("product", customersPet, transactionsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter("product", customersPet,transactions){
                    showAlert()
                }
                query?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter("product", customersPet,transactions) {
                    showAlert()
                }
                newText?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
        })
        fab_add.setOnClickListener{
            showAlert()
        }
        swipe_rv.setOnRefreshListener {
            presenterCu.getAllCustomer()
            presenterP.getAllProduct()
            presenterC.getAllCustomerPet()
            presenter.getAllProductTransaction()
        }
        show_paid_off.setOnClickListener{
            val filtered = transactionsTemp.filter { it.payment == "1" }
            temps = filtered as ArrayList<Transaction>
            getList()
        }
        show_not_paid_off.setOnClickListener{
            val filtered = transactionsTemp.filter { it.payment == "0" }
            temps = filtered as ArrayList<Transaction>
            getList()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = TransactionRecyclerViewAdapter("product", customersPet,temps){}
        }else{
            recyclerview.adapter = TransactionRecyclerViewAdapter("product", customersPet,temps){
                transaction = it
                showDialog(it, it.payment.toString())
            }
        }
        transactionAdapter.notifyDataSetChanged()
    }

    override fun showTransactionLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideTransactionLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun transactionSuccess(data: TransactionResponse?) {
        if (add == "0"){
            val temp: List<Transaction> = data?.transactions ?: emptyList()
            if (temp.isEmpty()){
                CustomFun.warningLongSnackBar(container, baseContext, "Empty data")
            }else{
                clearList()
                transactionsList.addAll(temp)
                transactionsTemp.addAll(temp)
                temps = transactionsTemp
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = TransactionRecyclerViewAdapter("product", customersPet,transactionsList){
                    transaction = it
                    showDialog(it, it.payment.toString())
                }
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }else{
            transaction = data?.transactions?.get(0)!!
            startActivity<AddTransactionActivity>("type" to "product")
        }
    }

    override fun transactionFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        transactionsList.clear()
        transactionsTemp.clear()
    }

    override fun showCustomerPetLoading() {
    }

    override fun hideCustomerPetLoading() {
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        val temp: List<CustomerPet> = data?.customerpets ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningLongSnackBar(container, baseContext, "Empty data")
        }else{
            clearPet()
            customersPet.addAll(temp)
            for (pet in temp){
                if (pet.deleted_at == null){
                    customerPetName.add(pet.name.toString())
                    customerPetId.add(pet.id.toString())
                }
            }
        }
    }

    override fun customerPetFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearPet(){
        customersPet.clear()
        customerPetName.clear()
        customerPetId.clear()
    }

    override fun showProductLoading() {
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningLongSnackBar(container, baseContext, "Empty data")
        }else{
            clearProduct()
            products.addAll(temp)
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    productName.add(temp[i].name.toString())
                    productId.add(temp[i].id.toString())
                }
            }
        }
    }

    override fun productFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearProduct(){
        products.clear()
        productName.clear()
        productId.clear()
    }

    override fun showCustomerLoading() {
    }

    override fun hideCustomerLoading() {
    }

    override fun customerSuccess(data: CustomerResponse?) {
        val temp = data?.customers ?: emptyList()
        if (temp.isNotEmpty()){
            customers.clear()
            customers.addAll(temp)
        }
    }

    override fun customerFailed(data: String) {
    }

    private fun showAlert(){
        add = "1"
        type = "product"
        dialogAlert = AlertDialog.Builder(this)
            .setIcon(R.drawable.product_transaction)
            .setTitle("Confirmation")
            .setMessage("Are you sure to create product transaction?")
            .setCancelable(false)
            .setPositiveButton("YES"){ _, _ ->
                chooseCustomerPet()
            }
            .setNegativeButton("NO"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun chooseCustomerPet() {
        petId = "-1"
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_pet, null)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, customerPetName)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            petId = customerPetId[position]
            checkCustomer(petId)
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnAdd.setOnClickListener {
            transaction = if (petId == "-1"){
                Transaction(last_cs = lastEmp)
            }else{
                Transaction(id_customer_pet = petId, last_cs = lastEmp)
            }
            presenter.addTransaction("Product", transaction)
        }
    }

    private fun checkCustomer(petId: String){
        var idCustomer = "-1"
        for(customerPet in customersPet){
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

    private fun showDialog(transactionInput: Transaction, payment: String){
        transaction = transactionInput
        Toast.makeText(this, "ID : "+transaction.id, Toast.LENGTH_LONG).show()

        if (payment == "0"){
            dialogAlert = AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(R.drawable.product_transaction)
                .setTitle("What do you want to do?")
                .setMessage("You can edit this transaction, this transaction not yet paid off.")
                .setPositiveButton("Edit"){ _, _ ->
                    startActivity<AddTransactionActivity>("type" to "product")
                }
                .setNegativeButton("Cancel"){dialog,_ ->
                    dialog.dismiss()
                }
                .setNeutralButton("Show"){_,_ ->
                    startActivity<ShowTransactionActivity>("type" to "product")
                }
                .show()
        }else if (payment == "1"){
            dialogAlert = AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(R.drawable.product_transaction)
                .setTitle("What do you want to do?")
                .setMessage("You can not edit this transaction, this transaction is paid off.")
                .setPositiveButton("Show"){ _, _ ->
                    startActivity<ShowTransactionActivity>("type" to "product")
                }
                .setNegativeButton("Cancel"){dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }

}
