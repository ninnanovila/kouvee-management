package com.example.kouveemanagement.customer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_customer.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddCustomerFragment : Fragment(), CustomerView {

    private lateinit var lastEmp: String
    private lateinit var customer: Customer
    private lateinit var presenter: CustomerPresenter

    companion object{
        fun newInstance() = AddCustomerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastEmp = MainActivity.currentUser?.user_id.toString()
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = CustomerPresenter(this, Repository())
                presenter.addCustomer(customer)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        showDatePicker()
    }

    fun getData(){
        val inputName = name.text.toString()
        val inputAddress = address.text.toString()
        val inputBirthday = birthdate.text.toString()
        val inputPhoneNumber = phone_number.text.toString()
        customer = Customer(null, inputName, inputAddress, inputBirthday, inputPhoneNumber, null, null, null, lastEmp)
    }

    private fun showDatePicker(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        birthdate.setOnClickListener {
            val datePickerDialog =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener {
                            _, year, month, dayOfMonth -> birthdate.setText("$year-$month-$dayOfMonth")
                    }, year, month, day)
                }
            datePickerDialog?.show()
        }
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = R.string.error_name.toString()
            return false
        }
        if (address.text.isNullOrEmpty()){
            address.error = R.string.error_address.toString()
            return false
        }
        if (birthdate.text.isNullOrEmpty()){
            birthdate.error = R.string.error_birthdate.toString()
            return false
        }
        if (phone_number.text.isNullOrEmpty()){
            phone_number.error = R.string.error_phone_number.toString()
            return false
        }
        return true
    }

    override fun showCustomerLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideCustomerLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_add.visibility = View.VISIBLE
    }

    override fun customerSuccess(data: CustomerResponse?) {
        Toast.makeText(context, "Success Customer", Toast.LENGTH_SHORT).show()
        startActivity<CustomerManagementActivity>()
    }

    override fun customerFailed() {
        Toast.makeText(context, "Failed Customer", Toast.LENGTH_SHORT).show()
    }

}
