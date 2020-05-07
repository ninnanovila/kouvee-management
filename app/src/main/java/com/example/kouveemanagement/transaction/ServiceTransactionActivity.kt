package com.example.kouveemanagement.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.TransactionRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_service_transaction.*
import org.jetbrains.anko.startActivity

class  ServiceTransactionActivity : AppCompatActivity(), TransactionView, CustomerPetView, ServiceView, CustomerView, PetSizeView{

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
    private lateinit var petId: String
    private lateinit var presenterS: ServicePresenter
    private lateinit var presenterCu: CustomerPresenter
    private lateinit var presenterSize: PetSizePresenter

    private var add = "0"
    private var type = "service"
    private var size:Int = -1

    companion object{
        var idOfSize: String = "-1"
        var transaction: Transaction = Transaction()
        var customerPetName: MutableList<String> = arrayListOf()
        var customerPetId: MutableList<String> = arrayListOf()
        var customersPet: MutableList<CustomerPet> = arrayListOf()
        var serviceName: MutableList<String> = arrayListOf()
        var serviceId: MutableList<String> = arrayListOf()
        var services: MutableList<Service> = arrayListOf()
        var transactions: MutableList<Transaction> = mutableListOf()
        var customers: MutableList<Customer> = mutableListOf()
        var petSizesName: MutableList<String> = arrayListOf()
        var petSizesId: MutableList<String> = arrayListOf()
        var petSizes: MutableList<PetSize> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_transaction)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        getAllData()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        transactionAdapter = TransactionRecyclerViewAdapter("service", customersPet, transactionsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter("service", customersPet, transactions){
                    showAlert()
                }
                query?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = TransactionRecyclerViewAdapter("service", customersPet, transactions) {
                    showAlert()
                }
                newText?.let { transactionAdapter.filterTransaction(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            showAlert()
        }
        swipe_rv.setOnRefreshListener {
            presenterCu.getAllCustomer()
            presenterS.getAllService()
            presenterC.getAllCustomerPet()
            presenter.getAllServiceTransaction()
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

    private fun getAllData(){
        presenterSize = PetSizePresenter(this, Repository())
        presenterSize.getAllPetSize()
        presenterCu = CustomerPresenter(this, Repository())
        presenterCu.getAllCustomer()
        presenterS = ServicePresenter(this, Repository())
        presenterS.getAllService()
        presenterC = CustomerPetPresenter(this, Repository())
        presenterC.getAllCustomerPet()
        presenter = TransactionPresenter(this, Repository())
        presenter.getAllServiceTransaction()
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = TransactionRecyclerViewAdapter("service", customersPet, temps){}
        }else{
            recyclerview.adapter = TransactionRecyclerViewAdapter("service", customersPet, temps){
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
                val filtered = transactionsTemp.filter { it.payment == "0" }
                temps = filtered as ArrayList<Transaction>
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = TransactionRecyclerViewAdapter("service", customersPet, temps){
                    transaction = it
                    showDialog(it, it.payment.toString())
                }
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }else{
            transaction = data?.transactions?.get(0)!!
            startActivity<AddTransactionActivity>("type" to "service")
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
            for (i in temp.indices){
                customerPetName.add(i, temp[i].name.toString())
                customerPetId.add(i, temp[i].id.toString())
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

    override fun showServiceLoading() {
    }

    override fun hideServiceLoading() {
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.warningLongSnackBar(container, baseContext, "Empty data")
        }else{
            clearService()
            services.addAll(temp)
            for (service in temp){
                if (service.deleted_at == null){
                    serviceName.add(service.name.toString())
                    serviceId.add(service.id.toString())
                }
            }
        }
    }

    override fun serviceFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearService(){
        services.clear()
        serviceName.clear()
        serviceId.clear()
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

    override fun showPetSizeLoading() {
    }

    override fun hidePetSizeLoading() {
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
        val temp = data?.petsize ?: emptyList()
        if (temp.isNotEmpty()){
            clearPetSizes()
            petSizes.addAll(temp)
            for (size in temp){
                if (size.deleted_at == null){
                    petSizesName.add(size.name.toString())
                    petSizesId.add(size.id.toString())
                }
            }
        }
    }

    override fun petSizeFailed(data: String) {
    }

    private fun clearPetSizes(){
        petSizes.clear()
        petSizesId.clear()
        petSizesName.clear()
    }

    private fun showAlert(){
        add = "1"
        type = "service"
        dialogAlert = AlertDialog.Builder(this)
            .setIcon(R.drawable.service_transaction)
            .setTitle("Confirmation")
            .setMessage("Are you sure to create service transaction?")
            .setCancelable(false)
            .setPositiveButton("YES"){ _, _ ->
                chooseSize()
            }
            .setNegativeButton("NO"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun chooseSize(){
        val name = petSizesName.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Choose Size")
            .setCancelable(false)
            .setSingleChoiceItems(name, size){ _, i ->
                size = i
                idOfSize = petSizesId[i]
            }
            .setPositiveButton("CHOOSE"){ dialogInterface, _ ->
                if (size == -1){
                    CustomFun.failedSnackBar(container, baseContext, "Please choose size")
                }else{
                    dialogInterface.dismiss()
                    chooseCustomerPet()
                }
            }
            .setNegativeButton("CANCEL"){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun chooseCustomerPet() {
        petId = "-1"
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_pet, null)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, customerPetName)
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
            presenter.addTransaction("Service", transaction)
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
            if (transactionInput.status != "Done"){
                dialogAlert = AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.service_transaction)
                    .setTitle("What do you want to do?")
                    .setMessage("You can edit this transaction, this transaction on progress and not yet paid off.")
                    .setPositiveButton("Edit"){ _, _ ->
                        startActivity<AddTransactionActivity>("type" to "service")
                    }
                    .setNegativeButton("Cancel"){dialog,_ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton("Show"){_,_ ->
                        startActivity<ShowTransactionActivity>("type" to "service")
                    }
                    .show()
            }else{
                dialogAlert = AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.service_transaction)
                    .setTitle("What do you want to do?")
                    .setMessage("You can edit this transaction, this transaction done and not yet paid off. Let's pay it.")
                    .setPositiveButton("Show"){ _, _ ->
                        startActivity<ShowTransactionActivity>("type" to "service")
                    }
                    .setNegativeButton("Cancel"){dialog,_ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }else if (payment == "1"){
            dialogAlert = AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(R.drawable.service_transaction)
                .setTitle("What do you want to do?")
                .setMessage("You can not edit this transaction, this transaction is paid off.")
                .setPositiveButton("Show"){ _, _ ->
                    startActivity<ShowTransactionActivity>("type" to "service")
                }
                .setNegativeButton("Cancel"){dialog, _ ->
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
