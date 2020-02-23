package com.example.kouveemanagement.supplier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_supplier.*

/**
 * A simple [Fragment] subclass.
 */
class AddSupplierFragment : Fragment(), SupplierView {

    private lateinit var supplier: Supplier
    private lateinit var presenter: SupplierPresenter

    companion object {
        fun newInstance() = AddSupplierFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_supplier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            getData()

            presenter = SupplierPresenter(this, Repository())
            presenter.addSupplier(supplier)
        }
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val phone_number = phone_number.text.toString()
        supplier = Supplier(null, name, address, phone_number, null, null, null)
    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun supplierSuccess(data: SupplierResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun supplierFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }


}
