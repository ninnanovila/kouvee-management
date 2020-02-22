package com.example.kouveemanagement.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_product.*

class EditProductActivity : AppCompatActivity(), ProductView {

    private lateinit var id: String
    private lateinit var presenter: ProductPresenter
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        setData()

        btn_save.setOnClickListener {
            getData()
            presenter = ProductPresenter(this, Repository())
            presenter.editProduct(id, product)
        }

        btn_delete.setOnClickListener {
            presenter = ProductPresenter(this, Repository())
            presenter.deleteProduct(id)
        }

        btn_choose.setOnClickListener {
//            IMAGE
        }
    }

    private fun setData(){

        val base_url = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"

        val product: Product? = intent.getParcelableExtra("product")
        id = product?.id.toString()

        name.setText(product?.name)
        unit.setText(product?.unit)
        product?.stock?.toString()?.let { stock.setText(it) }
        product?.min_stock?.toString()?.let { min_stock.setText(it) }
        product?.price?.toString()?.let { price.setText(it) }

        product?.photo.let { Picasso.get().load(base_url+product?.photo.toString()).fit().into(image_product) }
    }

    private fun getData(){
        val name = name.text.toString()
        val unit = unit.text.toString()
        val stock = stock.text.toString()
        val minStock = min_stock.text.toString()
        val price = price.text.toString()

        product = Product(id, name, unit, stock.toInt(), minStock.toInt(), price.toDouble(), null)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun productSuccess(data: ProductResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }


}
