package com.example.kouveemanagement.product

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.kouveemanagement.CustomFun
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
            if (isValid()){
                getData()
                presenter.editProduct(id, product)
            }
        }
        btn_cancel.setOnClickListener {
            presenter.deleteProduct(id)
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        btn_choose.setOnClickListener {
            startActivityForResult(getImageChooserIntent(), 200)
        }
        btn_upload.setOnClickListener {
            CustomFun.warningSnackBar(container, baseContext, "Uploading image...")
            multipartImageUpload()
        }
    }

    private fun setData(){
        val baseUrl = "https://gregpetshop.berusahapastibisakok.tech/api/product/photo/"
        val product: Product? = intent.getParcelableExtra("product")
        id = product?.id.toString()
        name.setText(product?.name)
        unit.setText(product?.unit)
        product?.stock?.toString()?.let { stock.setText(it) }
        product?.min_stock?.toString()?.let { min_stock.setText(it) }
        product?.price?.toString()?.let { price.setText(it) }
        created_at.setText(product?.created_at)
        if (product?.updated_at.isNullOrBlank()){
            updated_at.setText("-")
        }else{
            updated_at.setText(product?.updated_at)
        }
        if (product?.deleted_at.isNullOrBlank()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(product?.deleted_at)
        }
        product?.photo.let { Picasso.get().load(baseUrl+product?.photo.toString()).fit().into(image_product) }
    }

    private fun getData(){
        val name = name.text.toString()
        val unit = unit.text.toString()
        val stock = stock.text.toString()
        val minStock = min_stock.text.toString()
        val price = price.text.toString()
        product = Product(id = id,name = name,unit = unit,stock = stock.toInt(), min_stock = minStock.toInt(), price = price.toDouble())
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (unit.text.isNullOrEmpty()){
            unit.error = getString(R.string.error_unit)
            return false
        }
        if (stock.text.isNullOrEmpty() || stock.text.toString().toInt() < 1){
            stock.error = getString(R.string.error_stock)
            return false
        }
        if (min_stock.text.isNullOrEmpty() || min_stock.text.toString().toInt() < 1){
            min_stock.error = getString(R.string.error_min_stock)
            return false
        }
        if (price.text.isNullOrEmpty() || price.text.toString().toDouble() < 1){
            price.error = getString(R.string.error_price)
            return false
        }
        return true
    }

    override fun showProductLoading() {
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
        startActivity<ProductManagementActivity>()
    }

    override fun productFailed(data: String) {
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<ProductManagementActivity>()
    }

    //IMAGE
    private lateinit var bitmap: Bitmap
    private lateinit var byteArray: ByteArray

    //CAMERA
    private fun getImageOutputUri(): Uri? {
        var fileUri: Uri? = null
        val img = getExternalFilesDir("")
        if (img!=null){
            fileUri = Uri.fromFile(File(img.path, "product.png"))
        }
        return fileUri
    }

    //IMAGE
    private fun getPathFromURI(contentUri: Uri): String? {
        try {
            val projectData = arrayOf(MediaStore.Audio.Media.DATA)
            val cursor = contentResolver.query(contentUri, projectData,null, null, null)!!
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }catch (e: Exception){
            Log.d("IMG ", e.message)
        }
        return ""
    }

    //PATH
    private fun getImageFromFilePath(dataInput: Intent?): String {
        val isCamera = dataInput == null || dataInput.data == null

        return if (isCamera) getImageOutputUri()?.path.toString()
        else dataInput?.data?.let { getPathFromURI(it) }!!
    }

    private fun getImageFilePath(data: Intent?): String {
        return getImageFromFilePath(data)
    }

    private fun getByteArrayInBackground() {
        val thread: Thread = object : Thread() {
            override fun run() {
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                byteArray = bos.toByteArray()
            }
        }
        thread.start()
    }

    private fun getImageChooserIntent(): Intent{
        val fileUri = getImageOutputUri()
        val intents: MutableList<Intent> = mutableListOf()

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
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

        val chosenIntent = Intent.createChooser(mainIntent, "Select image source")
        chosenIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray())

        return chosenIntent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 200){
                val filePath = getImageFilePath(data)
                Log.d("IMG PATH ", filePath)
                CustomFun.welcomeSnackBar(container, baseContext, "Image has been chosen..")
                try {
                    val bmOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(filePath, bmOptions)
                    if (bitmap.byteCount > 102400){
                        CustomFun.failedSnackBar(container, baseContext, "Max size : 100KB")
                    }else{
                        getByteArrayInBackground()
                        image_product.setImageBitmap(bitmap)
                        btn_upload.visibility = View.VISIBLE
                    }
                }catch (e: OutOfMemoryError){
                    CustomFun.failedSnackBar(container, baseContext, e.message.toString())
                }
            }
        }
    }

    private fun multipartImageUpload() {
        try {
            val filesDir = applicationContext.filesDir
            val file = File(filesDir, "product" + ".png")
            val fos = FileOutputStream(file)
            fos.write(byteArray)
            fos.flush()
            fos.close()
            val body: MultipartBody.Part = createFormData("photo", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            imagePresenter.uploadPhotoProduct(id, body)
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
        CustomFun.warningLongSnackBar(container, baseContext, "Uploading image..")
    }

    override fun hideUploadProgress() {
    }

    override fun uploadProductSuccess(data: ResponseBody?) {
        startActivity<ProductManagementActivity>()
    }

    override fun uploadProductFailed(data: String) {
        progressbar_img.visibility = View.INVISIBLE
        btn_choose.visibility = View.VISIBLE
        btn_upload.visibility = View.VISIBLE
        CustomFun.failedSnackBar(container, baseContext, data)
    }


}
