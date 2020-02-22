package com.example.kouveemanagement.customer


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_customer.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddCustomerFragment : Fragment(), CustomerView {

    private lateinit var customer: Customer
    private lateinit var presenter: CustomerPresenter

    companion object{
        fun newInstance() = AddCustomerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            getData()

            presenter = CustomerPresenter(this, Repository())
            presenter.addCustomer(customer)
        }

        showDatePicker()
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val birthdate = birthdate.text.toString()
        val phone_number = phone_number.text.toString()
        val last_emp = "0"

        customer = Customer(null, name, address, birthdate, phone_number, last_emp)
    }

    fun showDatePicker(){

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

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun customerSuccess(data: CustomerResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun customerFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

}
