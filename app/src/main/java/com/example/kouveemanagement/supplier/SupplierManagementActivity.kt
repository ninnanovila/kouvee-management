package com.example.kouveemanagement.supplier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kouveemanagement.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_supplier_management.*

class SupplierManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_management)

        setTabLayout()
    }

    fun setTabLayout(){
        viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> {
                        AllSupplierFragment.newInstance()
                    }
                    else -> {
                        AddSupplierFragment.newInstance()
                    }
                }
            }
        }

        TabLayoutMediator(tablayout, viewpager) {tab, position ->
            tab.text = when(position) {
                0 -> "Show All"
                else -> "Add"
            }
        }.attach()
    }
}
