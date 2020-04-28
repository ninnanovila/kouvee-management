package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
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

    private lateinit var idProduct: String
    private lateinit var maxStock: String

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
            btn_cancel.visibility = View.VISIBLE
        }
        btn_save.setOnClickListener {
            if (isValid() && !passStock()){
                state = "edit"
                getData()
                presenter.editDetailProductTransaction(detailProductTransaction)
            }else if (passStock()){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Max stock : $maxStock")
            }
        }
        btn_cancel.setOnClickListener {
            state = "delete"
            presenter.deleteDetailProductTransaction(idTransaction,idProduct)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun setDropdown(detailProductTransaction: DetailProductTransaction){
        for (i in ProductTransactionActivity.productId.indices){
            if (ProductTransactionActivity.productId[i] == detailProductTransaction.id_product){
                product.setText(ProductTransactionActivity.productName[i])
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
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideDetailProductTransactionLoading() {
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        startActivity<AddTransactionActivity>("type" to "product")
    }

    override fun detailProductTransactionFailed(data: String) {
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        if (state == "edit"){
            CustomFun.failedSnackBar(requireView(), requireContext(), data)
        }else if (state == "delete"){
            CustomFun.failedSnackBar(requireView(), requireContext(), data)
        }
    }

    private fun passStock() : Boolean{
        val products = ProductTransactionActivity.products
        for (i in products.indices){
            if (idProduct == products[i].id){
                maxStock = products[i].stock.toString()
                if (quantity.text.toString().toInt() > products[i].stock.toString().toInt()){
                    return true
                }
            }
        }
        return false
    }

}
