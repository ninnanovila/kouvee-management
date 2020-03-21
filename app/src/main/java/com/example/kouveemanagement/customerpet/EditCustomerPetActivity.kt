package com.example.kouveemanagement.customerpet

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kouveemanagement.CustomerServiceActivity
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.CustomerPetResponse
import com.example.kouveemanagement.presenter.CustomerPetPresenter
import com.example.kouveemanagement.presenter.CustomerPetView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_customet_pet.*
import org.jetbrains.anko.startActivity
import java.util.*

class EditCustomerPetActivity : AppCompatActivity(), CustomerPetView {

    private lateinit var id: String
    private lateinit var lastEmp: String
    private lateinit var presenter: CustomerPetPresenter
    private lateinit var customerpet: CustomerPet

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idCustomerList: MutableList<String> = arrayListOf()
    private lateinit var idCustomer: String

    private var typeDropdown: MutableList<String> = arrayListOf()
    private var idTypeList: MutableList<String> = arrayListOf()
    private lateinit var idType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customet_pet)
        setData()
        showDatePicker()
        presenter = CustomerPetPresenter(this, Repository())
        lastEmp = MainActivity.currentUser?.user_id.toString()
        btn_save.setOnClickListener {
            if (isValid()){
                getData()
                presenter.editCustomerPet(id, customerpet)
            }
        }
        btn_delete.setOnClickListener {
            presenter.deleteCustomerPet(id)
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setDropdown(customerPet: CustomerPet){
        var positionC = 0
        var positionT = 0
        nameDropdown = CustomerPetManagementActivity.nameCustomerDropdown
        idCustomerList = CustomerPetManagementActivity.idCustomerList
        typeDropdown = CustomerPetManagementActivity.nameTypeDropdown
        idTypeList = CustomerPetManagementActivity.idTypeList
        for (i in idCustomerList.indices){
            if (idCustomerList[i] == customerPet.id_customer){
                positionC = i
                idCustomer = idCustomerList[i]
            }
        }
        for (i in idTypeList.indices){
            if (idTypeList[i] == customerPet.id_type){
                positionT = i
                idType = idTypeList[i]
            }
        }
        val adapterC = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        customer_dropdown.setAdapter(adapterC)
        customer_dropdown.setText(nameDropdown[positionC], true)
        customer_dropdown.setOnItemClickListener { _, _, position, _ ->
            idCustomer = idCustomerList[position]
            Toast.makeText(this, "ID CUSTOMER : $idCustomer", Toast.LENGTH_LONG).show()
        }
        val adapterT = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeDropdown)
        type_dropdown.setAdapter(adapterT)
        type_dropdown.setText(typeDropdown[positionT], true)
        type_dropdown.setOnItemClickListener { _, _, position, _ ->
            idType = idTypeList[position]
            Toast.makeText(this, "ID TYPE : $idType", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(){
        val customerPet: CustomerPet? = intent.getParcelableExtra("customerpet")
        id = customerPet?.id.toString()
        name.setText(customerPet?.name)
        birthdate.setText(customerPet?.birthdate)
        created_at.setText(customerPet?.created_at)
        updated_at.setText(customerPet?.updated_at)
        if (customerPet?.deleted_at.isNullOrBlank()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(customerPet?.deleted_at)
        }
        last_emptv.setText(customerPet?.last_emp)
        customerPet?.let { setDropdown(it) }
    }

    fun getData(){
        val name = name.text.toString()
        val birthdate = birthdate.text.toString()
        customerpet = CustomerPet(id, idCustomer, idType, name, birthdate, null, null, null, lastEmp)
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

    override fun showCustomerPetLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideCustomerPetLoading() {
        progressbar.visibility = View.GONE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<CustomerPetManagementActivity>()
    }

    override fun customerPetFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerPetManagementActivity>()
    }
}
