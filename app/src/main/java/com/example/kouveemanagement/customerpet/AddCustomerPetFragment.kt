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
class AddCustomerPetFragment : Fragment(), CustomerPetView, CustomerView, PetTypeView{

    private lateinit var last_emp: String
    private lateinit var customerPet: CustomerPet
    private lateinit var presenter: CustomerPetPresenter

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idCustomer: MutableList<String> = arrayListOf()
    private lateinit var presenterC: CustomerPresenter

    private var typeDropdown: MutableList<String> = arrayListOf()
    private var idType: MutableList<String> = arrayListOf()
    private lateinit var presenterT: PetTypePresenter

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
        presenterC = CustomerPresenter(this, Repository())
        presenterC.getAllCustomer()
        presenterT = PetTypePresenter(this, Repository())
        presenterT.getAllPetType()
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
        var id_customer: String = ""
        customer_dropdown.setOnItemClickListener { _, _, position, _ ->
            id_customer = idCustomer[position]
        }
        var id_type: String = ""
        type_dropdown.setOnItemClickListener { _, _, position, _ ->
            id_type = idType[position]
        }
        customerPet = CustomerPet(null, id_customer,id_type, name, birthdate, null, null, null, last_emp)
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

    override fun customerSuccess(data: CustomerResponse?) {
        val temp: List<Customer> = data?.customers ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                nameDropdown.add(i, temp[i].name.toString())
                idCustomer.add(i, temp[i].id.toString())
            }
            setCustomerDropdown()
        }
    }

    override fun customerFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun showCustomerLoading() {

    }

    override fun hideCustomerLoading() {

    }

    private fun setCustomerDropdown(){
        val adapter = context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        }
        customer_dropdown.setAdapter(adapter)
    }

    override fun showPetTypeLoading() {

    }

    override fun hidePetTypeLoading() {
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        val temp: List<PetType> = data?.pettype ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                typeDropdown.add(i, temp[i].name.toString())
                idType.add(i, temp[i].id.toString())
            }
            setTypeDropDown()
        }
    }

    override fun petTypeFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun setTypeDropDown(){
        val adapter = context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item, typeDropdown)
        }
        type_dropdown.setAdapter(adapter)
    }

}
