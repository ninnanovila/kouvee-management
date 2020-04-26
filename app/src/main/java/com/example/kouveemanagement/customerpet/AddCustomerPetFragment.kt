package com.example.kouveemanagement.customerpet

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_customer_pet.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddCustomerPetFragment : Fragment(), CustomerPetView{

    private lateinit var lastEmp: String
    private lateinit var customerPet: CustomerPet
    private lateinit var presenter: CustomerPetPresenter

    private lateinit var idCustomer: String
    private lateinit var idType: String

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
        lastEmp = MainActivity.currentUser?.user_id.toString()
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = CustomerPetPresenter(this, Repository())
                presenter.addCustomerPet(customerPet)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        showDatePicker()
        setCustomerDropdown()
        setTypeDropDown()
    }

    fun getData(){
        val name =  name.text.toString()
        val birthday = birthdate.text.toString()
        customerPet = CustomerPet(id_customer = idCustomer,id_type = idType, name = name, birthdate = birthday, last_emp = lastEmp)
    }

    private fun showDatePicker(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        birthdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener {
                            _, year, month, dayOfMonth -> birthdate.setText("$year-$month-$dayOfMonth")
                    }, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (birthdate.text.isNullOrEmpty()){
            birthdate.error = getString(R.string.error_birthdate)
            return false
        }
        return true
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        startActivity<CustomerPetManagementActivity>()
    }

    override fun customerPetFailed(data: String) {
        btn_add.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    override fun showCustomerPetLoading() {
        btn_add.startAnimation()
    }

    override fun hideCustomerPetLoading() {
    }

    private fun setCustomerDropdown(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, CustomerPetManagementActivity.nameCustomerDropdown)
        customer_dropdown.setAdapter(adapter)
        customer_dropdown.setOnItemClickListener { _, _, position, _ ->
            idCustomer = CustomerPetManagementActivity.idCustomerList[position]
            val name = CustomerPetManagementActivity.nameCustomerDropdown[position]
            Toast.makeText(context, "Customer : $name", Toast.LENGTH_LONG).show()
        }
    }

    private fun setTypeDropDown(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, CustomerPetManagementActivity.nameTypeDropdown)
        type_dropdown.setAdapter(adapter)
        type_dropdown.setOnItemClickListener { _, _, position, _ ->
            idType = CustomerPetManagementActivity.idTypeList[position]
            val name = CustomerPetManagementActivity.nameTypeDropdown[position]
            Toast.makeText(context, "Type : $name", Toast.LENGTH_LONG).show()
        }
    }

}
