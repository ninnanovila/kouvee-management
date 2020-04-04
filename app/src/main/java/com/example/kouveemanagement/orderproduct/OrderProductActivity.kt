package com.example.kouveemanagement.orderproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        show_min_product.setOnClickListener {
            Toast.makeText(this, "Min Product", Toast.LENGTH_LONG).show()
        }
        show_pending.setOnClickListener {
            val filtered = orderProductsTemp.filter { it.status == "Pending" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
        show_delivery.setOnClickListener {
            val filtered = orderProductsTemp.filter { it.status == "On Delivery" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
        show_arrived.setOnClickListener {
            val filtered = orderProductsTemp.filter { it.status == "Arrived" }
            temps = filtered as ArrayList<OrderProduct>
            getList()
        }
    }

    private fun getList(){
        recyclerview.adapter = OrderProductRecyclerViewAdapter(temps as MutableList<OrderProduct>){
            showDetail(it)
        }
        orderAdapter.notifyDataSetChanged()
    }

    override fun showOrderProductLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideOrderProductLoading() {
        progressbar.visibility = View.GONE
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        if (add == "0"){
            val temp: List<OrderProduct> = data?.orderProducts ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                orderProductsList.addAll(temp)
                orderProductsTemp.addAll(temp)
                temps.addAll(temp)
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProductsList){
                    showDetail(it)
                    Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
                }
            }
        }else if (add == "1"){
            orderProduct = data?.orderProducts?.get(0)!!
            startActivity<AddOrderProductActivity>()
        }
    }

    override fun orderProductFailed() {
        Toast.makeText(this, "Failed Order Product", Toast.LENGTH_SHORT).show()
    }

    private fun showDetail(orderProductInput: OrderProduct){
        var text = "DONE"
        if (orderProductInput.status.equals("Arrived")){
            text = "SHOW"
        }else{
            text = "EDIT"
        }
        Toast.makeText(this, "PRINTED: "+orderProductInput.printed_at.toString() , Toast.LENGTH_SHORT).show()
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
        progressbar.visibility = View.VISIBLE
    }

    override fun hideSupplierLoading() {
        progressbar.visibility = View.GONE
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        val temp: List<Supplier> = data?.suppliers ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            suppliers.addAll(temp)
            if (supplierNameDropdown.isNotEmpty()){
                supplierNameDropdown.clear()
                supplierIdDropdown.clear()
                for (i in temp.indices){
                    if (temp[i].deleted_at == null){
                        supplierNameDropdown.add(temp[i].name.toString())
                        supplierIdDropdown.add(temp[i].id.toString())
                    }
                }
            }else{
                for (i in temp.indices){
                    if (temp[i].deleted_at == null){
                        supplierNameDropdown.add(temp[i].name.toString())
                        supplierIdDropdown.add(temp[i].id.toString())
                    }
                }
            }
        }
    }

    override fun supplierFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
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
            products.addAll(temp)
            if (productNameDropdown.isNotEmpty()){
                productNameDropdown.clear()
                productIdDropdown.clear()
                for (i in temp.indices){
                    if (temp[i].deleted_at == null){
                        productNameDropdown.add(temp[i].name.toString())
                        productIdDropdown.add(temp[i].id.toString())
                    }
                }
            }else{
                for (i in temp.indices){
                    if (temp[i].deleted_at == null){
                        productNameDropdown.add(temp[i].name.toString())
                        productIdDropdown.add(temp[i].id.toString())
                    }
                }
            }
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed Product", Toast.LENGTH_SHORT).show()
    }

    private fun chooseSupplier() {
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose, null)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, supplierNameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, position, _ ->
            supplierId = supplierIdDropdown[position]
            Toast.makeText(this, "ID SUPPLIER : $supplierId", Toast.LENGTH_LONG).show()
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnAdd.setOnClickListener {
            orderProduct = OrderProduct(null, supplierId, null, null)
            presenter.addOrderProduct(orderProduct)
        }
    }

}
