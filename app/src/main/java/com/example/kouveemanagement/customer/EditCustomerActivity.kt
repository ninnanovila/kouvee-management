package com.example.kouveemanagement.customer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_customer.*
import org.jetbrains.anko.startActivity
import java.util.*

class EditCustomerActivity : AppCompatActivity(), CustomerView {

    private lateinit var id: String
    private lateinit var lastEmp: String
    private lateinit var presenter: CustomerPresenter
    private lateinit var customer: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer)
        setData()
        showDatePicker()
        presenter = CustomerPresenter(this, Repository())
        lastEmp = MainActivity.currentUser?.user_id.toString()
        btn_save.setOnClickListener {
            if (isValid()){
                getData()
                presenter.editCustomer(id, customer)
            }
        }
        btn_delete.setOnClickListener {
            presenter.deleteCustomer(id, lastEmp)
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setData(){
        val customer: Customer? = intent.getParcelableExtra("customer")
        id = customer?.id.toString()
        name.setText(customer?.name)
        address.setText(customer?.address)
        birthdate.setText(customer?.birthdate)
        phone_number.setText(customer?.phone_number)
        created_at.setText(customer?.created_at)
        updated_at.setText(customer?.updated_at)
        if (customer?.deleted_at.isNullOrEmpty()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(customer?.deleted_at)
        }
        last_emptv.setText(customer?.last_emp)
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthday = birthdate.text.toString()
        val phoneNumber = phone_number.text.toString()
        customer = Customer(id, name, address, birthday, phoneNumber, null, null, null, lastEmp)
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (address.text.isNullOrEmpty()){
            address.error = getString(R.string.error_address)
            return false
        }
        if (birthdate.text.isNullOrEmpty()){
            birthdate.error = getString(R.string.error_birthdate)
            return false
        }
        if (phone_number.text.isNullOrEmpty()){
            phone_number.error = getString(R.string.error_phone_number)
            return false
        }
        return true
    }

    private fun showDatePicker(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        birthdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                birthdate.setText("$year-$month-$dayOfMonth")
            }, year, month,day)
            datePickerDialog.show()
        }
    }

    override fun showCustomerLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideCustomerLoading() {
        progressbar.visibility = View.GONE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun customerSuccess(data: CustomerResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<CustomerManagementActivity>()
    }

    override fun customerFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerManagementActivity>()
    }
}
