package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.DetailOrderProductResponse
import com.example.kouveemanagement.presenter.DetailOrderProductPresenter
import com.example.kouveemanagement.presenter.DetailOrderProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_detail_order_product.*
import kotlinx.android.synthetic.main.item_detail_order_product.quantity
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddDetailOrderProductFragment : Fragment(), DetailOrderProductView {

    private lateinit var idOrderProduct: String
    private lateinit var detailOrderProduct: DetailOrderProduct
    private lateinit var presenter: DetailOrderProductPresenter

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
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
        idOrderProduct = AddOrderProductActivity.idOrderProduct
        nameDropdown = AddOrderProductActivity.nameProductDropdown
        idDropdown = AddOrderProductActivity.idProductDropdown
        idProduct = idDropdown[0]
        btn_add.setOnClickListener {
            getData()
            presenter = DetailOrderProductPresenter(this, Repository())
            presenter.addDetailOrderProduct(detailOrderProduct)
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

    override fun showDetailOrderProductLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailOrderProductLoading() {
        progressbar.visibility = View.GONE
        btn_add.visibility = View.VISIBLE
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<AddOrderProductActivity>()
    }

    override fun detailOrderProductFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
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
