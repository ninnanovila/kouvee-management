package com.example.kouveemanagement.orderproduct

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.model.DetailOrderProductResponse
import com.example.kouveemanagement.model.OrderProduct
import com.example.kouveemanagement.model.OrderProductResponse
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_add_order_product.*
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import java.io.*

class AddOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView, OrderInvoiceView {

    private lateinit var presenter: OrderProductPresenter

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter
    private lateinit var detailAdapter: DetailOrderProductRecyclerViewAdapter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private lateinit var supplierId: String
    private lateinit var orderProduct: OrderProduct

    private var state: String = ""

    //PRINT
    private lateinit var presenterI: OrderInvoicePresenter

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
        presenterI = OrderInvoicePresenter(this, Repository())
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
        CustomFun.setSwipe(swipe_rv)
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
        total.text = CustomFun.changeToRp(totalInput.toDouble())
        created_at.text = input.created_at.toString()
        updated_at.text = input.updated_at.toString()
        printed_at.text = input.printed_at.toString()
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
                CustomFun.successSnackBar(container, baseContext, "Success edit supplier")
                startActivity<AddOrderProductActivity>()
            }
            "delete" -> {
                startActivity<OrderProductActivity>()
            }
            else -> {
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }
    }

    override fun orderProductFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
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
            CustomFun.neutralSnackBar(container, baseContext, "Empty detail")
        }else{
            clearDetail()
            detailOrderProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(OrderProductActivity.products, detailOrderProducts.asReversed()){
                val fragment = EditDetailOrderProductFragment.newInstance(it)
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragment).commit()
            }
            CustomFun.successSnackBar(container, baseContext, "Detail success")
        }
    }

    override fun detailOrderProductFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearDetail(){
        detailOrderProducts.clear()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    private fun showEditSupplier(){
        supplierId = orderProduct.id_supplier.toString()
        dialog = LayoutInflater.from(this).inflate(R.layout.item_choose_supplier, null)
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
            .setCancelable(false)
            .show()

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }

        btnSave.setOnClickListener {
            if (supplierId == orderProduct.id_supplier.toString()){
                CustomFun.failedSnackBar(container, baseContext, "Supplier same as before")
            }else{
                state = "edit"
                val newOrderProduct = OrderProduct(orderProduct.id.toString(), supplierId, orderProduct.total, orderProduct.status)
                presenter.editOrderProduct(orderProduct.id.toString(), newOrderProduct)
            }
        }
    }

    private fun alertDialog(){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure to $state this order ?")
            .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                if (state == "print"){
                    presenterI.getOrderInvoice(orderProduct.id.toString())
                }else{
                    presenter.deleteOrderProduct(orderProduct.id.toString())
                }
            }
            .setNegativeButton("NO"){ _: DialogInterface, _: Int ->
                CustomFun.warningSnackBar(container, baseContext, "Process canceled")
            }
            .setCancelable(false)
            .show()
    }

    override fun showDownloadProgress() {
        CustomFun.warningLongSnackBar(container, baseContext, "Creating invoice..")
    }

    override fun hideDownloadProgress() {
    }

    override fun orderInvoiceSuccess(data: ResponseBody?) {
        data?.let { writeToDisk(it) }
    }

    override fun orderInvoiceFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    //FUNCTION FOR WRITE TO DISK
    private fun writeToDisk(responseBody: ResponseBody) : Boolean {
        val id = orderProduct.id.toString()+"_order_invoice.pdf"
        val file = File(getExternalFilesDir(null).toString() + File.separator.toString() + id)

        lateinit var inputStream: InputStream
        lateinit var outputStream: OutputStream
        try {
            val fileReader = ByteArray(4096)
            val fileSize: Long = responseBody.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = responseBody.byteStream()
            outputStream = FileOutputStream(file)

            while (true){
                val read = inputStream.read(fileReader)
                if (read == -1){
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read
                Log.d("PDF ", " file download: $fileSizeDownloaded of $fileSize");
            }
            CustomFun.successSnackBar(container, baseContext, "Download invoice done..")
            presenter.editPrintOrderProduct(orderProduct.id.toString())
            outputStream.flush()
            return true
        }catch (e: IOException){
            return false
        }finally {
            inputStream.close()
            outputStream.close()
        }
    }

}
