package com.example.kouveemanagement.orderproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_order_product.*
import org.jetbrains.anko.startActivity

class EditOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView, ProductView {

    private lateinit var presenter: OrderProductPresenter
    private lateinit var orderProduct: OrderProduct
    private lateinit var idOrderProduct: String

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter

    private lateinit var presenterP: ProductPresenter
    private var products: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order_product)
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        orderProduct = OrderProductActivity.orderProduct
        idOrderProduct = orderProduct.id.toString()
        setData(orderProduct)
        presenter = OrderProductPresenter(this, Repository())
        presenterD = DetailOrderProductPresenter(this, Repository())
        presenterD.getDetailOrderProductByOrderId(idOrderProduct)
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        btn_done.setOnClickListener {
            presenter.editDoneOrderProduct(idOrderProduct)
        }
    }

    private fun setData(input : OrderProduct){
        if (input.status.equals("Arrived")){
            btn_done.visibility = View.GONE
        }
        id.setText(input.id.toString())
        status.setText(input.status.toString())
        total.setText(input.total.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    override fun showProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "Empty Product", Toast.LENGTH_SHORT).show()
        }else{
            products.addAll(temp)
            Toast.makeText(this, "Success Product", Toast.LENGTH_SHORT).show()
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed Product", Toast.LENGTH_SHORT).show()
    }

    override fun showOrderProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideOrderProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        Toast.makeText(this, "Order Product Done", Toast.LENGTH_SHORT).show()
        status.setText(data?.orderProducts?.get(0)?.status.toString())
    }

    override fun orderProductFailed() {
        Toast.makeText(this, "Failed Order Product", Toast.LENGTH_SHORT).show()
    }

    override fun showDetailOrderProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailOrderProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        val temp: List<DetailOrderProduct> = data?.detailOrderProducts ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "Empty Detail Order Product", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                detailOrderProducts.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(products, detailOrderProducts){
                Toast.makeText(this, it.id_order, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun detailOrderProductFailed() {
        Toast.makeText(this, "Failed Detail Order Product", Toast.LENGTH_SHORT).show()
    }

}
