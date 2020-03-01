package com.example.kouveemanagement.customerpet

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idCustomerList: MutableList<String> = arrayListOf()
    private lateinit var idCustomer: String

    private var typeDropdown: MutableList<String> = arrayListOf()
    private var idTypeList: MutableList<String> = arrayListOf()
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
        nameDropdown = CustomerPetManagementActivity.nameCustomerDropdown
        idCustomerList = CustomerPetManagementActivity.idCustomerList
        typeDropdown = CustomerPetManagementActivity.nameTypeDropdown
        idTypeList = CustomerPetManagementActivity.idTypeList
        lastEmp = MainActivity.currentUser?.user_id.toString()
        btn_add.setOnClickListener {
            getData()
            presenter = CustomerPetPresenter(this, Repository())
            presenter.addCustomerPet(customerPet)
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
        customerPet = CustomerPet(null, idCustomer,idType, name, birthday, null, null, null, lastEmp)
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

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<CustomerPetManagementActivity>()
    }

    override fun customerPetFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun showCustomerPetLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideCustomerPetLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_add.visibility = View.VISIBLE
    }

    private fun setCustomerDropdown(){
        val adapter = context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        }
        customer_dropdown.setAdapter(adapter)
        customer_dropdown.setOnItemClickListener { _, _, position, _ ->
            idCustomer = idCustomerList[position]
            Toast.makeText(context, "ID CUSTOMER : $idCustomer", Toast.LENGTH_LONG).show()
        }
    }

    private fun setTypeDropDown(){
        val adapter = context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item, typeDropdown)
        }
        type_dropdown.setAdapter(adapter)
        type_dropdown.setOnItemClickListener { _, _, position, _ ->
            idType = idTypeList[position]
            Toast.makeText(context, "ID TYPE : $idType", Toast.LENGTH_LONG).show()
        }
    }

}
