package com.example.kouveemanagement.supplier


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_supplier.*
import org.jetbrains.anko.support.v4.startActivity


/**
 * A simple [Fragment] subclass.
 */
class AddSupplierFragment : Fragment(), SupplierView {

    private lateinit var supplier: Supplier
    private lateinit var presenter: SupplierPresenter

    companion object {
        fun newInstance() = AddSupplierFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_supplier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = SupplierPresenter(this, Repository())
                presenter.addSupplier(supplier)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    fun getData(){
        val name = name.text.toString()
        val address = address.text.toString()
        val phoneNumber = phone_number.text.toString()
        supplier = Supplier(null, name, address, phoneNumber, null, null, null)
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
        btn_add.startAnimation()
    }

    override fun hideSupplierLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        startActivity<SupplierManagementActivity>()
    }

    override fun supplierFailed(data: String) {
        btn_add.revertAnimation()
        context?.let { view?.let { itView -> CustomFun.failedSnackBar(itView, it, data) } }
    }

}
