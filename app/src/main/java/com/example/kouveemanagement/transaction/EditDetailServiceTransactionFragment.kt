package com.example.kouveemanagement.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailServiceTransaction
import com.example.kouveemanagement.model.DetailServiceTransactionResponse
import com.example.kouveemanagement.presenter.DetailServiceTransactionPresenter
import com.example.kouveemanagement.presenter.DetailServiceTransactionView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_edit_detail_service_transaction.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class EditDetailServiceTransactionFragment : Fragment(), DetailServiceTransactionView {

    private lateinit var idTransaction: String
    private lateinit var detailServiceTransaction: DetailServiceTransaction
    private lateinit var presenter: DetailServiceTransactionPresenter

    private var nameDropdown: MutableList<String> = arrayListOf()
    private var idDropdown: MutableList<String> = arrayListOf()
    private lateinit var idService: String

    private var state = "edit"

    companion object{
        private const val detailServiceTransaction = "input"
        fun newInstance(input: DetailServiceTransaction) : EditDetailServiceTransactionFragment {
            val fragment = EditDetailServiceTransactionFragment()
            val bundle = Bundle().apply {
                putParcelable(detailServiceTransaction, input)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailServiceTransaction = arguments?.getParcelable("input")!!
        return inflater.inflate(R.layout.fragment_edit_detail_service_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(detailServiceTransaction)
        presenter = DetailServiceTransactionPresenter(this, Repository())
        idTransaction = detailServiceTransaction.id_transaction.toString()
        idService = detailServiceTransaction.id_service.toString()
        btn_edit.setOnClickListener {
            quantity.isEnabled = true
            btn_edit.visibility = View.GONE
            btn_save.visibility = View.VISIBLE
            btn_delete.visibility = View.VISIBLE
        }
        btn_save.setOnClickListener{
            if (isValid()){
                state = "edit"
                getData()
                presenter.editDetailServiceTransaction(detailServiceTransaction)
            }
        }
        btn_delete.setOnClickListener {
            state = "delete"
            presenter.deleteDetailServiceTransaction(idTransaction, idService)
        }
        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun setDropdown(detailServiceTransaction: DetailServiceTransaction){
        nameDropdown = AddTransactionActivity.nameServiceDropdown
        idDropdown = AddTransactionActivity.idServiceDropdown
        for (i in idDropdown.indices){
            if (idDropdown[i] == detailServiceTransaction.id_service){
                service.setText(nameDropdown[i])
            }
        }
    }

    private fun setData(detailServiceTransaction: DetailServiceTransaction){
        setDropdown(detailServiceTransaction)
        quantity.setText(detailServiceTransaction.quantity.toString())
    }

    private fun getData(){
        val quantity = quantity.text.toString()
        detailServiceTransaction = DetailServiceTransaction(idTransaction, idService, quantity = quantity.toInt())
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
        progressbar.visibility = View.VISIBLE
    }

    override fun hideDetailServiceTransactionLoading() {
        progressbar.visibility = View.GONE
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        if (state == "edit"){
            Toast.makeText(context, "Success to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Success to delete", Toast.LENGTH_SHORT).show()
        }
        startActivity<AddTransactionActivity>("type" to "service")
    }

    override fun detailServiceTransactionFailed() {
        if (state == "edit"){
            Toast.makeText(context, "Failed to edit", Toast.LENGTH_SHORT).show()
        }else if (state == "delete"){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        }    }

}
