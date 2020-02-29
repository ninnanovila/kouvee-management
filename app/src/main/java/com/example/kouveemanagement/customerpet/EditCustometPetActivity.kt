package com.example.kouveemanagement.customerpet

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

class EditCustometPetActivity : AppCompatActivity(), CustomerPetView {

    private lateinit var id: String
    private lateinit var last_emp: String
    private lateinit var presenter: CustomerPetPresenter
    private lateinit var customerpet: CustomerPet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customet_pet)
        setData()
        showDatePicker()
        presenter = CustomerPetPresenter(this, Repository())
        last_emp = MainActivity.currentUser?.user_id.toString()
        btn_save.setOnClickListener {
            getData()
            presenter.editCustomerPet(id, customerpet)
        }
        btn_delete.setOnClickListener {
            presenter.deleteCustomerPet(id)
        }
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
    }

    private fun setData(){
        val customerPet: CustomerPet? = intent.getParcelableExtra("customerpet")
        id = customerPet?.id.toString()
        name.setText(customerPet?.name)
        birthdate.setText(customerPet?.birthdate)
        type.setText(customerPet?.id_type)
        customer.setText(customerPet?.id_customer)
        created_at.text = customerPet?.created_at
        updated_at.text = customerPet?.updated_at
        deleted_at.text = customerPet?.deleted_at
        last_emptv.text = customerPet?.last_emp
    }

    fun getData(){
        val name = name.text.toString()
        val birthdate = birthdate.text.toString()
        val type = type.text.toString()
        val customer = customer.text.toString()
        customerpet = CustomerPet(id, customer, type, name, birthdate, null, null, null, last_emp)
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
        progressbar.visibility = View.INVISIBLE
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
