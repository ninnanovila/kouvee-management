package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_order_product.*
import org.jetbrains.anko.startActivity

class AddOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView, ProductView {

    private lateinit var presenter: OrderProductPresenter

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private lateinit var presenterP: ProductPresenter
    private var products: MutableList<Product> = mutableListOf()

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var supplierId: String
    private lateinit var orderProduct: OrderProduct

    private var state: String = ""

    companion object{
        lateinit var idOrderProduct: String
        var nameProductDropdown: MutableList<String> = arrayListOf()
        var idProductDropdown: MutableList<String> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_product)
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        orderProduct = OrderProductActivity.orderProduct
        idOrderProduct = orderProduct.id.toString()
        nameDropdown = OrderProductActivity.nameDropdown
        idDropdown = OrderProductActivity.idDropdown
        presenter = OrderProductPresenter(this, Repository())
        presenter.editTotalOrderProduct(orderProduct.id.toString())
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
        btn_print.setOnClickListener {
            state = "print"
            presenter.editPrintOrderProduct(orderProduct.id.toString())
        }
        btn_delete.setOnClickListener {
            state = "delete"
            presenter.deleteOrderProduct(orderProduct.id.toString())
        }
        setData()
    }

    private fun setData(){
        id.setText(orderProduct.id.toString())
        status.setText(orderProduct.status.toString())
        supplier.setText(orderProduct.id_supplier.toString())
        total.setText(orderProduct.total.toString())
    }

    override fun showProductLoading() {
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        products.addAll(temp)
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            if (nameProductDropdown.isNotEmpty()){
                nameProductDropdown.clear()
                idProductDropdown.clear()
                for (i in temp.indices){
                    nameProductDropdown.add(i, temp[i].name.toString())
                    idProductDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    nameProductDropdown.add(i, temp[i].name.toString())
                    idProductDropdown.add(i, temp[i].id.toString())
                }
            }
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
        orderProduct = data?.orderProducts?.get(0)!!
        supplier.setText(orderProduct.id_supplier)
        total.setText(orderProduct.total.toString())
        when (state) {
            "print" -> {
                Toast.makeText(this, "Success Print Order Product", Toast.LENGTH_SHORT).show()
                startActivity<OrderProductActivity>()
            }
            "edit" -> {
                startActivity<AddOrderProductActivity>()
            }
            "delete" -> {
                Toast.makeText(this, "Success Delete Order Product", Toast.LENGTH_SHORT).show()
                startActivity<OrderProductActivity>()
            }
            else -> {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        }
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
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                detailOrderProducts.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(products, detailOrderProducts){
                val fragment = EditDetailOrderProductFragment.newInstance(it)
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
                Toast.makeText(this, it.id_order, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun detailOrderProductFailed() {
        Toast.makeText(this, "Failed Detail Order Product", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    private fun showEditSupplier(){
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose, null)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        btnAdd.visibility = View.GONE
        btnSave.visibility = View.VISIBLE

        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            supplierId = idDropdown[position]
            Toast.makeText(this, "ID SUPPLIER : $supplierId", Toast.LENGTH_LONG).show()
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
}
