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
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.CustomerPetResponse
import com.example.kouveemanagement.model.Transaction
import com.example.kouveemanagement.model.TransactionResponse
import com.example.kouveemanagement.presenter.CustomerPetPresenter
import com.example.kouveemanagement.presenter.CustomerPetView
import com.example.kouveemanagement.presenter.TransactionPresenter
import com.example.kouveemanagement.presenter.TransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_transaction.*
import org.jetbrains.anko.startActivity

class  TransactionActivity : AppCompatActivity(), TransactionView, CustomerPetView{

    private var transactionsList: MutableList<Transaction> = mutableListOf()
    private lateinit var presenter: TransactionPresenter

    private lateinit var lastEmp: String

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var dialogAlert: AlertDialog

    private lateinit var presenterC: CustomerPetPresenter
    private lateinit var customerPetId: String

    private var add = "0"
    private var type = "normal"

    companion object{
        lateinit var transaction: Transaction
        var customerPetNameDropdown: MutableList<String> = arrayListOf()
        var customerPetIdDropdown: MutableList<String> = arrayListOf()
        var transactions: MutableList<Transaction> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        presenterC = CustomerPetPresenter(this, Repository())
        presenterC.getAllCustomerPet()
        presenter = TransactionPresenter(this, Repository())
        presenter.getAllTransaction()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        val adapter = TransactionRecyclerViewAdapter(transactionsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter(transactions){
                    showAlert()
                }
                query?.let { adapter.filterTransaction(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter(transactions) {
                    showAlert()
                }
                newText?.let { adapter.filterTransaction(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            showAlert()
        }
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
                for (i in temp.indices){
                    transactionsList.add(i, temp[i])
                }
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
            if (customerPetNameDropdown.isNotEmpty()){
                customerPetNameDropdown.clear()
                customerPetIdDropdown.clear()
                for (i in temp.indices){
                    customerPetNameDropdown.add(i, temp[i].name.toString())
                    customerPetIdDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    customerPetNameDropdown.add(i, temp[i].name.toString())
                    customerPetIdDropdown.add(i, temp[i].id.toString())
                }
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

}
