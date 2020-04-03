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
            if (isValid()){
                getData()
                presenter.editSupplier(id, supplier)
            }
        }
        btn_cancel.setOnClickListener {
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
        created_at.setText(supplier?.created_at)
        updated_at.setText(supplier?.updated_at)
        if (supplier?.deleted_at.isNullOrBlank()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(supplier?.deleted_at)
        }
    }

    private fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val phoneNumber = phone_number.text.toString()
        supplier = Supplier(id, name, address, phoneNumber, null, null, null)
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (address.text.isNullOrEmpty()){
            address.error = getString(R.string.error_address)
            return false
        }
        if (phone_number.text.isNullOrEmpty()){
            phone_number.error = getString(R.string.error_phone_number)
            return false
        }
        return true
    }

    override fun showSupplierLoading() {
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideSupplierLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<SupplierManagementActivity>()
    }

    override fun supplierFailed() {
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<SupplierManagementActivity>()
    }
}
