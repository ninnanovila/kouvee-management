package com.example.kouveemanagement.orderproduct

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.DetailOrderProductRecyclerViewAdapter
import com.example.kouveemanagement.model.*
import com.example.kouveemanagement.presenter.*
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_order_product.*
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import java.io.*

class EditOrderProductActivity : AppCompatActivity(), OrderProductView, DetailOrderProductView, OrderInvoiceView {

    private lateinit var presenter: OrderProductPresenter
    private lateinit var orderProduct: OrderProduct
    private lateinit var idOrderProduct: String

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter

    private lateinit var presenterI: OrderInvoicePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order_product)
        orderProduct = OrderProductActivity.orderProduct
        idOrderProduct = orderProduct.id.toString()
        setData(orderProduct)
        presenter = OrderProductPresenter(this, Repository())
        presenterD = DetailOrderProductPresenter(this, Repository())
        presenterD.getDetailOrderProductByOrderId(idOrderProduct)
        presenterI = OrderInvoicePresenter(this, Repository())
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        btn_done.setOnClickListener {
            alertDialog()
        }
        btn_print.setOnClickListener {
            presenterI.getOrderInvoice(idOrderProduct)
        }
    }

    private fun setData(input : OrderProduct){
        if (input.status.equals("Arrived")){
            btn_done.visibility = View.GONE
        }
        id.text = input.id.toString()
        for (i in OrderProductActivity.supplierNameDropdown.indices){
            if (input.id_supplier == OrderProductActivity.supplierIdDropdown[i]){
                supplier.text = OrderProductActivity.supplierNameDropdown[i]
            }
        }
        status.text = input.status.toString()
        val totalInput = input.total.toString()
        total.text = CustomFun.changeToRp(totalInput.toDouble())
        created_at.text = input.created_at.toString()
        updated_at.text = input.updated_at.toString()
        printed_at.text = input.printed_at.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OrderProductActivity>()
    }

    override fun showOrderProductLoading() {
        btn_print.startAnimation()
    }

    override fun hideOrderProductLoading() {
    }

    override fun orderProductSuccess(data: OrderProductResponse?) {
        status.text = data?.orderProducts?.get(0)?.status.toString()
        startActivity<OrderProductActivity>()
    }

    override fun orderProductFailed(data: String) {
        btn_print.revertAnimation()
        CustomFun.failedSnackBar(container, baseContext, data)
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
            CustomFun.neutralSnackBar(container, baseContext, "Detail empty")
        }else{
            detailOrderProducts.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = DetailOrderProductRecyclerViewAdapter(OrderProductActivity.products, detailOrderProducts){
                Toast.makeText(this, it.id_order, Toast.LENGTH_LONG).show()
            }
            CustomFun.successSnackBar(container, baseContext, "Detail success")
        }
    }

    override fun detailOrderProductFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun alertDialog(){
        AlertDialog.Builder(this)
            .setIcon(R.drawable.update)
            .setTitle("Confirmation")
            .setMessage("Are you sure to done this order ?")
            .setCancelable(false)
            .setPositiveButton("YES"){ _, _ ->
                presenter.editDoneOrderProduct(idOrderProduct)
            }
            .setNegativeButton("NO"){ dialog, _ ->
                dialog.dismiss()
                CustomFun.warningSnackBar(container, baseContext, "Process canceled..")
            }
            .show()
    }

    override fun showDownloadProgress() {
        btn_print.startAnimation()
        CustomFun.warningLongSnackBar(container, baseContext, "Creating invoice..")
    }

    override fun hideDownloadProgress() {
    }

    override fun orderInvoiceSuccess(data: ResponseBody?) {
        btn_print.revertAnimation()
        data?.let { writeToDisk(it) }
    }

    override fun orderInvoiceFailed(data: String) {
        btn_print.revertAnimation()
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    //FUNCTION FOR WRITE TO DISK
    private fun writeToDisk(responseBody: ResponseBody) : Boolean {
        val id = orderProduct.id.toString()+"_order_invoice.pdf"
        val file = File(getExternalFilesDir(null).toString() + File.separator.toString() + "Kouvee/" + id)

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
