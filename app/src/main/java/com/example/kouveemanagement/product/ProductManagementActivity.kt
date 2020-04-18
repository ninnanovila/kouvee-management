package com.example.kouveemanagement.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ProductRecyclerViewAdapter
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_management.*
import org.jetbrains.anko.startActivity

class ProductManagementActivity : AppCompatActivity(), ProductView {

    private var productsList: MutableList<Product> = mutableListOf()
    private val productsTemp = ArrayList<Product>()
    private var temps = ArrayList<Product>()
    private val minProducts = ArrayList<Product>()

    private lateinit var productAdapter: ProductRecyclerViewAdapter
    private lateinit var presenter: ProductPresenter

    private lateinit var dialog: View

    companion object{
        var products: MutableList<Product> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)
        presenter = ProductPresenter(this, Repository())
        presenter.getAllProduct()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        productAdapter = ProductRecyclerViewAdapter(productsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                query?.let { productAdapter.filterProduct(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                newText?.let { productAdapter.filterProduct(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddProductFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_min.setOnClickListener {
            if (minProducts.isNullOrEmpty()){
                CustomFun.warningSnackBar(container, baseContext, "Minimum product empty")
            }else{
                temps = minProducts
                getList()
            }
        }
        show_all.setOnClickListener {
            temps = productsTemp
            getList()
        }
        show_en.setOnClickListener {
            val filtered = productsTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<Product>
            getList()
        }
        show_dis.setOnClickListener {
            val filtered = productsTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<Product>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenter.getAllProduct()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if(temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = ProductRecyclerViewAdapter(temps as MutableList<Product>){}
        }else{
            if(sort_switch.isChecked){
                val sorted = temps.sortedBy { it.name }
                recyclerview.adapter = ProductRecyclerViewAdapter(sorted as MutableList<Product>){
                    showDialog(it)
                }
            }else{
                recyclerview.adapter = ProductRecyclerViewAdapter(temps as MutableList<Product>){
                    showDialog(it)
                }
            }
        }
        productAdapter.notifyDataSetChanged()
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
            clearList()
            productsList.addAll(temp)
            productsTemp.addAll(temp)
            temps = productsTemp
            for (i in productsList.indices){
                if (temps[i].deleted_at == null && (temps[i].stock!!.toInt() <= temps[i].min_stock!!.toInt())){
                    minProducts.add(temps[i])
                }
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = ProductRecyclerViewAdapter(productsList) {
                showDialog(it)
            }
            CustomFun.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun productFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        productsList.clear()
        productsTemp.clear()
        minProducts.clear()
    }

    private fun showDialog(product: Product){
        val baseUrl = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_product, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val unit = dialog.findViewById<TextView>(R.id.unit)
        val stock = dialog.findViewById<TextView>(R.id.stock)
        val minStock = dialog.findViewById<TextView>(R.id.min_stock)
        val price = dialog.findViewById<TextView>(R.id.price)
        val photo = dialog.findViewById<ImageView>(R.id.photo)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = product.name.toString()
        unit.text = product.unit.toString()
        stock.text = product.stock.toString()
        minStock.text = product.min_stock.toString()
        price.text = product.price.toString()
        product.photo.let { Picasso.get().load(baseUrl+product.photo.toString()).fit().into(photo) }
        createdAt.text = product.created_at
        updatedAt.text = product.updated_at
        if (product.deleted_at.isNullOrEmpty()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = product.deleted_at
        }
        if (product.deleted_at != null){
            btnEdit.visibility = View.GONE
        }
        val infoDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialog)
            .show()
        btnEdit.setOnClickListener {
            startActivity<EditProductActivity>("product" to product)
        }
        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }

}

