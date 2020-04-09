package com.example.kouveemanagement.customerpet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.*
import com.example.kouveemanagement.adapter.CustomerPetRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_customer_pet_management.*
import org.jetbrains.anko.startActivity

class CustomerPetManagementActivity : AppCompatActivity(), CustomerPetView, CustomerView, PetTypeView {

    private lateinit var customerPetAdapter: CustomerPetRecyclerViewAdapter
    private lateinit var presenter: CustomerPetPresenter

    private var customerPetsList: MutableList<CustomerPet> = mutableListOf()
    private val customerPetsTemp = ArrayList<CustomerPet>()
    private var temps = ArrayList<CustomerPet>()

    private lateinit var dialog: View

    private lateinit var presenterC: CustomerPresenter
    private lateinit var presenterT: PetTypePresenter
    private var petTypes: MutableList<PetType> = mutableListOf()

    companion object{
        var nameCustomerDropdown: MutableList<String> = arrayListOf()
        var idCustomerList: MutableList<String> = arrayListOf()
        var nameTypeDropdown: MutableList<String> = arrayListOf()
        var idTypeList: MutableList<String> = arrayListOf()
        var customerPets: MutableList<CustomerPet> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_pet_management)
        presenterT = PetTypePresenter(this, Repository())
        presenterT.getAllPetType()
        presenterC = CustomerPresenter(this, Repository())
        presenterC.getAllCustomer()
        presenter = CustomerPetPresenter(this, Repository())
        presenter.getAllCustomerPet()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        customerPetAdapter = CustomerPetRecyclerViewAdapter(petTypes, customerPetsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = CustomerPetRecyclerViewAdapter(petTypes, customerPets){
                    showDialog(it)
                }
                query?.let { customerPetAdapter.filterCustomerPet(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = CustomerPetRecyclerViewAdapter(petTypes, customerPets){
                    showDialog(it)
                }
                newText?.let { customerPetAdapter.filterCustomerPet(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddCustomerPetFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_all.setOnClickListener {
            temps = customerPetsTemp
            getList()
        }
        show_en.setOnClickListener {
            val filtered = customerPetsTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<CustomerPet>
            getList()
        }
        show_dis.setOnClickListener {
            val filtered = customerPetsTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<CustomerPet>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenter.getAllCustomerPet()
        }
        CustomView.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (sort_switch.isChecked){
            val sorted = temps.sortedBy { it.name }
            recyclerview.adapter = CustomerPetRecyclerViewAdapter(petTypes, sorted as MutableList<CustomerPet>){
                showDialog(it)
            }
        }else{
            recyclerview.adapter = CustomerPetRecyclerViewAdapter(petTypes, temps){
                showDialog(it)
            }
        }
        customerPetAdapter.notifyDataSetChanged()
    }

    override fun showPetTypeLoading() {
    }

    override fun hidePetTypeLoading() {
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        val temp: List<PetType> = data?.pettype ?: emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Pet Type empty")
        }else{
            nameTypeDropdown.clear()
            idTypeList.clear()
            petTypes.clear()
            petTypes.addAll(temp)
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    nameTypeDropdown.add(temp[i].name.toString())
                    idTypeList.add(temp[i].id.toString())
                }
            }
        }
    }

    override fun petTypeFailed() {
        CustomView.failedSnackBar(container, baseContext, "Pet Type failed")
    }

    override fun showCustomerPetLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideCustomerPetLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        val temp: List<CustomerPet> = data?.customerpets ?: emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Oops, no result")
        }else{
            clearList()
            customerPetsList.addAll(temp)
            customerPetsTemp.addAll(temp)
            temps.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = CustomerPetRecyclerViewAdapter(petTypes, customerPetsList){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
            CustomView.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun customerPetFailed() {
        CustomView.failedSnackBar(container, baseContext, "Oops, failed")
    }

    private fun clearList(){
        customerPetsList.clear()
        customerPetsTemp.clear()
        temps.clear()
    }

    private fun showDialog(customerPet: CustomerPet){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_customer_pet, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val birthday = dialog.findViewById<TextView>(R.id.birthdate)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = customerPet.name.toString()
        birthday.text = customerPet.birthdate.toString()

        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        if (customerPet.deleted_at != null){
            btnEdit.visibility = View.GONE
        }

        btnEdit.setOnClickListener {
            startActivity<EditCustomerPetActivity>("customerpet" to customerPet)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }

    override fun showCustomerLoading() {
    }

    override fun hideCustomerLoading() {
    }

    override fun customerSuccess(data: CustomerResponse?) {
        val temp: List<Customer> = data?.customers ?: emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Customer empty")
        }else{
            nameCustomerDropdown.clear()
            idCustomerList.clear()
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    nameCustomerDropdown.add(temp[i].name.toString())
                    idCustomerList.add(temp[i].id.toString())
                }
            }
        }
    }

    override fun customerFailed() {
        CustomView.failedSnackBar(container, baseContext, "Customer failed")
    }
}
