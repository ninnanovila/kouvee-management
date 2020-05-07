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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.presenter.ProductPresenter
import com.example.kouveemanagement.presenter.ProductView
import com.example.kouveemanagement.presenter.UploadImagePresenter
import com.example.kouveemanagement.presenter.UploadPhotoProductView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_product.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.support.v4.startActivity
import java.io.*

/**
 * A simple [Fragment] subclass.
 */
class AddProductFragment : Fragment(), ProductView, UploadPhotoProductView {

    private lateinit var product: Product
    private lateinit var presenter: ProductPresenter

    private lateinit var imagePresenter: UploadImagePresenter
    private var withPhoto = "0"

    companion object {
        fun newInstance() = AddProductFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePresenter = UploadImagePresenter(this, Repository())
        btn_add.setOnClickListener {
            if (isValid()){
                if (withPhoto == "0"){
                    getData()
                    presenter = ProductPresenter(this, Repository())
                    presenter.addProduct(product)
                }else if (withPhoto == "1"){
                    getData()
                    presenter = ProductPresenter(this, Repository())
                    presenter.addProduct(product)
                    multipartImageUpload()
                }
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        btn_choose.setOnClickListener {
            startActivityForResult(getImageChooserIntent(), 200)
        }
    }

    fun getData(){
        val name = name.text.toString()
        val unit = unit.text.toString()
        val stock = stock.text.toString()
        val minStock = min_stock.text.toString()
        val price = price.text.toString()
        product = Product(name = name, unit = unit, stock = stock.toInt(), min_stock = minStock.toInt(), price = price.toDouble())
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
        if (price.text.isNullOrEmpty() || price.text.toString().toInt() < 1){
            price.error = getString(R.string.error_price)
            return false
        }
        return true
    }

    override fun showProductLoading() {
        btn_add.startAnimation()
    }

    override fun hideProductLoading() {
    }

    override fun productSuccess(data: ProductResponse?) {
    }

    override fun productFailed(data: String) {
        btn_choose.visibility = View.VISIBLE
        btn_add.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    //PHOTO
    //IMAGE
    private lateinit var bitmap: Bitmap
    private lateinit var byteArray: ByteArray

    //CAMERA
    private fun getImageOutputUri(): Uri? {
        var fileUri: Uri? = null
        val img = requireActivity().getExternalFilesDir("")
        if (img!=null){
            fileUri = Uri.fromFile(File(img.path, "product.png"))
        }
        return fileUri
    }

    //IMAGE
    private fun getPathFromURI(contentUri: Uri): String? {
        try {
            val projectData = arrayOf(MediaStore.Audio.Media.DATA)
            val cursor = requireActivity().contentResolver.query(contentUri, projectData,null, null, null)!!
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
        val listCam = requireActivity().packageManager.queryIntentActivities(captureIntent, 0)
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
        val listGallery = requireActivity().packageManager.queryIntentActivities(galleryIntent, 0)
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
                CustomFun.welcomeSnackBar(requireView(), requireContext(), "Image has been chosen..")
                try {
                    val bmOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(filePath, bmOptions)
                    val fileLength = File(filePath).length()
                    val sizeInKb = fileLength / (1024.0)
                    val sizeInKbStr = "%.2f".format(sizeInKb)
                    if (sizeInKb > 500){
                        CustomFun.failedSnackBar(requireView(), requireContext(), "Size: $sizeInKbStr KB, Max : 500 KB")
                    }else{
                        withPhoto = "1"
                        getByteArrayInBackground()
                        image_product.setImageBitmap(bitmap)
                        CustomFun.successSnackBar(requireView(), requireContext(),  "Image size : $sizeInKbStr KB")
                    }
                }catch (e: OutOfMemoryError){
                    CustomFun.failedSnackBar(requireView(), requireContext(), e.message.toString())
                }
            }
        }
    }

    private fun multipartImageUpload() {
        try {
            val filesDir = requireContext().filesDir
            val file = File(filesDir, "product" + ".png")
            val fos = FileOutputStream(file)
            fos.write(byteArray)
            fos.flush()
            fos.close()
            val body: MultipartBody.Part = createFormData("photo", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            imagePresenter.uploadPhotoProduct(product.id.toString(), body)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun showUploadProgress() {
        btn_choose.visibility = View.INVISIBLE
    }

    override fun hideUploadProgress() {
    }

    override fun uploadProductSuccess(data: ResponseBody?) {
        startActivity<ProductManagementActivity>()
    }

    override fun uploadProductFailed(data: String) {
        btn_choose.visibility = View.VISIBLE
        btn_add.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

}
