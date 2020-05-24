package com.example.kouveemanagement.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ProductRecyclerViewAdapter
import com.example.kouveemanagement.model.DetailProductTransaction
import com.example.kouveemanagement.model.DetailProductTransactionResponse
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.presenter.DetailProductTransactionPresenter
import com.example.kouveemanagement.presenter.DetailProductTransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_detail_product_transaction.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddDetailProductTransactionFragment : Fragment(), DetailProductTransactionView {

    private lateinit var idTransaction: String
    private lateinit var detailProductTransaction: DetailProductTransaction
    private lateinit var presenter: DetailProductTransactionPresenter

    private lateinit var idProduct: String
    private lateinit var productAdapter: ProductRecyclerViewAdapter

    companion object{
        fun newInstance() = AddDetailProductTransactionFragment()
        var products: MutableList<Product> = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_detail_product_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productAdapter = ProductRecyclerViewAdapter(ProductTransactionActivity.enProducts){}
        idTransaction = AddTransactionActivity.idTransaction
        idProduct = "-1"
        btn_close.setOnClickListener {
            startActivity<AddTransactionActivity>("type" to "product")
        }
        show_detail.setOnClickListener {
            startActivity<AddTransactionActivity>("type" to "product")
        }
        setProducts()
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                query?.let { productAdapter.filterForTransaction(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = ProductRecyclerViewAdapter(products){
                    showDialog(it)
                }
                newText?.let { productAdapter.filterForTransaction(it) }
                return false
            }
        })
    }

    fun getData(quantity: String){
        detailProductTransaction = DetailProductTransaction(id_transaction = idTransaction, id_product = idProduct, quantity = quantity.toInt())
    }

    private fun isValid(editText: EditText): Boolean {
        if(editText.text.isNullOrEmpty()){
            editText.error = getString(R.string.error_quantity)
            return false
        }else if (editText.text.toString() == "0"){
            editText.error = getString(R.string.error_zero_quantity)
            return false
        }
        return true
    }

    private fun setProducts(){
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = ProductRecyclerViewAdapter(ProductTransactionActivity.enProducts){
            idProduct = it.id.toString()
            showDialog(it)
        }
    }

    private fun showDialog(product: Product){
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.item_quantity, null)
        val name = dialog.findViewById<EditText>(R.id.name)
        val stock = dialog.findViewById<EditText>(R.id.stock)
        val quantity = dialog.findViewById<EditText>(R.id.quantity)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        name.setText(product.name.toString())
        stock.setText(product.stock.toString())
        quantity.setText("0")

        val showDialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialog)
            .show()

        btnAdd.setOnClickListener {
            if (isValid(quantity) && !passStock(product, quantity.text.toString())){
                getData(quantity.text.toString())
                presenter = DetailProductTransactionPresenter(this, Repository())
                presenter.addDetailProductTransaction(detailProductTransaction)
            }else if (passStock(product, quantity.text.toString())){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Max stock : ${product.stock}")
            }else if (idProduct == "-1"){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please choose product")
            }
            showDialog.dismiss()
        }

        btnClose.setOnClickListener{
            showDialog.dismiss()
        }

    }

    override fun showDetailProductTransactionLoading() {
    }

    override fun hideDetailProductTransactionLoading() {
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        CustomFun.successSnackBar(requireView(), requireContext(), "Success add!")
    }

    override fun detailProductTransactionFailed(data: String) {
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    private fun passStock(product: Product, quantity: String) : Boolean{
        if (quantity.toInt() > product.stock.toString().toInt()){
            return true
        }
        return false
    }

}
