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
import com.example.kouveemanagement.model.OrderProduct
import com.example.kouveemanagement.model.OrderProductResponse
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.OrderProductPresenter
import com.example.kouveemanagement.presenter.OrderProductView
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_order_product.*
import org.jetbrains.anko.startActivity

class OrderProductActivity : AppCompatActivity(), OrderProductView, SupplierView {

    private var orderProductsList: MutableList<OrderProduct> = mutableListOf()
    private lateinit var presenter: OrderProductPresenter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private lateinit var dialogAlert: AlertDialog

    private lateinit var presenterS: SupplierPresenter
    private lateinit var supplierId: String

    private var add: String = "0"

    companion object{
        lateinit var orderProduct: OrderProduct
        var nameDropdown: MutableList<String> = arrayListOf()
        var idDropdown: MutableList<String> = arrayListOf()
        var orderProducts: MutableList<OrderProduct> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_product)
        presenter = OrderProductPresenter(this, Repository())
        presenter.getAllOrderProduct()
        presenterS = SupplierPresenter(this, Repository())
        presenterS.getAllSupplier()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        val adapter = OrderProductRecyclerViewAdapter(orderProductsList) {}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProducts){
                    showDetail(it)
                }
                query?.let { adapter.filterOrderProduct(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = OrderProductRecyclerViewAdapter(orderProducts){
                    showDetail(it)
                }
                newText?.let { adapter.filterOrderProduct(it) }
                return false
            }

        })
        fabAnimation()
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
                for (i in temp.indices){
                    orderProductsList.add(i, temp[i])
                }
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
        dialogAlert = AlertDialog.Builder(this)
            .setTitle("Edit")
            .setMessage("What will you do with this order product?")
            .setPositiveButton("EDIT"){ _, _ ->
                orderProduct = orderProductInput
                startActivity<AddOrderProductActivity>()
            }
            .setNegativeButton("DONE"){ _, _ ->
                if(orderProductInput.printed_at.isNullOrEmpty()){
                    Toast.makeText(this, "Please print to done it.", Toast.LENGTH_LONG).show()
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
            .setMessage("Are you sure to make order product?")
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
    }

    override fun hideSupplierLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {
        val temp: List<Supplier> = data?.suppliers ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            if (nameDropdown.isNotEmpty()){
                nameDropdown.clear()
                idDropdown.clear()
                for (i in temp.indices){
                    nameDropdown.add(i, temp[i].name.toString())
                    idDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    nameDropdown.add(i, temp[i].name.toString())
                    idDropdown.add(i, temp[i].id.toString())
                }
            }
        }
    }

    override fun supplierFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun chooseSupplier() {
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose, null)
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        val dropdown = dialog.findViewById<AutoCompleteTextView>(R.id.dropdown)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
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

        btnAdd.setOnClickListener {
            orderProduct = OrderProduct(null, supplierId, null, null)
            presenter.addOrderProduct(orderProduct)
        }
    }
}
