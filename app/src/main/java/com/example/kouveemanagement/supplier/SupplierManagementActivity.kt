package com.example.kouveemanagement.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.SupplierRecyclerViewAdapter
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_supplier_management.*
import org.jetbrains.anko.startActivity


class SupplierManagementActivity : AppCompatActivity(), SupplierView {

    private var suppliersList: MutableList<Supplier> = mutableListOf()
    private val suppliersTemp = ArrayList<Supplier>()

    private lateinit var supplierAdapter: SupplierRecyclerViewAdapter
    private lateinit var presenter: SupplierPresenter

    private lateinit var dialog: View

    companion object{
        var suppliers: MutableList<Supplier> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_management)
        presenter = SupplierPresenter(this, Repository())
        presenter.getAllSupplier()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        supplierAdapter = SupplierRecyclerViewAdapter(suppliersList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = SupplierRecyclerViewAdapter(suppliers){
                    showDialog(it)
                }
                query?.let { supplierAdapter.filterSupplier(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = SupplierRecyclerViewAdapter(suppliers){
                    showDialog(it)
                }
                newText?.let { supplierAdapter.filterSupplier(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddSupplierFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        sort_name.setOnClickListener {
            val sorted = suppliersTemp.sortedBy { it.name }
            recyclerview.adapter = SupplierRecyclerViewAdapter(sorted as MutableList<Supplier>){
                showDialog(it)
            }
            supplierAdapter.notifyDataSetChanged()
        }
        show_all.setOnClickListener {
            recyclerview.adapter = SupplierRecyclerViewAdapter(suppliersList){
                showDialog(it)
            }
            supplierAdapter.notifyDataSetChanged()
        }
        show_en.setOnClickListener {
            val filtered = suppliersTemp.filter { it.deleted_at === null }
            recyclerview.adapter = SupplierRecyclerViewAdapter(filtered as MutableList<Supplier>){
                showDialog(it)
            }
            supplierAdapter.notifyDataSetChanged()
        }
        show_dis.setOnClickListener {
            val filtered = suppliersTemp.filter { it.deleted_at !== null }
            recyclerview.adapter = SupplierRecyclerViewAdapter(filtered as MutableList<Supplier>){
                showDialog(it)
            }
            supplierAdapter.notifyDataSetChanged()
        }
    }

    override fun showSupplierLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideSupplierLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        val temp: List<Supplier> = data?.suppliers ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            suppliersList.addAll(temp)
            suppliersTemp.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = SupplierRecyclerViewAdapter(suppliersList) {
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
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
        val phoneNumber = dialog.findViewById<TextView>(R.id.phone_number)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = supplier.name.toString()
        address.text = supplier.address.toString()
        phoneNumber.text = supplier.phone_number.toString()
        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()
        btnEdit.setOnClickListener {
            startActivity<EditSupplierActivity>("supplier" to supplier)
        }
        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }


}
