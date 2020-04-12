package com.example.kouveemanagement.orderproduct

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomView
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_order_product.*
import org.jetbrains.anko.startActivity

class EditOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView {

    private lateinit var presenter: OrderProductPresenter
    private lateinit var orderProduct: OrderProduct
    private lateinit var idOrderProduct: String

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order_product)
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
            alertDialog()
        }
    }

    private fun setData(input : OrderProduct){
        if (input.status.equals("Arrived")){
            btn_done.visibility = View.GONE
        }
        id.text = input.id.toString()
        for (i in OrderProductActivity.supplierNameDropdown.indices){
            if (input.id_supplier == OrderProductActivity.supplierIdDropdown[i]){
                supplier.text = OrderProductActivity.supplierNameDropdown[i]
            }
        }
        status.text = input.status.toString()
        total.text = input.total.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    override fun showOrderProductLoading() {
        btn_done.startAnimation()
    }

    override fun hideOrderProductLoading() {
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        status.text = data?.orderProducts?.get(0)?.status.toString()
        startActivity<OrderProductActivity>()
    }

    override fun orderProductFailed() {
        btn_done.revertAnimation()
        CustomView.failedSnackBar(container, baseContext, "Oops, try again")
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
            CustomView.neutralSnackBar(container, baseContext, "Detail empty")
        }else{
            detailOrderProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(OrderProductActivity.products, detailOrderProducts){
                Toast.makeText(this, it.id_order, Toast.LENGTH_LONG).show()
            }
            CustomView.successSnackBar(container, baseContext, "Detail success")
        }
    }

    override fun detailOrderProductFailed() {
        CustomView.failedSnackBar(container, baseContext, "Detail failed")
    }

    private fun alertDialog(){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to done this order ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                presenter.editDoneOrderProduct(idOrderProduct)
            }
            .setNegativeButton("NO"){ _: DialogInterface, _: Int ->
                CustomView.warningSnackBar(container, baseContext, "Process canceled")
            }
            .show()
    }
}
