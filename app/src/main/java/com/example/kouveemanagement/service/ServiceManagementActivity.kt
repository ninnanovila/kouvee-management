package com.example.kouveemanagement.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ServiceRecyclerViewAdapter
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetSizeResponse
import com.example.kouveemanagement.model.Service
import com.example.kouveemanagement.model.ServiceResponse
import com.example.kouveemanagement.presenter.PetSizePresenter
import com.example.kouveemanagement.presenter.PetSizeView
import com.example.kouveemanagement.presenter.ServicePresenter
import com.example.kouveemanagement.presenter.ServiceView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_service_management.*
import org.jetbrains.anko.startActivity

class ServiceManagementActivity : AppCompatActivity(), ServiceView, PetSizeView {

    private var servicesList: MutableList<Service> = mutableListOf()
    private val servicesTemp = ArrayList<Service>()
    private var temps = ArrayList<Service>()

    private lateinit var presenter: ServicePresenter
    private lateinit var serviceAdapter: ServiceRecyclerViewAdapter

    private lateinit var dialog: View

    private lateinit var presenterS: PetSizePresenter

    companion object {
        var namePetSize: MutableList<String> = arrayListOf()
        var idPetSize: MutableList<String> = arrayListOf()
        var services: MutableList<Service> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_management)
        if (!CustomFun.verifiedNetwork(this)){
            CustomFun.warningSnackBar(container, baseContext, "Please check internet connection")
        }
        presenter = ServicePresenter(this, Repository())
        presenter.getAllService()
        presenterS = PetSizePresenter(this, Repository())
        presenterS.getAllPetSize()
        btn_home.setOnClickListener{
            startActivity<OwnerActivity>()
        }
        serviceAdapter = ServiceRecyclerViewAdapter(servicesList) {}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = ServiceRecyclerViewAdapter(services){
                    showDialog(it)
                }
                query?.let { serviceAdapter.filterService(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = ServiceRecyclerViewAdapter(services){
                    showDialog(it)
                }
                newText?.let { serviceAdapter.filterService(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddServiceFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_all.setOnClickListener {
            temps = servicesTemp
            getList()
        }
        show_en.setOnClickListener {
            val filtered = servicesTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<Service>
            getList()
        }
        show_dis.setOnClickListener {
            val filtered = servicesTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<Service>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenter.getAllService()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = ServiceRecyclerViewAdapter(temps as MutableList<Service>){}
        }else{
            if(sort_switch.isChecked){
                val sorted = temps.sortedBy { it.name }
                recyclerview.adapter = ServiceRecyclerViewAdapter(sorted as MutableList<Service>){
                    showDialog(it)
                }
            }else{
                recyclerview.adapter = ServiceRecyclerViewAdapter(temps as MutableList<Service>){
                    showDialog(it)
                }
            }
        }
        serviceAdapter.notifyDataSetChanged()
    }

    override fun showServiceLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideServiceLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.neutralSnackBar(container, baseContext, "Oops, no result")
        }else{
            clearList()
            servicesList.addAll(temp)
            servicesTemp.addAll(temp)
            temps = servicesTemp
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = ServiceRecyclerViewAdapter(servicesList){
                showDialog(it)
            }
            CustomFun.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun serviceFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        servicesList.clear()
        servicesTemp.clear()
    }

    private fun showDialog(service: Service){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_service, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val price = dialog.findViewById<TextView>(R.id.price)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = service.name.toString()
        price.text = service.price.toString()
        createdAt.text = service.created_at
        updatedAt.text = service.updated_at
        if (service.deleted_at.isNullOrEmpty()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = service.deleted_at
        }
        if (service.deleted_at != null){
            btnEdit.visibility = View.GONE
        }
        val infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .show()
        btnEdit.setOnClickListener {
            startActivity<EditServiceActivity>("service" to service)
        }
        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }

    override fun showPetSizeLoading() {
    }

    override fun hidePetSizeLoading() {
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
        val temp: List<PetSize> = data?.petsize ?: emptyList()
        if (temp.isEmpty()){
            CustomFun.neutralSnackBar(container, baseContext, "Pet size empty")
        }else{
            namePetSize.clear()
            idPetSize.clear()
            for (i in temp.indices){
                if (temp[i].deleted_at == null){
                    idPetSize.add(temp[i].id.toString())
                    namePetSize.add(temp[i].name.toString())
                }
            }
        }
    }

    override fun petSizeFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }
}
