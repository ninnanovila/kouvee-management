package com.example.kouveemanagement.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
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
        val adapter = ProductRecyclerViewAdapter(productsList){}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                query?.let { adapter.filterProduct(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                newText?.let { adapter.filterProduct(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddProductFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
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
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                productsList.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = ProductRecyclerViewAdapter(productsList) {
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
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
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = product.name.toString()
        unit.text = product.unit.toString()
        stock.text = product.stock.toString()
        minStock.text = product.min_stock.toString()
        price.text = product.price.toString()
        product.photo.let { Picasso.get().load(baseUrl+product.photo.toString()).fit().into(photo) }

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
