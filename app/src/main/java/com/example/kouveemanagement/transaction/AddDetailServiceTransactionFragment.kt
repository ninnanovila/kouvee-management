package com.example.kouveemanagement.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailServiceTransaction
import com.example.kouveemanagement.model.DetailServiceTransactionResponse
import com.example.kouveemanagement.presenter.DetailServiceTransactionPresenter
import com.example.kouveemanagement.presenter.DetailServiceTransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_add_detail_service_transaction.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddDetailServiceTransactionFragment : Fragment(), DetailServiceTransactionView {

    private lateinit var idTransaction: String
    private lateinit var detailServiceTransaction: DetailServiceTransaction
    private lateinit var presenter: DetailServiceTransactionPresenter

    private lateinit var idService: String
    private var services = ServiceTransactionActivity.services
    private var serviceName: MutableList<String> = arrayListOf()
    private var serviceId: MutableList<String> = arrayListOf()

    companion object{
        fun newInstance() = AddDetailServiceTransactionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_detail_service_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setServiceName()
        idTransaction = AddTransactionActivity.idTransaction
        idService = "-1"
        btn_add.setOnClickListener {
            if (isValid()){
                getData()
                presenter = DetailServiceTransactionPresenter(this, Repository())
                presenter.addDetailServiceTransaction(detailServiceTransaction)
            }else if (idService == "-1"){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please choose service")
            }
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    fun getData(){
        val quantity = quantity.text.toString()
        detailServiceTransaction = DetailServiceTransaction(id_transaction = idTransaction, id_service = idService, quantity = quantity.toInt())
    }

    private fun isValid(): Boolean {
        if(quantity.text.isNullOrEmpty()){
            quantity.error = getString(R.string.error_quantity)
            return false
        }else if (quantity.text.toString() == "0"){
            quantity.error = getString(R.string.error_zero_quantity)
            return false
        }
        return true
    }

    override fun showDetailServiceTransactionLoading() {
        btn_add.startAnimation()
    }

    override fun hideDetailServiceTransactionLoading() {
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        startActivity<AddTransactionActivity>("type" to "service")
    }

    override fun detailServiceTransactionFailed(data: String) {
        btn_add.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    private fun setServiceDropdown(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceName)
        service_dropdown.setAdapter(adapter)
        service_dropdown.setOnItemClickListener { _, _, position, _ ->
            idService = serviceId[position]
            val name = serviceName[position]
            Toast.makeText(context, "Service : $name", Toast.LENGTH_LONG).show()
        }
    }

    private fun setServiceName(){
        for (service in services){
            if (service.id_size == AddTransactionActivity.idOfSize){
                serviceId.add(service.id.toString())
                serviceName.add(service.name.toString())
            }
        }
        setServiceDropdown()
    }

}
