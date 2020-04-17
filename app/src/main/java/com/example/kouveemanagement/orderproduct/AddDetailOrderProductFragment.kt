package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.DetailOrderProductResponse
import com.example.kouveemanagement.presenter.DetailOrderProductPresenter
import com.example.kouveemanagement.presenter.DetailOrderProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_detail_order_product.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddDetailOrderProductFragment : Fragment(), DetailOrderProductView {

    private lateinit var idOrderProduct: String
    private lateinit var detailOrderProduct: DetailOrderProduct
    private lateinit var presenter: DetailOrderProductPresenter

    private lateinit var idProduct: String

    companion object {
        fun newInstance() = AddDetailOrderProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_detail_order_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.bringToFront()
        idOrderProduct = AddOrderProductActivity.idOrderProduct
        idProduct = OrderProductActivity.productIdDropdown[0]
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = DetailOrderProductPresenter(this, Repository())
                presenter.addDetailOrderProduct(detailOrderProduct)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        setProductDropdown()
    }

    fun getData(){
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
        btn_add.startAnimation()
    }

    override fun hideDetailOrderProductLoading() {
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        startActivity<AddOrderProductActivity>()
    }

    override fun detailOrderProductFailed(data: String) {
        btn_add.revertAnimation()
        context?.let { view?.let { itView -> CustomView.failedSnackBar(itView, it, data) } }
    }

    private fun setProductDropdown(){
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, OrderProductActivity.productNameDropdown)
        }
        product_dropdown.setAdapter(adapter)
        product_dropdown.setOnItemClickListener { _, _, position, _ ->
            idProduct = OrderProductActivity.productIdDropdown[position]
            val name = OrderProductActivity.productNameDropdown[position]
            Toast.makeText(context, "Product : $name", Toast.LENGTH_LONG).show()
        }
    }

}
