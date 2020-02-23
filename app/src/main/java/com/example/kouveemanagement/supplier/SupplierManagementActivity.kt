package com.example.kouveemanagement.supplier

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.SupplierRcyclerViewAdapter
import com.example.kouveemanagement.model.Supplier
import com.example.kouveemanagement.model.SupplierResponse
import com.example.kouveemanagement.presenter.SupplierPresenter
import com.example.kouveemanagement.presenter.SupplierView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_supplier_management.*


class SupplierManagementActivity : AppCompatActivity(), SupplierView {

    private var suppliers: MutableList<Supplier> = mutableListOf()
    private lateinit var presenter: SupplierPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_management)

        presenter = SupplierPresenter(this, Repository())
        presenter.getAllSupplier()

        fab_add.setOnClickListener {
            val fragment: Fragment = AddSupplierFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }

//        setTabLayout()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun supplierSuccess(data: SupplierResponse?) {

        val temp: List<Supplier> = data?.suppliers ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                suppliers.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = this.let {
                SupplierRcyclerViewAdapter(suppliers) {
                    //                    showDialog(it)
                    Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
                }
            }


        }
    }

    override fun supplierFailed() {
    }

//    fun setTabLayout(){
//        viewpager.adapter = object : FragmentStateAdapter(this) {
//            override fun getItemCount(): Int {
//                return 2
//            }
//
//            override fun createFragment(position: Int): Fragment {
//                return when(position){
//                    0 -> {
//                        AllSupplierFragment.newInstance()
//                    }
//                    else -> {
//                        AddSupplierFragment.newInstance()
//                    }
//                }
//            }
//        }
//
//        TabLayoutMediator(tablayout, viewpager) {tab, position ->
//            tab.text = when(position) {
//                0 -> "Show All"
//                else -> "Add"
//            }
//        }.attach()
//    }
}
