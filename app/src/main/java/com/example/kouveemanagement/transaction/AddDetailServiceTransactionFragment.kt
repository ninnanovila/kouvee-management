package com.example.kouveemanagement.transaction

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.ServiceRecyclerViewAdapter
import com.example.kouveemanagement.model.DetailServiceTransaction
import com.example.kouveemanagement.model.DetailServiceTransactionResponse
import com.example.kouveemanagement.model.Service
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
    private lateinit var serviceAdapter: ServiceRecyclerViewAdapter

    private var servicesBasedSize: MutableList<Service> = mutableListOf()

    companion object{
        fun newInstance() = AddDetailServiceTransactionFragment()
        var services: MutableList<Service> = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_detail_service_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (AddTransactionActivity.idOfSize == "-1"){
            setServicesBasedSize(ServiceTransactionActivity.idOfSize)
        }else if (ServiceTransactionActivity.idOfSize == "-1"){
            setServicesBasedSize(AddTransactionActivity.idOfSize)
        }
        serviceAdapter = ServiceRecyclerViewAdapter(servicesBasedSize, ServiceTransactionActivity.petSizes){}
        idTransaction = AddTransactionActivity.idTransaction
        idService = "-1"
        show_detail.setOnClickListener {
            startActivity<AddTransactionActivity>("type" to "service")
        }
        btn_close.setOnClickListener {
            startActivity<AddTransactionActivity>("type" to "service")
        }
        setServices()
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerview.adapter = ServiceRecyclerViewAdapter(services, ServiceTransactionActivity.petSizes){
                    showDialog(it)
                }
                query?.let { serviceAdapter.filterForTransaction(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerview.adapter = ServiceRecyclerViewAdapter(services, ServiceTransactionActivity.petSizes){
                    showDialog(it)
                }
                newText?.let { serviceAdapter.filterForTransaction(it) }
                return false
            }

        })
    }

    fun getData(quantity: String){
        detailServiceTransaction = DetailServiceTransaction(id_transaction = idTransaction, id_service = idService, quantity = quantity.toInt())
    }

    private fun isValid(editText: EditText): Boolean {
        if(editText.text.isNullOrEmpty()){
            editText.error = getString(R.string.error_quantity)
            return false
        }else if (editText.text.toString() == "0"){
            editText.error = getString(R.string.error_zero_quantity)
            return false
        }
        return true
    }

    private fun setServices(){
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = ServiceRecyclerViewAdapter(servicesBasedSize, ServiceTransactionActivity.petSizes){
            idService = it.id.toString()
            showDialog(it)
        }
    }

    private fun showDialog(service: Service){
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.item_quantity, null)
        val name = dialog.findViewById<EditText>(R.id.name)
        val stock = dialog.findViewById<EditText>(R.id.stock)
        val quantity = dialog.findViewById<EditText>(R.id.quantity)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        var txt = ""
        for (size in ServiceTransactionActivity.petSizes){
            if (service.id_size.equals(size.id)){
                txt = size.name.toString()
            }
        }

        name.setText(service.name.toString()+" $txt")
        stock.visibility = View.GONE
        quantity.setText("0")

        val showDialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialog)
            .show()

        btnAdd.setOnClickListener {
            if (isValid(quantity)){
                getData(quantity.text.toString())
                presenter = DetailServiceTransactionPresenter(this, Repository())
                presenter.addDetailServiceTransaction(detailServiceTransaction)
            }else if (idService == "-1"){
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please choose service")
            }
            showDialog.dismiss()
        }

        btnClose.setOnClickListener {
            showDialog.dismiss()
        }
    }

    override fun showDetailServiceTransactionLoading() {
    }

    override fun hideDetailServiceTransactionLoading() {
    }

    override fun detailServiceTransactionSuccess(data: DetailServiceTransactionResponse?) {
        CustomFun.successSnackBar(requireView(), requireContext(), "Success add!")
    }

    override fun detailServiceTransactionFailed(data: String) {
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    private fun setServicesBasedSize(id: String){
        for (service in ServiceTransactionActivity.enService){
            if (service.id_size == id){
                servicesBasedSize.add(service)
            }
        }
    }

}
