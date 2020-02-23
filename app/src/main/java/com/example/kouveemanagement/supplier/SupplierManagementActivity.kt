package com.example.kouveemanagement.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_management)

        presenter = SupplierPresenter(this, Repository())
        presenter.getAllSupplier()

        fab_add.setOnClickListener {
            val fragment: Fragment = AddSupplierFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }

    }

    override fun showLoading() {

    }

    override fun hideLoading() {
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
    }

    private fun showDialog(supplier: Supplier){

        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_customer, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = supplier.name.toString()
        address.text = supplier.address.toString()
        phone_number.text = supplier.phone_number.toString()

        AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Supplier Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditSupplierActivity>("supplier" to supplier)
        }
    }


}
