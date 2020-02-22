package com.example.kouveemanagement.employee


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.EmployeeRecyclerViewAdapter
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_employee.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AllEmployeeFragment : Fragment(), EmployeeView {

    private var employees: MutableList<Employee> = mutableListOf()
    private lateinit var presenter: EmployeePresenter

    companion object {
        fun newInstance() = AllEmployeeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = EmployeePresenter(this, Repository())
        presenter.getAllEmployee()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        val temp: List<Employee> = data?.employees ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                employees.add(i, temp.get(i))
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                EmployeeRecyclerViewAdapter(employees){
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun employeeFailed() {

    }

    private fun showDialog(employee: Employee){

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_employee, null)

        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val phone_number = dialog.findViewById<TextView>(R.id.phone_number)
        val role = dialog.findViewById<TextView>(R.id.role)
        val btn_edit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = employee.name.toString()
        address.text = employee.address.toString()
        birthdate.text = employee.birthdate.toString()
        phone_number.text = employee.phone_number.toString()
        role.text = employee.role.toString()

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Employee Info")
            .show()

        btn_edit.setOnClickListener {
            startActivity<EditEmployeeActivity>("employee" to employee)
        }
    }


}
