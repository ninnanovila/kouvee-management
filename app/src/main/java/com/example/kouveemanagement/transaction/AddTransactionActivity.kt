package com.example.kouveemanagement.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_transaction.*

class AddTransactionActivity : AppCompatActivity(), ProductView, ServiceView, TransactionView, DetailProductTransactionView, DetailServiceTransactionView {

    private var state: String = ""
    private lateinit var type: String

    private lateinit var presenterP: ProductPresenter
    private lateinit var presenterS: ServicePresenter

    private lateinit var presenter: TransactionPresenter
    private lateinit var transaction: Transaction
    private lateinit var detailProductPresenter: DetailProductTransactionPresenter
    private var detailProducts: MutableList<DetailProductTransaction> = mutableListOf()
    private lateinit var detailServicePresenter: DetailServiceTransactionPresenter
    private var detailServices: MutableList<DetailServiceTransaction> = mutableListOf()

    companion object{
        lateinit var idTransaction: String
        var nameProductDropdown: MutableList<String> = arrayListOf()
        var idProductDropdown: MutableList<String> = arrayListOf()
        var nameServiceDropdown: MutableList<String> = arrayListOf()
        var idServiceDropdown: MutableList<String> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        transaction = TransactionActivity.transaction
        idTransaction = transaction.id.toString()
        type = intent?.getStringExtra("type").toString()
        presenterP = ProductPresenter(this, Repository())
        presenterP.getAllProduct()
        presenterS = ServicePresenter(this, Repository())
        presenterS.getAllService()
        presenter.editTotalTransaction(idTransaction)
        if (type == "service"){
            detailServicePresenter = DetailServiceTransactionPresenter(this, Repository())
            detailServicePresenter.getDetailServiceTransactionByIdTransaction(idTransaction)
        }else if (type == "product"){
            detailProductPresenter = DetailProductTransactionPresenter(this, Repository())
            detailProductPresenter.getDetailProductTransactionByIdTransaction(idTransaction)
        }
        setData()
    }

    private fun setData(){
        id.text = transaction.id
        id_customer_pet.text = transaction.id_customer_pet
        status.text = transaction.status
    }

    override fun showProductLoading() {
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        val temp: List<Product> = data?.products ?: emptyList()
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

    override fun showServiceLoading() {
    }

    override fun hideServiceLoading() {
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            if (nameServiceDropdown.isNotEmpty()){
                nameServiceDropdown.clear()
                idServiceDropdown.clear()
                for (i in temp.indices){
                    nameServiceDropdown.add(i, temp[i].name.toString())
                    idServiceDropdown.add(i, temp[i].id.toString())
                }
            }else{
                for (i in temp.indices){
                    nameServiceDropdown.add(i, temp[i].name.toString())
                    idServiceDropdown.add(i, temp[i].id.toString())
                }
            }
        }
    }

    override fun serviceFailed() {
        Toast.makeText(this, "Failed Service", Toast.LENGTH_SHORT).show()
    }

    override fun showTransactionLoading() {
    }

    override fun hideTransactionLoading() {
    }

    override fun transactionSuccess(data: TransactionResponse?) {
    }

    override fun transactionFailed() {
        Toast.makeText(this, "Failed Transaction", Toast.LENGTH_SHORT).show()
    }

    override fun showDetailProductTransactionLoading() {
    }

    override fun hideDetailProductTransactionLoading() {
    }

    override fun detailProductTransactionSuccess(data: DetailProductTransactionResponse?) {
        val temp: List<DetailProductTransaction> = data?.detailProductTransactions ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                detailProducts.add(i, temp[i])
            }
//            recyclerview.layoutManager =
        }
    }

    override fun detailProductTransactionFailed() {
        Toast.makeText(this, "Failed Detail Product Transaction", Toast.LENGTH_SHORT).show()
    }

    override fun showDetailServiceTransactionLoading() {
    }

    override fun hideDetailServiceTransactionLoading() {
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        val temp: List<DetailServiceTransaction> = data?.detailServiceTransactions ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                detailServices.add(i, temp[i])
            }
        }
    }

    override fun detailServiceTransactionFailed() {
        Toast.makeText(this, "Failed Detail Service Transaction", Toast.LENGTH_SHORT).show()
    }
}
