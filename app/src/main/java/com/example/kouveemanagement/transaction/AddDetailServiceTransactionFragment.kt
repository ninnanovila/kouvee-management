package com.example.kouveemanagement.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var idService: String

    companion object{
        fun newInstance() = AddDetailServiceTransactionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_detail_service_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idTransaction = AddTransactionActivity.idTransaction
        nameDropdown = AddTransactionActivity.nameServiceDropdown
        idDropdown = AddTransactionActivity.idServiceDropdown
        idService = idDropdown[0]
        btn_add.setOnClickListener {
            getData()
            presenter = DetailServiceTransactionPresenter(this, Repository())
            presenter.addDetailServiceTransaction(detailServiceTransaction)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        setServiceDropdown()
    }

    fun getData(){
        val quantity = quantity.text.toString()
        detailServiceTransaction = DetailServiceTransaction(id_transaction = idTransaction, id_service = idService, quantity = quantity.toInt())
    }

    override fun showDetailServiceTransactionLoading() {
        btn_add.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailServiceTransactionLoading() {
        btn_add.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        Toast.makeText(context, "Success Detail Service", Toast.LENGTH_SHORT).show()
        startActivity<AddTransactionActivity>("type" to "service")
    }

    override fun detailServiceTransactionFailed() {
        Toast.makeText(context, "Failed Detail Service", Toast.LENGTH_SHORT).show()
    }

    private fun setServiceDropdown(){
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, nameDropdown)
        }
        service_dropdown.setAdapter(adapter)
        service_dropdown.setOnItemClickListener { _, _, position, _ ->
            idService = idDropdown[position]
            Toast.makeText(context, "ID PRODUCT : $idService", Toast.LENGTH_LONG).show()
        }
    }

}
