package com.example.kouveemanagement.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomView
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.CustomerRecyclerViewAdapter
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_customer_management.*
import org.jetbrains.anko.startActivity

class CustomerManagementActivity : AppCompatActivity(), CustomerView {

    private lateinit var customerAdapter: CustomerRecyclerViewAdapter
    private lateinit var presenter: CustomerPresenter

    private var customersList: MutableList<Customer> = mutableListOf()
    private val customersTemp = ArrayList<Customer>()
    private var temps = ArrayList<Customer>()

    private lateinit var dialog: View

    companion object{
        var customers: MutableList<Customer> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_management)
        if (!CustomView.verifiedNetwork(this)){
            CustomView.warningSnackBar(container, baseContext, "Please check internet connection")
        }
        presenter = CustomerPresenter(this, Repository())
        presenter.getAllCustomer()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        customerAdapter = CustomerRecyclerViewAdapter(temps){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = CustomerRecyclerViewAdapter(customers){
                    showDialog(it)
                }
                query?.let { customerAdapter.filterCustomer(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = CustomerRecyclerViewAdapter(customers){
                    showDialog(it)
                }
                newText?.let { customerAdapter.filterCustomer(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddCustomerFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_all.setOnClickListener {
            temps = customersTemp
            getList()
        }
        show_en.setOnClickListener {
            val filtered = customersTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<Customer>
            getList()
        }
        show_dis.setOnClickListener {
            val filtered = customersTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<Customer>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenter.getAllCustomer()
        }
        CustomView.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomView.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = CustomerRecyclerViewAdapter(temps as MutableList<Customer>){}
        }else{
            if(sort_switch.isChecked){
                val sorted = temps.sortedBy { it.name }
                recyclerview.adapter = CustomerRecyclerViewAdapter(sorted as MutableList<Customer>){
                    showDialog(it)
                }
            }else{
                recyclerview.adapter = CustomerRecyclerViewAdapter(temps as MutableList<Customer>){
                    showDialog(it)
                }
            }
        }
        customerAdapter.notifyDataSetChanged()
    }

    override fun showCustomerLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideCustomerLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun customerSuccess(data: CustomerResponse?) {
        val temp: List<Customer> = data?.customers ?: emptyList()
//        val temp: List<Customer> = emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Oops, no result")
        }else{
            clearList()
            customersList.addAll(temp)
            customersTemp.addAll(temp)
            temps = customersTemp
            recyclerview.apply {
                layoutManager = LinearLayoutManager(this@CustomerManagementActivity)
                customerAdapter = CustomerRecyclerViewAdapter(customersList) {
                    showDialog(it)
                }
                adapter = customerAdapter
            }
            CustomView.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun customerFailed(data: String) {
        CustomView.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        customersList.clear()
        customersTemp.clear()
    }

    private fun showDialog(customer: Customer){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_customer, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val phoneNumber = dialog.findViewById<TextView>(R.id.phone_number)
        val birthday = dialog.findViewById<TextView>(R.id.birthdate)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val lastEmp = dialog.findViewById<TextView>(R.id.last_emptv)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = customer.name.toString()
        address.text = customer.address.toString()
        birthday.text = customer.birthdate.toString()
        phoneNumber.text = customer.phone_number.toString()
        createdAt.text = customer.created_at
        updatedAt.text = customer.updated_at
        if (customer.deleted_at.isNullOrEmpty()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = customer.deleted_at
        }
        lastEmp.text = customer.last_emp
        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()
        if (customer.deleted_at != null){
            btnEdit.visibility = View.GONE
        }
        btnEdit.setOnClickListener {
            startActivity<EditCustomerActivity>("customer" to customer)
        }
        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }

}
