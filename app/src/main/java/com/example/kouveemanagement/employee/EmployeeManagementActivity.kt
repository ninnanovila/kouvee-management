package com.example.kouveemanagement.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kouveemanagement.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_employee_management.*

class EmployeeManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_management)

        setTabLayout()
    }

    private fun setTabLayout(){
        viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> {
                        AllEmployeeFragment.newInstance()
                    }
                    else -> {
                        AddEmployeeFragment.newInstance()
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
