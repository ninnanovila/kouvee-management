package com.example.kouveemanagement

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.kouveemanagement.adapter.MenuRecyclerViewAdapter
import com.example.kouveemanagement.customer.CustomerManagementActivity
import com.example.kouveemanagement.customerpet.CustomerPetManagementActivity
import com.example.kouveemanagement.model.Menu
import com.example.kouveemanagement.persistent.AppDatabase
import com.example.kouveemanagement.persistent.CurrentUser
import com.example.kouveemanagement.transaction.ProductTransactionActivity
import com.example.kouveemanagement.transaction.ServiceTransactionActivity
import kotlinx.android.synthetic.main.activity_customer_service.*
import org.jetbrains.anko.startActivity

class CustomerServiceActivity : AppCompatActivity() {

    private var menu: MutableList<Menu> = mutableListOf()

    companion object{
        var database: AppDatabase? = null
        var currentUser: CurrentUser? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuInitialization()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "kouvee-db").build()
        setContentView(R.layout.activity_customer_service)
        if (!CustomFun.verifiedNetwork(this)) CustomFun.warningSnackBar(container, baseContext, "Please check internet connection")
        else CustomFun.welcomeSnackBar(container, baseContext, "Welcome Customer Service!")
        setMenu()
        getCurrentUser()
        btn_logout.setOnClickListener {
            showLogoutConfirm()
        }
    }

    private fun menuInitialization(){
        val name = resources.getStringArray(R.array.cs_menu)
        val desc = resources.getStringArray(R.array.cs_desc)
        val image = resources.obtainTypedArray(R.array.cs_image)
        menu.clear()
        for (i in name.indices){
            menu.add(Menu(name[i], desc[i], image.getResourceId(i,0)))
        }
        image.recycle()
    }

    private fun setMenu(){
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = MenuRecyclerViewAdapter(menu) {
            when(it.name) {
                //DATA MASTER
                "Customer" -> startActivity<CustomerManagementActivity>()
                "Customer's Pet" -> startActivity<CustomerPetManagementActivity>()
                //TRANSACTION
                "Product Transaction" -> startActivity<ProductTransactionActivity>()
                "Service Transaction" -> startActivity<ServiceTransactionActivity>()
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

}
