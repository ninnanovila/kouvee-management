package com.example.kouveemanagement.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Service
import com.example.kouveemanagement.model.ServiceResponse
import com.example.kouveemanagement.presenter.ServicePresenter
import com.example.kouveemanagement.presenter.ServiceView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_service.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddServiceFragment : Fragment(), ServiceView {

    private lateinit var service: Service
    private lateinit var presenter: ServicePresenter

    //For Dropdown
    private var sizeDropdown: MutableList<String> = arrayListOf()
    private var idSizeList: MutableList<String> = arrayListOf()
    private lateinit var idSize: String

    companion object {
        fun newInstance() = AddServiceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sizeDropdown = ServiceManagementActivity.namePetSize
        idSizeList = ServiceManagementActivity.idPetSize
        idSize = idSizeList[0]
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = ServicePresenter(this, Repository())
                presenter.addService(service)
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        setSizeDropDown()
    }

    private fun getData(){
        val name = name.text.toString()
        val price = price.text.toString()
        service = Service(id_size = idSize, name = name, price = price.toDouble())
    }

    private fun isValid(): Boolean {
        if (name.text.isNullOrEmpty()){
            name.error = R.string.error_name.toString()
            return false
        }
        if (price.text.isNullOrEmpty()){
            price.error = R.string.error_price.toString()
            return false
        }
        return true
    }

    override fun showServiceLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideServiceLoading() {
        progressbar.visibility = View.GONE
        btn_add.visibility = View.VISIBLE
    }

    override fun serviceSuccess(data: ServiceResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ServiceManagementActivity>()
    }

    override fun serviceFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun setSizeDropDown(){
        val adapter = context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item, sizeDropdown)
        }
        size_dropdown.setAdapter(adapter)
        size_dropdown.setOnItemClickListener { _, _, position, _ ->
            idSize = idSizeList[position]
            Toast.makeText(context, "ID SIZE : $idSize", Toast.LENGTH_LONG).show()
        }
    }

}
