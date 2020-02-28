package com.example.kouveemanagement.customerpet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.*
import com.example.kouveemanagement.adapter.CustomerPetRecyclerViewAdapter
import com.example.kouveemanagement.model.CustomerPet
import com.example.kouveemanagement.model.CustomerPetResponse
import com.example.kouveemanagement.presenter.CustomerPetPresenter
import com.example.kouveemanagement.presenter.CustomerPetView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_customer_pet_management.*
import org.jetbrains.anko.startActivity

class CustomerPetManagementActivity : AppCompatActivity(), CustomerPetView {

    private var customerpet: MutableList<CustomerPet> = mutableListOf()
    private lateinit var presenter: CustomerPetPresenter

    private lateinit var dialog: View
    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_pet_management)
        presenter = CustomerPetPresenter(this, Repository())
        presenter.getAllCustomerPet()
        btn_home.setOnClickListener {
            startActivity<CustomerServiceActivity>()
        }
        fabAnimation()
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun customerPetSuccess(data: CustomerPetResponse?) {
        val temp: List<CustomerPet> = data?.customerpets ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                customerpet.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = CustomerPetRecyclerViewAdapter(customerpet){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun customerPetFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(customerPet: CustomerPet){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_customer_pet, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = customerPet.name.toString()
        birthdate.text = customerPet.birthdate.toString()

        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditCustometPetActivity>("customerpet" to customerPet)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
            val fragment: Fragment = AddCustomerPetFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<CustomerServiceActivity>()
    }
}
