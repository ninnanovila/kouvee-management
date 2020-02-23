package com.example.kouveemanagement.supplier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var last_emp: String
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
    }

    private fun setData(){
        val supplier: Supplier? = intent.getParcelableExtra("supplier")
        id = supplier?.id.toString()
        last_emp = "0"
        name.setText(supplier?.name)
        address.setText(supplier?.address)
        phone_number.setText(supplier?.phone_number)
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val phone_number = phone_number.text.toString()

        supplier = Supplier(id, name, address, phone_number, null, null, null)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<SupplierManagementActivity>()
    }

    override fun supplierFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }
}
