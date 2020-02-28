package com.example.kouveemanagement.customerpet

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kouveemanagement.MainActivity

import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.CustomerPetResponse
import com.example.kouveemanagement.presenter.CustomerPetPresenter
import com.example.kouveemanagement.presenter.CustomerPetView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_customer_pet.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddCustomerPetFragment : Fragment(), CustomerPetView {

    private lateinit var last_emp: String
    private lateinit var customerPet: CustomerPet
    private lateinit var presenter: CustomerPetPresenter

    companion object{
        fun newInstance() = AddCustomerPetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_customer_pet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        last_emp = MainActivity.currentUser?.user_id.toString()
        btn_add.setOnClickListener {
            getData()
            presenter = CustomerPetPresenter(this, Repository())
            presenter.addCustomerPet(customerPet)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        showDatePicker()
    }

    fun getData(){
        val name =  name.text.toString()
        val birthdate = birthdate.text.toString()
        customerPet = CustomerPet(null, "1","1", name, birthdate, null, null, null, last_emp)
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

    override fun showLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_add.visibility = View.VISIBLE
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<CustomerPetManagementActivity>()
    }

    override fun customerPetFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

}
