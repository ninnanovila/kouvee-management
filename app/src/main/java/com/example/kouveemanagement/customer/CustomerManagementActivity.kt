package com.example.kouveemanagement.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var dialog: View

    companion object{
        var customers: MutableList<Customer> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_management)
        presenter = CustomerPresenter(this, Repository())
        presenter.getAllCustomer()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        customerAdapter = CustomerRecyclerViewAdapter(customersList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = CustomerRecyclerViewAdapter(customers){
                    showDialog(it)
                }
                query?.let { customerAdapter.filterCustomer(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
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
        sort_name.setOnClickListener {
            val sorted = customersTemp.sortedBy { it.name }
            recyclerview.adapter = CustomerRecyclerViewAdapter(sorted as MutableList<Customer>){
                showDialog(it)
            }
            customerAdapter.notifyDataSetChanged()
        }
        show_all.setOnClickListener {
            recyclerview.adapter = CustomerRecyclerViewAdapter(customersList){
                showDialog(it)
            }
            customerAdapter.notifyDataSetChanged()
        }
        show_en.setOnClickListener {
            val filtered = customersTemp.filter { it.deleted_at === null }
            recyclerview.adapter = CustomerRecyclerViewAdapter(filtered as MutableList<Customer>){
                showDialog(it)
            }
            customerAdapter.notifyDataSetChanged()
        }
        show_dis.setOnClickListener {
            val filtered = customersTemp.filter { it.deleted_at !== null }
            recyclerview.adapter = CustomerRecyclerViewAdapter(filtered as MutableList<Customer>){
                showDialog(it)
            }
            customerAdapter.notifyDataSetChanged()
        }
    }

    override fun showCustomerLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideCustomerLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun customerSuccess(data: CustomerResponse?) {
        val temp: List<Customer> = data?.customers ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            customersList.addAll(temp)
            customersTemp.addAll(temp)
            recyclerview.apply {
                layoutManager = LinearLayoutManager(this@CustomerManagementActivity)
                customerAdapter = CustomerRecyclerViewAdapter(customersList) {
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
                adapter = customerAdapter
            }
        }
    }

    override fun customerFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(customer: Customer){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_customer, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val phoneNumber = dialog.findViewById<TextView>(R.id.phone_number)
        val birthday = dialog.findViewById<TextView>(R.id.birthdate)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = customer.name.toString()
        address.text = customer.address.toString()
        birthday.text = customer.birthdate.toString()
        phoneNumber.text = customer.phone_number.toString()
        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()
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
