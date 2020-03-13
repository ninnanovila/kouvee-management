package com.example.kouveemanagement.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.Animation
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
    private lateinit var presenter: ServicePresenter
    private lateinit var adapter: ServiceRecyclerViewAdapter

    private lateinit var dialog: View
    private var isRotate = false

    private lateinit var presenterS: PetSizePresenter

    companion object {
        var namePetSize: MutableList<String> = arrayListOf()
        var idPetSize: MutableList<String> = arrayListOf()
        var services: MutableList<Service> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_management)
        presenter = ServicePresenter(this, Repository())
        presenter.getAllService()
        presenterS = PetSizePresenter(this, Repository())
        presenterS.getAllPetSize()
        btn_home.setOnClickListener{
            startActivity<OwnerActivity>()
        }
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val adapter = ServiceRecyclerViewAdapter(servicesList) {}
                recyclerview.adapter = ServiceRecyclerViewAdapter(services){
                    showDialog(it)
                }
                query?.let { adapter.filterData(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = ServiceRecyclerViewAdapter(servicesList) {}
                recyclerview.adapter = ServiceRecyclerViewAdapter(services){
                    showDialog(it)
                }
                newText?.let { adapter.filterData(it) }
                return false
            }

        })
        fabAnimation()
    }

    override fun showServiceLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideServiceLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        val temp: List<Service> = data?.services ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{
            for (i in temp.indices){
                servicesList.add(i, temp[i])
            }
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = ServiceRecyclerViewAdapter(servicesList){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun serviceFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(service: Service){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_service, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val price = dialog.findViewById<TextView>(R.id.price)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = service.name.toString()
        price.text = service.price.toString()

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

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
            val fragment: Fragment = AddServiceFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
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
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
        }else{
            if (namePetSize.isNotEmpty()){
                namePetSize.clear()
                idPetSize.clear()
                for (i in temp.indices){
                    idPetSize.add(i, temp[i].id.toString())
                    namePetSize.add(i, temp[i].name.toString())
                }
            }else{
                for (i in temp.indices){
                    idPetSize.add(i, temp[i].id.toString())
                    namePetSize.add(i, temp[i].name.toString())
                }
            }
        }
    }

    override fun petSizeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }
}
