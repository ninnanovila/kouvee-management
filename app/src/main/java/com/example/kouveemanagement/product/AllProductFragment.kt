package com.example.kouveemanagement.product


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ProductRecyclerViewAdapter
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_detail_product.*
import kotlinx.android.synthetic.main.fragment_all_product.*
import org.jetbrains.anko.support.v4.startActivity

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

            recyclerview.layoutManager = GridLayoutManager(context, 2)
            recyclerview.adapter = context?.let {
                ProductRecyclerViewAdapter(products){
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun productFailed() {

    }

    private fun showDialog(product: Product){

        val base_url = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_product, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val unit = dialog.findViewById<TextView>(R.id.unit)
        val stock = dialog.findViewById<TextView>(R.id.stock)
        val min_stock = dialog.findViewById<TextView>(R.id.min_stock)
        val price = dialog.findViewById<TextView>(R.id.price)
        val photo = dialog.findViewById<ImageView>(R.id.photo)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = product.name.toString()
        unit.text = product.unit.toString()
        stock.text = product.stock.toString()
        min_stock.text = product.min_stock.toString()
        price.text = product.price.toString()
        product.photo.let { Picasso.get().load(base_url+product.photo.toString()).fit().into(photo) }

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Product Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditProductActivity>("product" to product)
        }

    }

}
