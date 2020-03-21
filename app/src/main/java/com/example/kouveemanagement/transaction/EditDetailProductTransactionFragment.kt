package com.example.kouveemanagement.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailProductTransaction
import com.example.kouveemanagement.model.DetailProductTransactionResponse
import com.example.kouveemanagement.presenter.DetailProductTransactionPresenter
import com.example.kouveemanagement.presenter.DetailProductTransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_edit_detail_product_transaction.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class EditDetailProductTransactionFragment : Fragment(), DetailProductTransactionView {

    private lateinit var idTransaction: String
    private lateinit var detailProductTransaction: DetailProductTransaction
    private lateinit var presenter: DetailProductTransactionPresenter

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var idProduct: String

    private var state = "edit"

    companion object{
        private const val detailProductTransaction = "input"
        fun newInstance(input: DetailProductTransaction) : EditDetailProductTransactionFragment{
            val fragment = EditDetailProductTransactionFragment()
            val bundle = Bundle().apply {
                putParcelable(detailProductTransaction, input)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailProductTransaction = arguments?.getParcelable("input")!!
        return inflater.inflate(R.layout.fragment_edit_detail_product_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(detailProductTransaction)
        presenter = DetailProductTransactionPresenter(this, Repository())
        idTransaction = detailProductTransaction.id_transaction.toString()
        idProduct = detailProductTransaction.id_product.toString()
        btn_edit.setOnClickListener {
            quantity.isEnabled = true
            btn_edit.visibility = View.GONE
            btn_save.visibility = View.VISIBLE
            btn_delete.visibility = View.VISIBLE
        }
        btn_save.setOnClickListener {
            if (isValid()){
                state = "edit"
                getData()
                presenter.editDetailProductTransaction(detailProductTransaction)
            }
        }
        btn_delete.setOnClickListener {
            state = "delete"
            presenter.deleteDetailProductTransaction(idTransaction,idProduct)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun setDropdown(detailProductTransaction: DetailProductTransaction){
        nameDropdown = AddTransactionActivity.nameProductDropdown
        idDropdown = AddTransactionActivity.idProductDropdown
        for (i in idDropdown.indices){
            if (idDropdown[i] == detailProductTransaction.id_product){
                product.setText(nameDropdown[i])
            }
        }
    }

    private fun setData(detailProductTransaction: DetailProductTransaction){
        setDropdown(detailProductTransaction)
        quantity.setText(detailProductTransaction.quantity.toString())
    }

    private fun getData(){
        val quantity = quantity.text.toString()
        detailProductTransaction = DetailProductTransaction(idTransaction, idProduct, quantity = quantity.toInt())
    }

    private fun isValid(): Boolean {
        if(quantity.text.isNullOrEmpty()){
            quantity.error = getString(R.string.error_quantity)
            return false
        }else if (quantity.text.toString() == "0"){
            quantity.error = getString(R.string.error_zero_quantity)
            return false
        }
        return true
    }

    override fun showDetailProductTransactionLoading() {
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailProductTransactionLoading() {
        progressbar.visibility = View.GONE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        if (state == "edit"){
            Toast.makeText(context, "Success to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Success to delete", Toast.LENGTH_SHORT).show()
        }
        startActivity<AddTransactionActivity>("type" to "product")
    }

    override fun detailProductTransactionFailed() {
        if (state == "edit"){
            Toast.makeText(context, "Failed to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }

}
