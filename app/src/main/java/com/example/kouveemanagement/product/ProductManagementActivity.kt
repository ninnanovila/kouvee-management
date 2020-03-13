package com.example.kouveemanagement.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.Animation
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

    private var products: MutableList<Product> = mutableListOf()
    private lateinit var presenter: ProductPresenter

    private lateinit var dialog: View
    private var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)
        presenter = ProductPresenter(this, Repository())
        presenter.getAllProduct()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        fabAnimation()
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
                products.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = ProductRecyclerViewAdapter(products) {
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(product: Product){

        val base_url = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"

        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_product, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val unit = dialog.findViewById<TextView>(R.id.unit)
        val stock = dialog.findViewById<TextView>(R.id.stock)
        val min_stock = dialog.findViewById<TextView>(R.id.min_stock)
        val price = dialog.findViewById<TextView>(R.id.price)
        val photo = dialog.findViewById<ImageView>(R.id.photo)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = product.name.toString()
        unit.text = product.unit.toString()
        stock.text = product.stock.toString()
        min_stock.text = product.min_stock.toString()
        price.text = product.price.toString()
        product.photo.let { Picasso.get().load(base_url+product.photo.toString()).fit().into(photo) }

        val infoDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditProductActivity>("product" to product)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }

    }

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
            val fragment: Fragment = AddProductFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }

}
