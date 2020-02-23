package com.example.kouveemanagement.supplier


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.SupplierRcyclerViewAdapter
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_supplier.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AllSupplierFragment : Fragment(), SupplierView {

    private var suppliers: MutableList<Supplier> = mutableListOf()
    private lateinit var presenter: SupplierPresenter

    companion object {
        fun newInstance() = AllSupplierFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_supplier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SupplierPresenter(this, Repository())
        presenter.getAllSupplier()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        val temp: List<Supplier> = data?.suppliers ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                suppliers.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                SupplierRcyclerViewAdapter(suppliers) {
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
            }


        }
    }

    override fun supplierFailed() {
    }

    private fun showDialog(supplier: Supplier){

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_customer, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = supplier.name.toString()
        address.text = supplier.address.toString()
        phone_number.text = supplier.phone_number.toString()

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Supplier Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditSupplierActivity>("supplier" to supplier)
        }
    }


}
