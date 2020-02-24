package com.example.kouveemanagement.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.Animation
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.SupplierRcyclerViewAdapter
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_supplier_management.*
import org.jetbrains.anko.startActivity


class SupplierManagementActivity : AppCompatActivity(), SupplierView {

    private var suppliers: MutableList<Supplier> = mutableListOf()
    private lateinit var presenter: SupplierPresenter

    private lateinit var dialog: View
    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_management)

        presenter = SupplierPresenter(this, Repository())
        presenter.getAllSupplier()

        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }

        fabAnimation()
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun supplierSuccess(data: SupplierResponse?) {

        val temp: List<Supplier> = data?.suppliers ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                suppliers.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = this.let {
                SupplierRcyclerViewAdapter(suppliers) {
                    showDialog(it)
                    Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun supplierFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(supplier: Supplier){

        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_supplier, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = supplier.name.toString()
        address.text = supplier.address.toString()
        phone_number.text = supplier.phone_number.toString()

        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditSupplierActivity>("supplier" to supplier)
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
            val fragment: Fragment = AddSupplierFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }


}
