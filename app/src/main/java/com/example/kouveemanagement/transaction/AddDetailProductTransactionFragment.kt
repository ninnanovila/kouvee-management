package com.example.kouveemanagement.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailProductTransaction
import com.example.kouveemanagement.model.DetailProductTransactionResponse
import com.example.kouveemanagement.presenter.DetailProductTransactionPresenter
import com.example.kouveemanagement.presenter.DetailProductTransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_detail_product_transaction.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddDetailProductTransactionFragment : Fragment(), DetailProductTransactionView {

    private lateinit var idTransaction: String
    private lateinit var detailProductTransaction: DetailProductTransaction
    private lateinit var presenter: DetailProductTransactionPresenter

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var idProduct: String

    companion object{
        fun newInstance() = AddDetailProductTransactionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_detail_product_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idTransaction = AddTransactionActivity.idTransaction
        nameDropdown = AddTransactionActivity.nameProductDropdown
        idDropdown = AddTransactionActivity.idProductDropdown
        idProduct = idDropdown[0]
        btn_add.setOnClickListener {
            getData()
            presenter = DetailProductTransactionPresenter(this, Repository())
            presenter.addDetailProductTransaction(detailProductTransaction)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        setProductDropdown()
    }

    fun getData(){
        val quantity = quantity.text.toString()
        detailProductTransaction = DetailProductTransaction(id_transaction = idTransaction, id_product = idProduct, quantity = quantity.toInt())
    }

    override fun showDetailProductTransactionLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailProductTransactionLoading() {
        progressbar.visibility = View.GONE
        btn_add.visibility = View.VISIBLE
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        Toast.makeText(context, "Success Detail Product", Toast.LENGTH_SHORT).show()
        startActivity<AddTransactionActivity>("type" to "product")
    }

    override fun detailProductTransactionFailed() {
        Toast.makeText(context, "Failed Detail Product", Toast.LENGTH_SHORT).show()
    }

    private fun setProductDropdown(){
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        }
        product_dropdown.setAdapter(adapter)
        product_dropdown.setOnItemClickListener { _, _, position, _ ->
            idProduct = idDropdown[position]
            Toast.makeText(context, "ID PRODUCT : $idProduct", Toast.LENGTH_LONG).show()
        }
    }

}
