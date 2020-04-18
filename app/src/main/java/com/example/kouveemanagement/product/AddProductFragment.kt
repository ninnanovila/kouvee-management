package com.example.kouveemanagement.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_product.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddProductFragment : Fragment(), ProductView {

    private lateinit var product: Product
    private lateinit var presenter: ProductPresenter

    companion object {
        fun newInstance() = AddProductFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = ProductPresenter(this, Repository())
                presenter.addProduct(product)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    fun getData(){
        val name = name.text.toString()
        val unit = unit.text.toString()
        val stock = stock.text.toString()
        val minStock = min_stock.text.toString()
        val price = price.text.toString()
        product = Product(null, name, unit, stock.toInt(), minStock.toInt(), price.toDouble(), null)
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (unit.text.isNullOrEmpty()){
            unit.error = getString(R.string.error_unit)
            return false
        }
        if (stock.text.isNullOrEmpty() || stock.text.toString().toInt() < 1){
            stock.error = getString(R.string.error_stock)
            return false
        }
        if (min_stock.text.isNullOrEmpty() || min_stock.text.toString().toInt() < 1){
            min_stock.error = getString(R.string.error_min_stock)
            return false
        }
        if (price.text.isNullOrEmpty() || price.text.toString().toInt() < 1){
            price.error = getString(R.string.error_price)
            return false
        }
        return true
    }

    override fun showProductLoading() {
        btn_add.startAnimation()
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        startActivity<ProductManagementActivity>()
    }

    override fun productFailed(data: String) {
        btn_add.revertAnimation()
        context?.let { view?.let { itView -> CustomFun.failedSnackBar(itView, it, data) } }
    }

}
