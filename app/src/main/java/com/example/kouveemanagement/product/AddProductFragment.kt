package com.example.kouveemanagement.product


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.fragment_add_product.btn_add
import kotlinx.android.synthetic.main.fragment_add_product.name
import kotlinx.android.synthetic.main.fragment_add_product.progressbar
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add.setOnClickListener {
            getData()
            presenter = ProductPresenter(this, Repository())
            presenter.addProduct(product)
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

    override fun showProductLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideProductLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_add.visibility = View.VISIBLE
    }

    override fun productSuccess(data: ProductResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ProductManagementActivity>()
    }

    override fun productFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

}
