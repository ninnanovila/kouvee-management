package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.OrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_order_product.*
import org.jetbrains.anko.startActivity

class OrderProductActivity : AppCompatActivity(), OrderProductView, SupplierView, ProductView {

    private var orderProductsList: MutableList<OrderProduct> = mutableListOf()
    private val orderProductsTemp = ArrayList<OrderProduct>()
    private var temps = ArrayList<OrderProduct>()

    private lateinit var orderAdapter: OrderProductRecyclerViewAdapter
    private lateinit var presenter: OrderProductPresenter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var dialogAlert: AlertDialog

    private lateinit var presenterS: SupplierPresenter
    private lateinit var presenterP: ProductPresenter
    private lateinit var supplierId: String

    private var add: String = "0"

    companion object{
        lateinit var orderProduct: OrderProduct
        var orderProducts: MutableList<OrderProduct> = mutableListOf()
        var supplierNameDropdown: MutableList<String> = arrayListOf()
        var supplierIdDropdown: MutableList<String> = arrayListOf()
        var suppliers: MutableList<Supplier> = arrayListOf()
        var productNameDropdown: MutableList<String> = arrayListOf()
        var productIdDropdown: MutableList<String> = arrayListOf()
        var products: MutableList<Product> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_product)
        presenter = OrderProductPresenter(this, Repository())
        presenter.getAllOrderProduct()
        presenterS = SupplierPresenter(this, Repository())
        presenterS.getAllSupplier()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        orderAdapter = OrderProductRecyclerViewAdapter(orderProductsList) {}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProducts){
                    showDetail(it)
                }
                query?.let { orderAdapter.filterOrderProduct(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProducts){
                    showDetail(it)
                }
                newText?.let { orderAdapter.filterOrderProduct(it) }
                return false
            }

        })
        fabAnimation()
        show_pending.setOnClickListener {
            CustomFun.createToolTips(this, "P").showAlignTop(show_pending)
            val filtered = orderProductsTemp.filter { it.status == "Pending" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
        show_delivery.setOnClickListener {
            CustomFun.createToolTips(this, "O").showAlignTop(show_delivery)
            val filtered = orderProductsTemp.filter { it.status == "On Delivery" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
        show_arrived.setOnClickListener {
            CustomFun.createToolTips(this, "A").showAlignTop(show_arrived)
            val filtered = orderProductsTemp.filter { it.status == "Arrived" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenter.getAllOrderProduct()
            presenterS.getAllSupplier()
            presenterP.getAllProduct()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = OrderProductRecyclerViewAdapter(temps as MutableList<OrderProduct>){}
        }else{
            recyclerview.adapter = OrderProductRecyclerViewAdapter(temps as MutableList<OrderProduct>){
                showDetail(it)
            }
        }
        orderAdapter.notifyDataSetChanged()
    }

    override fun showOrderProductLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideOrderProductLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        if (add == "0"){
            val temp: List<OrderProduct> = data?.orderProducts ?: emptyList()
            if (temp.isEmpty()){
                CustomFun.neutralSnackBar(container, baseContext, "Oops, no result")
            }else{
                clearList()
                orderProductsList.addAll(temp)
                orderProductsTemp.addAll(temp)
                temps = orderProductsTemp
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProductsList){
                    showDetail(it)
                    Toast.makeText(this, "Order Number : "+it.id, Toast.LENGTH_LONG).show()
                }
            }
            CustomFun.successSnackBar(container, baseContext, "Ok, success")
        }else if (add == "1"){
            orderProduct = data?.orderProducts?.get(0)!!
            startActivity<AddOrderProductActivity>()
        }
    }

    override fun orderProductFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        orderProductsList.clear()
        orderProductsTemp.clear()
    }

    private fun showDetail(orderProductInput: OrderProduct){
        val text: String = when {
            orderProductInput.status.equals("Arrived") -> {
                "SHOW"
            }
            orderProductInput.status.equals("On Delivery") -> {
                "DONE"
            }
            else -> {
                "EDIT"
            }
        }
        dialogAlert = AlertDialog.Builder(this)
            .setTitle("Order Product")
            .setMessage("What will you do with this order product?")
            .setPositiveButton(text){ _, _ ->
                if (orderProductInput.printed_at.isNullOrEmpty()){
                    orderProduct = orderProductInput
                    startActivity<AddOrderProductActivity>()
                }else{
                    orderProduct = orderProductInput
                    startActivity<EditOrderProductActivity>()
                }
            }
            .show()
    }

    private fun showAlert(){
        dialogAlert = AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to order product?")
            .setCancelable(false)
            .setPositiveButton("YES"){ _, _ ->
                add = "1"
                chooseSupplier()
            }
            .setNegativeButton("NO",null)
            .show()
    }

    private fun fabAnimation(){
        fab_add.setOnClickListener {
            showAlert()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }

    override fun showSupplierLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideSupplierLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        val temp: List<Supplier> = data?.suppliers ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.neutralSnackBar(container, baseContext, "Supplier empty")
        }else{
            clearSuppliers()
            suppliers.addAll(temp)
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    supplierNameDropdown.add(temp[i].name.toString())
                    supplierIdDropdown.add(temp[i].id.toString())
                }
            }
            CustomFun.successSnackBar(container, baseContext, "Supplier success")
        }
    }

    override fun supplierFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearSuppliers(){
        supplierNameDropdown.clear()
        supplierIdDropdown.clear()
        suppliers.clear()
    }

    override fun showProductLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideProductLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.neutralSnackBar(container, baseContext, "Product empty")
        }else{
            clearProducts()
            products.addAll(temp)
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    productNameDropdown.add(temp[i].name.toString())
                    productIdDropdown.add(temp[i].id.toString())
                }
            }
            CustomFun.successSnackBar(container, baseContext, "Product success")
        }
    }

    override fun productFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearProducts(){
        products.clear()
        productNameDropdown.clear()
        productIdDropdown.clear()
    }

    private fun chooseSupplier() {
        supplierId = "-1"
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_supplier, null)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, supplierNameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            supplierId = supplierIdDropdown[position]
            val supplierName = supplierNameDropdown[position]
            Toast.makeText(this, "Supplier : $supplierName", Toast.LENGTH_LONG).show()
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnAdd.setOnClickListener {
            if (supplierId == "-1"){
                CustomFun.failedSnackBar(container, baseContext, "Please choose supplier")
            }else{
                orderProduct = OrderProduct(id_supplier =  supplierId)
                presenter.addOrderProduct(orderProduct)
            }
        }
    }

}
