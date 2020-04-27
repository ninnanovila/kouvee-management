package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
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

    private lateinit var idProduct: String
    private lateinit var maxStock: String

    companion object{
        fun newInstance() = AddDetailProductTransactionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_detail_product_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idTransaction = AddTransactionActivity.idTransaction
        idProduct = "-1"
        quantity.setText("0")
        btn_add.setOnClickListener {
            if (isValid() && !passStock()){
                getData()
                presenter = DetailProductTransactionPresenter(this, Repository())
                presenter.addDetailProductTransaction(detailProductTransaction)
            }else if (passStock()){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Max stock : $maxStock")
            }else if (idProduct == "-1"){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please choose product")
            }
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
        btn_add.startAnimation()
    }

    override fun hideDetailProductTransactionLoading() {
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        startActivity<AddTransactionActivity>("type" to "product")
    }

    override fun detailProductTransactionFailed(data: String) {
        btn_add.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    private fun setProductDropdown(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ProductTransactionActivity.productNameDropdown)
        product_dropdown.setAdapter(adapter)
        product_dropdown.setOnItemClickListener { _, _, position, _ ->
            idProduct = ProductTransactionActivity.productIdDropdown[position]
            val name = ProductTransactionActivity.productNameDropdown[position]
            Toast.makeText(context, "Product: $name", Toast.LENGTH_LONG).show()
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
