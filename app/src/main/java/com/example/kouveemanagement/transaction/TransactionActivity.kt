package com.example.kouveemanagement.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.TransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_transaction.*
import org.jetbrains.anko.startActivity

class  TransactionActivity : AppCompatActivity(), TransactionView, CustomerPetView, ProductView, ServiceView{

    private var transactionsList: MutableList<Transaction> = mutableListOf()
    private val transactionsTemp = ArrayList<Transaction>()
    private var temps = ArrayList<Transaction>()

    private lateinit var presenter: TransactionPresenter
    private lateinit var transactionAdapter: TransactionRecyclerViewAdapter

    private lateinit var lastEmp: String

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var dialogAlert: AlertDialog

    private lateinit var presenterC: CustomerPetPresenter
    private lateinit var customerPetId: String
    private lateinit var presenterP: ProductPresenter
    private lateinit var presenterS: ServicePresenter

    private var add = "0"
    private var type = "normal"

    companion object{
        lateinit var transaction: Transaction
        var customerPetNameDropdown: MutableList<String> = arrayListOf()
        var customerPetIdDropdown: MutableList<String> = arrayListOf()
        var customersPet: MutableList<CustomerPet> = arrayListOf()
        var productNameDropdown: MutableList<String> = arrayListOf()
        var productIdDropdown: MutableList<String> = arrayListOf()
        var products: MutableList<Product> = arrayListOf()
        var serviceNameDropdown: MutableList<String> = arrayListOf()
        var serviceIdDropdown: MutableList<String> = arrayListOf()
        var services: MutableList<Service> = arrayListOf()
        var transactions: MutableList<Transaction> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        presenterS = ServicePresenter(this, Repository())
        presenterS.getAllService()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        presenterC = CustomerPetPresenter(this, Repository())
        presenterC.getAllCustomerPet()
        presenter = TransactionPresenter(this, Repository())
        presenter.getAllTransaction()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        transactionAdapter = TransactionRecyclerViewAdapter(transactionsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter(transactions){
                    showAlert()
                }
                query?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter(transactions) {
                    showAlert()
                }
                newText?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            showAlert()
        }
        show_all.setOnClickListener {
            temps = transactionsTemp
            getList()
        }
        show_product.setOnClickListener {
            val filtered = transactionsTemp.filter { it.id.toString()[0] == 'P' }
            temps = filtered as ArrayList<Transaction>
            getList()
        }
        show_service.setOnClickListener {
            val filtered = transactionsTemp.filter { it.id.toString()[0] == 'L' }
            temps = filtered as ArrayList<Transaction>
            getList()
        }
    }

    private fun getList(){
        if (paid_off_switch.isChecked){
            val newFiltered = temps.filter { it.payment.equals("1") }
            recyclerview.adapter = TransactionRecyclerViewAdapter(newFiltered as MutableList<Transaction>){
                showDialog(it)
            }
        }else{
            val newFiltered = temps.filter { it.payment.equals("0") }
            recyclerview.adapter = TransactionRecyclerViewAdapter(newFiltered as MutableList<Transaction>){
                showDialog(it)
            }
        }
        transactionAdapter.notifyDataSetChanged()
    }

    override fun showTransactionLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun transactionSuccess(data: TransactionResponse?) {
        if (add == "0"){
            val temp: List<Transaction> = data?.transactions ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                transactionsList.addAll(temp)
                transactionsTemp.addAll(temp)
                temps.addAll(temp)
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = TransactionRecyclerViewAdapter(transactionsList){
                    transaction = it
                    showDialog(it)
                }
            }
        }else{
            transaction = data?.transactions?.get(0)!!
            startActivity<AddTransactionActivity>("type" to type)
        }
    }

    override fun transactionFailed() {
        Toast.makeText(this, "Failed Transaction", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }

    override fun showCustomerPetLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideCustomerPetLoading() {
        progressbar.visibility = View.GONE
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        val temp: List<CustomerPet> = data?.customerpets ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            customersPet.addAll(temp)
            customerPetNameDropdown.clear()
            customerPetIdDropdown.clear()
            for (i in temp.indices){
                customerPetNameDropdown.add(i, temp[i].name.toString())
                customerPetIdDropdown.add(i, temp[i].id.toString())
            }
        }
    }

    override fun customerPetFailed() {
        Toast.makeText(this, "Failed Customer Pet", Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(){
        dialogAlert = AlertDialog.Builder(this)
            .setTitle("Which transaction?")
            .setPositiveButton("Service"){ _, _ ->
                add = "1"
                type = "service"
                chooseCustomerPet("Service")
            }
            .setNeutralButton("Product"){_,_ ->
                add = "1"
                type = "product"
                chooseCustomerPet("Product")
            }
            .show()
    }

    private fun chooseCustomerPet(type: String) {
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose, null)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, customerPetNameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            customerPetId = customerPetIdDropdown[position]
            Toast.makeText(this, "ID CUSTOMER PET : $customerPetId", Toast.LENGTH_LONG).show()
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnAdd.setOnClickListener {
            transaction = Transaction(id_customer_pet = customerPetId, last_cs = lastEmp)
            Toast.makeText(this, "ID CUSTOMER PET : $lastEmp", Toast.LENGTH_LONG).show()
            presenter.addTransaction(type, transaction)
        }
    }

    private fun showDialog(transaction: Transaction){
        val input = transaction.id.toString()[0]
        Toast.makeText(this, "ID : $input", Toast.LENGTH_LONG).show()
        if (input == 'P'){
            type = "product"
        }else if (input == 'L'){
            type = "service"
        }
        Toast.makeText(this, transaction.id, Toast.LENGTH_LONG).show()

        dialogAlert = AlertDialog.Builder(this)
            .setTitle("What do you want to do?")
            .setPositiveButton("Show"){ _, _ ->
                startActivity<ShowTransactionActivity>("type" to type)
            }
            .setNeutralButton("Edit"){_,_ ->
                startActivity<AddTransactionActivity>("type" to type)
            }
            .show()
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
            productNameDropdown.clear()
            productIdDropdown.clear()
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    productNameDropdown.add(temp[i].name.toString())
                    productIdDropdown.add(temp[i].id.toString())
                }
            }
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
            serviceNameDropdown.clear()
            serviceIdDropdown.clear()
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    serviceNameDropdown.add(temp[i].name.toString())
                    serviceIdDropdown.add(temp[i].id.toString())
                }
            }
        }
    }

    override fun serviceFailed() {
        Toast.makeText(this, "Failed Service", Toast.LENGTH_SHORT).show()
    }

}
