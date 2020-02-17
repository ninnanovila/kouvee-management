package com.example.kouveemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.adapter.MenuRecyclerViewAdapter
import com.example.kouveemanagement.employee.EmployeeManagementActivity
import com.example.kouveemanagement.model.Menu
import com.example.kouveemanagement.product.ProductManagementActivity
import kotlinx.android.synthetic.main.activity_owner.*
import org.jetbrains.anko.startActivity

class OwnerActivity : AppCompatActivity() {

    private var menu: MutableList<Menu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialization()

        setContentView(R.layout.activity_owner)
        setMenu()
    }

    private fun initialization(){
        val name = resources.getStringArray(R.array.owner_menu)
        val desc = resources.getStringArray(R.array.owner_desc)

        menu.clear()

        for (i in name.indices){
            menu.add(Menu(name[i], desc[i]))
        }
    }

    private fun setMenu(){
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = MenuRecyclerViewAdapter(menu) {
            when(it.name) {
                "Employee" -> startActivity<EmployeeManagementActivity>()
                "Customer" -> startActivity<EmployeeManagementActivity>()
                "Service" -> startActivity<EmployeeManagementActivity>()
                "Pet Type" -> startActivity<EmployeeManagementActivity>()
                "Pet Size" -> startActivity<EmployeeManagementActivity>()
                "Supplier" -> startActivity<EmployeeManagementActivity>()
                "Product" -> startActivity<ProductManagementActivity>()
                "Customer Pet" -> startActivity<EmployeeManagementActivity>()
                "Product Order" -> startActivity<EmployeeManagementActivity>()
                "Product Transaction" -> startActivity<EmployeeManagementActivity>()
                "Service Transaction" -> startActivity<EmployeeManagementActivity>()
            }
            Toast.makeText(this, "Yeay!", Toast.LENGTH_SHORT).show()
        }
    }
}
