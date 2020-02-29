package com.example.kouveemanagement.supplier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_supplier.*
import org.jetbrains.anko.startActivity

class EditSupplierActivity : AppCompatActivity(), SupplierView {

    private lateinit var id: String
    private lateinit var presenter: SupplierPresenter
    private lateinit var supplier: Supplier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_supplier)
        setData()
        presenter = SupplierPresenter(this, Repository())
        btn_save.setOnClickListener {
            getData()
            presenter.editSupplier(id, supplier)
        }
        btn_delete.setOnClickListener {
            presenter.deleteSupplier(id)
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
    }

    private fun setData(){
        val supplier: Supplier? = intent.getParcelableExtra("supplier")
        id = supplier?.id.toString()
        name.setText(supplier?.name)
        address.setText(supplier?.address)
        phone_number.setText(supplier?.phone_number)
        created_at.text = supplier?.created_at
        updated_at.text = supplier?.updated_at
        deleted_at.text = supplier?.deleted_at
    }

    private fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val phone_number = phone_number.text.toString()
        supplier = Supplier(id, name, address, phone_number, null, null, null)
    }

    override fun showSupplierLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideSupplierLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<SupplierManagementActivity>()
    }

    override fun supplierFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<SupplierManagementActivity>()
    }
}
