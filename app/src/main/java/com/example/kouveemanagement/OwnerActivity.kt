package com.example.kouveemanagement

import android.Manifest.permission.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.kouveemanagement.adapter.MenuRecyclerViewAdapter
import com.example.kouveemanagement.employee.EmployeeManagementActivity
import com.example.kouveemanagement.model.Menu
import com.example.kouveemanagement.model.Product
import com.example.kouveemanagement.model.ProductResponse
import com.example.kouveemanagement.orderproduct.OrderProductActivity
import com.example.kouveemanagement.persistent.AppDatabase
import com.example.kouveemanagement.persistent.CurrentUser
import com.example.kouveemanagement.pet.PetSizeManagementActivity
import com.example.kouveemanagement.pet.PetTypeManagementActivity
import com.example.kouveemanagement.presenter.MinProductPresenter
import com.example.kouveemanagement.presenter.MinProductView
import com.example.kouveemanagement.product.ProductManagementActivity
import com.example.kouveemanagement.profile.ProfileActivity
import com.example.kouveemanagement.repository.Repository
import com.example.kouveemanagement.service.ServiceManagementActivity
import com.example.kouveemanagement.supplier.SupplierManagementActivity
import kotlinx.android.synthetic.main.activity_owner.*
import org.jetbrains.anko.startActivity


class OwnerActivity : AppCompatActivity(), MinProductView {

    private var menu: MutableList<Menu> = mutableListOf()
    private var minProductPresenter = MinProductPresenter(this, Repository())

    //NOTIFICATION
    private var notificationId = 0
    private var stackNotification = ArrayList<Product>()
    private var nameProducts: ArrayList<String> = arrayListOf()

