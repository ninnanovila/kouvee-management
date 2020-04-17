package com.example.kouveemanagement.orderproduct

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomView
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.DetailOrderProductResponse
import com.example.kouveemanagement.model.OrderProduct
import com.example.kouveemanagement.model.OrderProductResponse
import com.example.kouveemanagement.presenter.DetailOrderProductPresenter
import com.example.kouveemanagement.presenter.DetailOrderProductView
import com.example.kouveemanagement.presenter.OrderProductPresenter
import com.example.kouveemanagement.presenter.OrderProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_order_product.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AddOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView {

    private lateinit var presenter: OrderProductPresenter

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter
    private lateinit var detailAdapter: DetailOrderProductRecyclerViewAdapter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private lateinit var supplierId: String
    private lateinit var orderProduct: OrderProduct

    private var state: String = ""

    companion object{
        lateinit var idOrderProduct: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_product)
        orderProduct = OrderProductActivity.orderProduct
        idOrderProduct = orderProduct.id.toString()
        presenter = OrderProductPresenter(this, Repository())
        presenter.editTotalOrderProduct(orderProduct.id.toString())
        detailAdapter = DetailOrderProductRecyclerViewAdapter(OrderProductActivity.products, detailOrderProducts){}
        presenterD = DetailOrderProductPresenter(this, Repository())
        presenterD.getDetailOrderProductByOrderId(orderProduct.id.toString())
        fab_add.setOnClickListener {
            val fragment: Fragment = AddDetailOrderProductFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        btn_edit.setOnClickListener {
            state = "edit"
            showEditSupplier()
        }
        btn_status.setOnClickListener {
            state = "print"
            alertDialog()
        }
        btn_cancel.setOnClickListener {
            state = "delete"
            alertDialog()
        }
        setData(orderProduct)
        swipe_rv.setOnRefreshListener {
            presenterD.getDetailOrderProductByOrderId(orderProduct.id.toString())
        }
        CustomView.setSwipe(swipe_rv)
    }

    private fun setData(input: OrderProduct){
        id.text = input.id.toString()
        status.text = input.status.toString()
        for (i in OrderProductActivity.supplierNameDropdown.indices){
            if (input.id_supplier == OrderProductActivity.supplierIdDropdown[i]){
                supplier.text = OrderProductActivity.supplierNameDropdown[i]
            }
        }
        val totalInput = input.total.toString()
        total.text = "Rp. $totalInput"
    }

    override fun showOrderProductLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideOrderProductLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        orderProduct = data?.orderProducts?.get(0)!!
        setData(orderProduct)
        when (state) {
            "print" -> {
                startActivity<OrderProductActivity>()
            }
            "edit" -> {
                CustomView.successSnackBar(container, baseContext, "Success edit supplier")
                startActivity<AddOrderProductActivity>()
            }
            "delete" -> {
                startActivity<OrderProductActivity>()
            }
            else -> {
                CustomView.successSnackBar(container, baseContext, "Ok, success")
            }
        }
    }

    override fun orderProductFailed(data: String) {
        CustomView.failedSnackBar(container, baseContext, data)
    }

    override fun showDetailOrderProductLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideDetailOrderProductLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun detailOrderProductSuccess(data: DetailOrderProductResponse?) {
        val temp: List<DetailOrderProduct> = data?.detailOrderProducts ?: emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Empty detail")
        }else{
            clearDetail()
            detailOrderProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(OrderProductActivity.products, detailOrderProducts){
                val fragment = EditDetailOrderProductFragment.newInstance(it)
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }
            CustomView.successSnackBar(container, baseContext, "Detail success")
        }
    }

    override fun detailOrderProductFailed(data: String) {
        CustomView.failedSnackBar(container, baseContext, data)
    }

    private fun clearDetail(){
        detailOrderProducts.clear()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    private fun showEditSupplier(){
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose, null)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, OrderProductActivity.supplierNameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        btnAdd.visibility = View.GONE
        btnSave.visibility = View.VISIBLE

        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            supplierId = OrderProductActivity.supplierIdDropdown[position]
            val name = OrderProductActivity.supplierNameDropdown[position]
            Toast.makeText(this, "Supplier : $name", Toast.LENGTH_LONG).show()
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnSave.setOnClickListener {
            state = "edit"
            val newOrderProduct = OrderProduct(orderProduct.id.toString(), supplierId, orderProduct.total, orderProduct.status)
            presenter.editOrderProduct(orderProduct.id.toString(), newOrderProduct)
        }
    }

    private fun alertDialog(){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to $state this order ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                if (state == "print"){
                    presenter.editPrintOrderProduct(orderProduct.id.toString())
                }else{
                    presenter.deleteOrderProduct(orderProduct.id.toString())
                }
            }
            .setNegativeButton("NO"){ _: DialogInterface, _: Int ->
                CustomView.warningSnackBar(container, baseContext, "Process canceled")
            }
            .show()
    }

}
