package com.example.kouveemanagement.customer


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
import com.example.kouveemanagement.adapter.CustomerRecyclerViewAdapter
import com.example.kouveemanagement.model.Customer
import com.example.kouveemanagement.model.CustomerResponse
import com.example.kouveemanagement.presenter.CustomerPresenter
import com.example.kouveemanagement.presenter.CustomerView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_customer.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AllCustomerFragment : Fragment(), CustomerView {

    private var customers: MutableList<Customer> = mutableListOf()
    private lateinit var presenter: CustomerPresenter

    companion object{
        fun newInstance() = AllCustomerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CustomerPresenter(this, Repository())
        presenter.getAllCustomer()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun customerSuccess(data: CustomerResponse?) {

        val temp: List<Customer> = data?.customers ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                customers.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                CustomerRecyclerViewAdapter(customers){
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    override fun customerFailed() {
    }

    private fun showDialog(customer: Customer){

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_customer, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = customer.name.toString()
        address.text = customer.address.toString()
        birthdate.text = customer.birthdate.toString()
        phone_number.text = customer.phone_number.toString()

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Customer Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditCustomerActivity>("customer" to customer)
        }
    }


}
