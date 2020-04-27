package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.DetailOrderProductResponse
import com.example.kouveemanagement.presenter.DetailOrderProductPresenter
import com.example.kouveemanagement.presenter.DetailOrderProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_edit_detail_order_product.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class EditDetailOrderProductFragment : Fragment(), DetailOrderProductView {

    private lateinit var idOrderProduct: String
    private lateinit var detailOrderProduct: DetailOrderProduct
    private lateinit var presenter: DetailOrderProductPresenter

    private lateinit var idProduct: String
    private lateinit var minStock: String

    private var state = "edit"

    companion object{
        private const val detailOrderProduct = "input"
        fun newInstance(input: DetailOrderProduct) : EditDetailOrderProductFragment{
            val fragment = EditDetailOrderProductFragment()
            val bundle = Bundle().apply {
                putParcelable(detailOrderProduct, input)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailOrderProduct = arguments?.getParcelable("input")!!
        return inflater.inflate(R.layout.fragment_edit_detail_order_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(detailOrderProduct)
        presenter = DetailOrderProductPresenter(this, Repository())
        idOrderProduct = detailOrderProduct.id_order.toString()
        idProduct = detailOrderProduct.id_product.toString()
        quantity.setText("0")
        btn_edit.setOnClickListener {
            btn_edit.visibility = View.GONE
            btn_save.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE
            quantity.isEnabled = true
        }
        btn_save.setOnClickListener {
            if (isValid() && passQuantity()){
                state = "edit"
                getData()
                presenter.editDetailOrderProduct(detailOrderProduct)
            }else if(!passQuantity()){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Min stock : $minStock")
            }
        }
        btn_cancel.setOnClickListener {
            state = "delete"
            presenter.deleteDetailOrderProduct(idOrderProduct, idProduct)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun setDropdown(detailOrderProduct: DetailOrderProduct){
        for (i in OrderProductActivity.productIdDropdown.indices){
            if (OrderProductActivity.productIdDropdown[i] == detailOrderProduct.id_product){
                product.setText(OrderProductActivity.productNameDropdown[i])
            }
        }
    }

    private fun setData(detailOrderProduct: DetailOrderProduct){
        setDropdown(detailOrderProduct)
        quantity.setText(detailOrderProduct.quantity.toString())
        quantity.isEnabled = false
    }

    private fun getData(){
        val quantity = quantity.text.toString()
        detailOrderProduct = DetailOrderProduct(idOrderProduct, idProduct, quantity.toInt())
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

    override fun showDetailOrderProductLoading() {
        btn_edit.visibility = View.INVISIBLE
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideDetailOrderProductLoading() {
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        startActivity<AddOrderProductActivity>()
    }

    override fun detailOrderProductFailed(data: String) {
        btn_edit.visibility = View.GONE
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        if (state == "edit"){
            CustomFun.failedSnackBar(requireView(), requireContext(), data)
        }else if (state == "delete"){
            CustomFun.failedSnackBar(requireView(), requireContext(), data)
        }
    }

    private fun passQuantity() : Boolean{
        var range = 0
        val products = OrderProductActivity.products
        for (product in products){
            if (idProduct == product.id){
                range = product.min_stock.toString().toInt() - product.stock.toString().toInt()
                minStock = range.toString()
                if (quantity.text.toString().toInt() < range){
                    return false
                }
            }
        }
        return true
    }

}
