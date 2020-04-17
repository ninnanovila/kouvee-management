package com.example.kouveemanagement.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kouveemanagement.CustomView
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Service
import com.example.kouveemanagement.model.ServiceResponse
import com.example.kouveemanagement.presenter.ServicePresenter
import com.example.kouveemanagement.presenter.ServiceView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_edit_service.*
import org.jetbrains.anko.startActivity

class EditServiceActivity : AppCompatActivity(), ServiceView {

    private lateinit var id: String
    private lateinit var presenter: ServicePresenter
    private lateinit var service: Service

    private var sizeDropdown: MutableList<String> = arrayListOf()
    private var idSizeList: MutableList<String> = arrayListOf()
    private lateinit var idSize: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_service)
        setData()
        presenter = ServicePresenter(this, Repository())
        btn_save.setOnClickListener {
            if (isValid()){
                getData()
                presenter.editService(id, service)
            }
        }
        btn_cancel.setOnClickListener {
            presenter.deleteService(id)
        }
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
    }

    private fun setDropdown(service: Service){
        var position = 0
        sizeDropdown = ServiceManagementActivity.namePetSize
        idSizeList = ServiceManagementActivity.idPetSize
        for (i in idSizeList.indices){
            if (idSizeList[i] == service.id_size){
                position = i
                idSize = idSizeList[i]
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeDropdown)
        size_dropdown.setAdapter(adapter)
        size_dropdown.setText(sizeDropdown[position], true)
        size_dropdown.setOnItemClickListener { _, _, position, _ ->
            idSize = idSizeList[position]
            val name = sizeDropdown[position]
            Toast.makeText(this, "Size : $name", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(){
        val service: Service? = intent.getParcelableExtra("service")
        id = service?.id.toString()
        name.setText(service?.name)
        price.setText(service?.price.toString())
        created_at.setText(service?.created_at)
        updated_at.setText(service?.updated_at)
        if (service?.deleted_at.isNullOrBlank()){
            deleted_at.setText("-")
        }else{
            deleted_at.setText(service?.deleted_at)
        }
        service?.let { setDropdown(it) }
    }

    private fun getData(){
        val name = name.text.toString()
        val price = price.text.toString()
        service = Service(id, idSize, name, price.toDouble())
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        if (price.text.isNullOrEmpty()){
            price.error = getString(R.string.error_price)
            return false
        }else if(Integer.parseInt(price.text.toString()) < 1){
            price.error = getString(R.string.error_zero_price)
            return false
        }
        return true
    }

    override fun showServiceLoading() {
        btn_save.startAnimation()
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun hideServiceLoading() {
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        startActivity<ServiceManagementActivity>()
    }

    override fun serviceFailed(data: String) {
        btn_save.revertAnimation()
        btn_cancel.visibility = View.VISIBLE
        CustomView.failedSnackBar(container, baseContext, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<ServiceManagementActivity>()
    }
}
