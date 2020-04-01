package com.example.kouveemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.kouveemanagement.adapter.MenuRecyclerViewAdapter
import com.example.kouveemanagement.employee.EmployeeManagementActivity
import com.example.kouveemanagement.model.Menu
import com.example.kouveemanagement.orderproduct.OrderProductActivity
import com.example.kouveemanagement.persistent.AppDatabase
import com.example.kouveemanagement.persistent.CurrentUser
import com.example.kouveemanagement.pet.PetTypeManagementActivity
import com.example.kouveemanagement.pet.PetSizeManagementActivity
import com.example.kouveemanagement.product.ProductManagementActivity
import com.example.kouveemanagement.search.SearchResultActivity
import com.example.kouveemanagement.service.ServiceManagementActivity
import com.example.kouveemanagement.supplier.SupplierManagementActivity
import kotlinx.android.synthetic.main.activity_owner.*
import org.jetbrains.anko.startActivity

class OwnerActivity : AppCompatActivity() {

    private var menu: MutableList<Menu> = mutableListOf()

    companion object {
        var database: AppDatabase? = null
        var currentUser: CurrentUser? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuInitialization()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "kouvee-db").build()
        setContentView(R.layout.activity_owner)
        setMenu()
        getCurrentUser()
        btn_logout.setOnClickListener {
            showLogoutConfirm()
        }
        fab_search.setOnClickListener {
            showDialogSearch()
        }
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
                else -> Toast.makeText(this, "Sorry, you can not access that.", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Yeay!", Toast.LENGTH_SHORT).show()
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
            .setTitle("Confirmation")
            .setMessage("Are you sure to log out ?")
        confirm.setNegativeButton("NO") { _, _ ->
            Toast.makeText(this, "Stay here.", Toast.LENGTH_SHORT).show()
        }
        confirm.setPositiveButton("YES") { _, _ ->
            val thread = Thread {
                currentUser?.let { database?.currentUserDao()?.deleteCurrentUser(it) }
                database?.clearAllTables()
                startActivity<MainActivity>()
            }
            thread.start()
        }
        confirm.show()
    }

    override fun onBackPressed() {
        showLogoutConfirm()
    }

    //SEARCH
    private fun showDialogSearch(){
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)

        val searchView = dialog.findViewById<SearchView>(R.id.search_view)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        val searchDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btnClose.setOnClickListener {
            searchDialog.dismiss()
        }

        getResultSearch(searchView)
    }

    private fun getResultSearch(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    return false
                }
                Toast.makeText(this@OwnerActivity, "Search for $query", Toast.LENGTH_LONG).show()
                startActivity<SearchResultActivity>("query" to query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}