    companion object {
        var database: AppDatabase? = null
        var currentUser: CurrentUser? = null
        var minProducts: MutableList<Product> = mutableListOf()

        //NOTIFICATION
        private const val NAME_CHANNEL = "kouvee channel"
        private const val GROUP_KEY_PRODUCTS = "group_key_products"
        private const val NOTIFICATION_REQUEST_CODE = 200
        private const val MAX_NOTIFICATION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuInitialization()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "kouvee-db").build()
        setContentView(R.layout.activity_owner)
        if (!CustomFun.verifiedNetwork(this)) CustomFun.warningSnackBar(container, baseContext, "Please check internet connection")
        else CustomFun.welcomeSnackBar(container, baseContext, "Welcome Admin!")
        setMenu()
        getCurrentUser()
        getMinProduct()
        btn_profile.setOnClickListener {
            startActivity<ProfileActivity>()
        }
        fab_notif.setOnClickListener{
            showDialog()
        }
        askingPermission()
    }

    private fun getMinProduct(){
        minProductPresenter.getMinProduct()
    }

    private fun menuInitialization(){
        val name = resources.getStringArray(R.array.owner_menu)
        val desc = resources.getStringArray(R.array.owner_desc)
        val image = resources.obtainTypedArray(R.array.owner_image)
        menu.clear()
        for (i in name.indices){
            menu.add(Menu(name[i], desc[i], image.getResourceId(i, 0)))
        }
        image.recycle()
    }

    private fun setMenu(){
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = MenuRecyclerViewAdapter(menu) {
            when(it.name) {
                //DATA MASTER
                "Employee" -> startActivity<EmployeeManagementActivity>()
                "Pet Size" -> startActivity<PetSizeManagementActivity>()
                "Pet Type" -> startActivity<PetTypeManagementActivity>()
                "Supplier" -> startActivity<SupplierManagementActivity>()
                "Product" -> startActivity<ProductManagementActivity>()
                "Service" -> startActivity<ServiceManagementActivity>()
                //TRANSACTION
                "Order Product" -> startActivity<OrderProductActivity>()
                else -> CustomFun.warningSnackBar(container, baseContext, "Don't have permission")
            }
        }
    }

    private fun getCurrentUser(){
        val thread = Thread {
            currentUser = database?.currentUserDao()?.getCurrentuser()
            id.text = currentUser?.user_id
            name.text = currentUser?.user_name
            role.text = currentUser?.user_role
        }
        thread.start()
    }

    private fun showLogoutConfirm(){
        val confirm = AlertDialog.Builder(this)
            .setIcon(R.drawable.alert)
            .setTitle("Confirmation")
            .setMessage("Are you sure to log out ?")
            .setCancelable(false)
        confirm.setNegativeButton("NO") { _, _ ->
        }
        confirm.setPositiveButton("YES") { dialog, _ ->
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
            val thread = Thread {
                currentUser?.let { database?.currentUserDao()?.deleteCurrentUser(it) }
                database?.clearAllTables()
                startActivity<MainActivity>()
            }
            thread.start()
            dialog.dismiss()
        }
        confirm.show()
    }

    override fun onBackPressed() {
        showLogoutConfirm()
    }

    override fun showMinProductLoading() {
    }

    override fun hideMinProductLoading() {
    }

    override fun minProductSuccess(data: ProductResponse?) {
        val temp = data?.products ?: emptyList()
        if (temp.isNotEmpty()){
            notificationId = 0
            stackNotification.clear()
            minProducts.clear()
            nameProducts.clear()
            minProducts.addAll(temp)
            for (i in minProducts.indices){
                stackNotification.add(minProducts[i])
                nameProducts.add(minProducts[i].name.toString())
                showNotification(stackNotification[i].name, stackNotification[i].stock, stackNotification[i].min_stock)
                notificationId++
            }
            fab_notif.visibility = View.VISIBLE
        }
    }

    override fun minProductFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        stackNotification.clear()
        notificationId = 0
    }

    private fun showNotification(title: String?, stock: Int?, minStock: Int?){
        if (MainActivity.currentUser?.user_role.toString() == "Admin"){
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val bigIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_product)
            val intent = Intent(this, OrderProductActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder: NotificationCompat.Builder

            val channelId = "channel_01"
            if (notificationId < MAX_NOTIFICATION){
                builder = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("$title on minimum stock")
                    .setContentText("Stock is $stock and minimum is $minStock, let's order")
                    .setSmallIcon(R.drawable.ic_notification_big)
                    .setLargeIcon(bigIcon)
                    .setGroup(GROUP_KEY_PRODUCTS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            }else{
                val notificationStyle = NotificationCompat.InboxStyle()
                    .addLine(stackNotification[notificationId].name + "on minimum stock")
                    .addLine(stackNotification[notificationId-1].name + "on minimum stock")
                    .setBigContentTitle("$notificationId products on minimum stock")
                    .setSummaryText("Let's order product!")
                builder = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("$notificationId products on minimum stock")
                    .setContentText("Let's order product!")
                    .setSmallIcon(R.drawable.ic_notification_big)
                    .setGroup(GROUP_KEY_PRODUCTS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(notificationStyle)
                    .setAutoCancel(true)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT)
                builder.setChannelId(channelId)
                notificationManager.createNotificationChannel(channel)
            }

            val notification = builder.build()
            notificationManager.notify(notificationId, notification)
        }else{
            stackNotification.clear()
            notificationId = 0
        }
    }

    private fun showDialog(){
        val input = nameProducts.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Minimum Product")
            .setIcon(R.drawable.ic_notification_big)
            .setCancelable(false)
            .setItems(input){ _, _ ->
            }
            .setPositiveButton("ORDER"){ _, _ ->
                startActivity<OrderProductActivity>()
            }
            .setNegativeButton("CLOSE"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private var askedPermissions = arrayListOf<String>()
    private var deniedPermission = arrayListOf<String>()
    private var requestedPermission = arrayListOf<String>()

    //PERMISSION
    private fun askingPermission(){
        askedPermissions.add(CAMERA)
        askedPermissions.add(WRITE_EXTERNAL_STORAGE)
        askedPermissions.add(READ_EXTERNAL_STORAGE)
        requestedPermission = findUnAskedPermissions(askedPermissions)

        if (requestedPermission.size > 0) requestPermissions(requestedPermission.toTypedArray(), 107)
    }

    private fun findUnAskedPermissions(requested: java.util.ArrayList<String>): ArrayList<String> {
        val result = java.util.ArrayList<String>()
        for (perm in requested) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 107){
            for (denied in requestedPermission){
                if (!hasPermission(denied)){
                    deniedPermission.add(denied)
                }
            }

            if (deniedPermission.size > 0) {
                if (shouldShowRequestPermissionRationale(deniedPermission[0])) {
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setIcon(R.drawable.alert)
                        .setTitle("Warning")
                        .setMessage("You should accept this permission to make this application work properly. Thank you.")
                        .setPositiveButton("OK"){ _, _ ->
                            requestPermissions(
                                deniedPermission.toTypedArray(),
                                107
                            )
                        }
                        .setNegativeButton("CANCEL"){dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

    }

}
