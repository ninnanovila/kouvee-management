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
import kotlinx.android.synthetic.main.activity_product_transaction.*
import org.jetbrains.anko.startActivity

class ProductTransactionActivity : AppCompatActivity(), TransactionView, CustomerPetView, ProductView {

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

    private var add = "0"
    private var type = "normal"

    companion object{
        var transaction: Transaction = Transaction()
        var customerPetNameDropdown: MutableList<String> = arrayListOf()
        var customerPetIdDropdown: MutableList<String> = arrayListOf()
        var customersPet: MutableList<CustomerPet> = arrayListOf()
        var productNameDropdown: MutableList<String> = arrayListOf()
        var productIdDropdown: MutableList<String> = arrayListOf()
        var products: MutableList<Product> = arrayListOf()
        var transactions: MutableList<Transaction> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        presenterC = CustomerPetPresenter(this, Repository())
        presenterC.getAllCustomerPet()
        presenter = TransactionPresenter(this, Repository())
        presenter.getAllProductTransaction()
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
    }

    private fun showAlert(){
        add = "1"
        type = "product"
        dialogAlert = AlertDialog.Builder(this)
            .setTitle("Create transaction ?")
            .setPositiveButton("YES"){ _, _ ->
                chooseCustomerPet("Product")
            }
            .setNeutralButton("NO"){_,_ ->
            }
            .show()
    }

    private fun chooseCustomerPet(type: String) {
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_pet, null)
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

    private fun showDialog(transactionInput: Transaction){
        val input = transactionInput.id.toString()[0]
        transaction = transactionInput
        if (input == 'P'){
            type = "product"
        }else if (input == 'L'){
            type = "service"
        }
        Toast.makeText(this, transaction.id, Toast.LENGTH_LONG).show()

        dialogAlert = AlertDialog.Builder(this)
            .setTitle("What do you want to do?")
            .setPositiveButton("Show"){ _, _ ->
                startActivity<ShowTransactionActivity>("type" to "product")
            }
            .setNeutralButton("Edit"){_,_ ->
                startActivity<AddTransactionActivity>("type" to "product")
            }
            .show()
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

    override fun transactionFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
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

    override fun customerPetFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
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

    override fun productFailed(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }
}
