package com.example.kouveemanagement.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    companion object {
        fun newInstance() = AddServiceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            getData()
            presenter = ServicePresenter(this, Repository())
            presenter.addService(service)
        }

        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun getData(){
        val name = name.text.toString()
        val price = price.text.toString()

        service = Service(null, "1", name, price.toDouble())
    }

    override fun showServiceLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideServiceLoading() {
        progressbar.visibility = View.INVISIBLE
        btn_add.visibility = View.VISIBLE    }

    override fun serviceSuccess(data: ServiceResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<ServiceManagementActivity>()
    }

    override fun serviceFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

}
