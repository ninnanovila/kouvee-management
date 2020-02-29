package com.example.kouveemanagement.product

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.presenter.UploadImagePresenter
import com.example.kouveemanagement.presenter.UploadPhotoProductView
import com.example.kouveemanagement.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_product.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import java.io.*

class EditProductActivity : AppCompatActivity(), ProductView, UploadPhotoProductView {

    private lateinit var id: String
    private lateinit var presenter: ProductPresenter
    private lateinit var product: Product

    private lateinit var imagePresenter: UploadImagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        setData()
        presenter = ProductPresenter(this, Repository())
        imagePresenter = UploadImagePresenter(this, Repository())

        btn_save.setOnClickListener {
            getData()
            presenter.editProduct(id, product)
        }

        btn_delete.setOnClickListener {
            presenter.deleteProduct(id)
        }

        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }

        btn_choose.setOnClickListener {
            //IMAGE
            startActivityForResult(getImageChooserIntent(), IMAGE_RESULT)
        }

        btn_upload.setOnClickListener {
            if (bitmap!=null){
                Toast.makeText(this, "Bitmap not null.", Toast.LENGTH_SHORT).show()
                multipartImageUpload()
            }else{
                Toast.makeText(this, "Bitmap null.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(){

        val base_url = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"

        val product: Product? = intent.getParcelableExtra("product")
        id = product?.id.toString()

        name.setText(product?.name)
        unit.setText(product?.unit)
        product?.stock?.toString()?.let { stock.setText(it) }
        product?.min_stock?.toString()?.let { min_stock.setText(it) }
        product?.price?.toString()?.let { price.setText(it) }
        created_at.text = product?.created_at
        updated_at.text = product?.updated_at

        product?.photo.let { Picasso.get().load(base_url+product?.photo.toString()).fit().into(image_product) }
    }

    private fun getData(){
        val name = name.text.toString()
        val unit = unit.text.toString()
        val stock = stock.text.toString()
        val minStock = min_stock.text.toString()
        val price = price.text.toString()

        product = Product(id, name, unit, stock.toInt(), minStock.toInt(), price.toDouble(), null)
    }

    override fun showProductLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideProductLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun productSuccess(data: ProductResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ProductManagementActivity>()
    }

    override fun productFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<ProductManagementActivity>()
    }


    //IMAGE
    private val IMAGE_RESULT = 200
    var bitmap: Bitmap? = null
    var byteArray: ByteArray? = null

    private fun getImageOutputUri(): Uri? {
        var fileUri: Uri? = null
        val img = getExternalFilesDir("")
        if (img!=null){
            fileUri = Uri.fromFile(File(img.path, "profile.jpeg"))
        }
        return fileUri
    }

    private fun getImageFromFilePath(data: Intent?): String {
        return getImageOutputUri()?.path.toString()
    }

    private fun getImageFilePath(data: Intent?): String {
        return getImageFromFilePath(data)
    }

    private fun getByteArrayInBackground() {
        val thread: Thread = object : Thread() {
            override fun run() {
                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
                byteArray = bos.toByteArray()
            }
        }
        thread.start()
    }

    private fun getImageChooserIntent(): Intent{
        val fileUri = getImageOutputUri()
        val intents: MutableList<Intent> = mutableListOf()

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam =
            packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            if (fileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            }
            intents.add(intent)
        }

        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery =
            packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intents.add(intent)
        }

        var mainIntent: Intent? = intents[intents.size - 1]
        for (intent in intents) {
            if (intent.component?.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        intents.remove(mainIntent)

        val choosedIntent = Intent.createChooser(mainIntent, "Select source")
        choosedIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray())

        return choosedIntent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_RESULT){
                val filePath = getImageFilePath(data)
                Toast.makeText(this, filePath, Toast.LENGTH_LONG).show()
                if (filePath != null){
                    bitmap = BitmapFactory.decodeFile(filePath)
                    getByteArrayInBackground()
                    image_product.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun multipartImageUpload() {
        try {
            if (byteArray != null) {
                val filesDir = applicationContext.filesDir
                val file = File(filesDir, "image" + ".png")
                val fos = FileOutputStream(file)
                fos.write(byteArray)
                fos.flush()
                fos.close()

                val body: MultipartBody.Part =
                    createFormData("photo", file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                imagePresenter.uploadPhotoProduct(id, body)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun showUploadProgress() {
        progressbar_img.visibility = View.VISIBLE
        btn_choose.visibility = View.INVISIBLE
        btn_upload.visibility = View.INVISIBLE
    }

    override fun hideUploadProgress() {
        progressbar_img.visibility = View.INVISIBLE
        btn_choose.visibility = View.VISIBLE
        btn_upload.visibility = View.VISIBLE
    }

    override fun uploadProductSuccess(data: ResponseBody?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ProductManagementActivity>()
    }

    override fun uploadProductFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

}
