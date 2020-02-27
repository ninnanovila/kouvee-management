package com.example.kouveemanagement.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_service)

        setData()
        presenter = ServicePresenter(this, Repository())

        btn_save.setOnClickListener {
            getData()
            presenter.editService(id, service)
        }

        btn_delete.setOnClickListener {
            presenter.deleteService(id)
        }

        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
    }

    private fun setData(){
        val service: Service? = intent.getParcelableExtra("service")
        id = service?.id.toString()
        name.setText(service?.name)
        price.setText(service?.price.toString())
        created_at.text = service?.created_at
        updated_at.text = service?.updated_at
        deleted_at.text = service?.deleted_at
    }

    private fun getData(){
        val name = name.text.toString()
        val price = price.text.toString()

        service = Service(id, "1", "1", name, price.toDouble(), null, null, null)
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
        btn_save.visibility = View.INVISIBLE
        btn_delete.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_save.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ServiceManagementActivity>()
    }

    override fun serviceFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<ServiceManagementActivity>()
    }
}
