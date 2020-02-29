package com.example.kouveemanagement.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.*
import com.example.kouveemanagement.adapter.CustomerRecyclerViewAdapter
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_customer_management.*
import org.jetbrains.anko.startActivity

class CustomerManagementActivity : AppCompatActivity(), CustomerView {

    private var customers: MutableList<Customer> = mutableListOf()
    private lateinit var presenter: CustomerPresenter

    private lateinit var dialog: View
    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_management)
        presenter = CustomerPresenter(this, Repository())
        presenter.getAllCustomer()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        fabAnimation()
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
            for (i in temp.indices){
                customers.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = CustomerRecyclerViewAdapter(customers) {
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
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
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = customer.name.toString()
        address.text = customer.address.toString()
        birthdate.text = customer.birthdate.toString()
        phone_number.text = customer.phone_number.toString()

        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditCustomerActivity>("customer" to customer)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
            val fragment: Fragment = AddCustomerFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }

}
