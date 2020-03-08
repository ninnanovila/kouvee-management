package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

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

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var idProduct: String

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
        // Inflate the layout for this fragment
        detailOrderProduct = arguments?.getParcelable("input")!!
        return inflater.inflate(R.layout.fragment_edit_detail_order_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(detailOrderProduct)
        presenter = DetailOrderProductPresenter(this, Repository())
        idOrderProduct = detailOrderProduct.id_order.toString()
        idProduct = detailOrderProduct.id_product.toString()
        btn_save.setOnClickListener {
            state = "edit"
            getData()
            presenter.editDetailOrderProduct(detailOrderProduct)
        }
        btn_delete.setOnClickListener {
            state = "delete"
            presenter.deleteDetailOrderProduct(idOrderProduct, idProduct)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun setDropdown(detailOrderProduct: DetailOrderProduct){
        nameDropdown = AddOrderProductActivity.nameProductDropdown
        idDropdown = AddOrderProductActivity.idProductDropdown
        for (i in idDropdown.indices){
            if (idDropdown[i] == detailOrderProduct.id_product){
                product.setText(nameDropdown[i])
            }
        }
    }

    private fun setData(detailOrderProduct: DetailOrderProduct){
        setDropdown(detailOrderProduct)
        quantity.setText(detailOrderProduct.quantity.toString())
    }

    private fun getData(){
        val quantity = quantity.text.toString()
        detailOrderProduct = DetailOrderProduct(idOrderProduct, idProduct, quantity.toInt())
    }

    override fun showDetailOrderProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailOrderProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        if (state == "edit"){
            Toast.makeText(context, "Success to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Success to delete", Toast.LENGTH_SHORT).show()
        }
        startActivity<AddOrderProductActivity>()
    }

    override fun detailOrderProductFailed() {
        if (state == "edit"){
            Toast.makeText(context, "Failed to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }

}
