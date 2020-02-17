package com.example.kouveemanagement.product


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ProductRecyclerViewAdapter
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_product.*

/**
 * A simple [Fragment] subclass.
 */
class AllProductFragment : Fragment(), ProductView {

    private var products : MutableList<Product> = mutableListOf()
    private lateinit var presenter: ProductPresenter

    companion object {
        fun newInstance() = AllProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ProductPresenter(this, Repository())
        presenter.getAllProduct()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                products.add(i, temp.get(i))
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                ProductRecyclerViewAdapter(products){
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun productFailed() {

    }

}
