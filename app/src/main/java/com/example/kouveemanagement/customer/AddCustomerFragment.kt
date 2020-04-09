package com.example.kouveemanagement.customer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomView
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

    override fun showCustomerLoading() {
        btn_add.startAnimation()
    }

    override fun hideCustomerLoading() {
    }

    override fun customerSuccess(data: CustomerResponse?) {
        startActivity<CustomerManagementActivity>()
    }

    override fun customerFailed() {
        btn_add.revertAnimation()
        context?.let { view?.let { itView -> CustomView.failedSnackBar(itView, it, "Oops, try again") } }
    }

}
