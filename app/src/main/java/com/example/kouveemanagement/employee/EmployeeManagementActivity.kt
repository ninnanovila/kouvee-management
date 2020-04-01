package com.example.kouveemanagement.employee

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.EmployeeRecyclerViewAdapter
import com.example.kouveemanagement.model.Employee
import com.example.kouveemanagement.model.EmployeeResponse
import com.example.kouveemanagement.presenter.EmployeePresenter
import com.example.kouveemanagement.presenter.EmployeeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_employee_management.*
import org.jetbrains.anko.startActivity

class EmployeeManagementActivity : AppCompatActivity(), EmployeeView {

    private var temps = ArrayList<Employee>()
    private val employeesTemp = ArrayList<Employee>()
    private var employeesList: MutableList<Employee> = mutableListOf()

    private lateinit var presenter: EmployeePresenter
    private lateinit var employeeAdapter: EmployeeRecyclerViewAdapter

    private lateinit var dialog: View

    companion object{
        var employees: MutableList<Employee> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_management)
        presenter = EmployeePresenter(this, Repository())
        presenter.getAllEmployee()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        employeeAdapter = EmployeeRecyclerViewAdapter(employeesList) {}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = EmployeeRecyclerViewAdapter(employees){
                    showDialog(it)
                }
                query?.let { employeeAdapter.filterEmployee(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = EmployeeRecyclerViewAdapter(employees){
                    showDialog(it)
                }
                newText?.let { employeeAdapter.filterEmployee(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddEmployeeFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_all.setOnClickListener {
            temps = employeesTemp
            getList()
        }
        show_en.setOnClickListener {
            val filtered = employeesTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<Employee>
            getList()
        }
        show_dis.setOnClickListener {
            val filtered = employeesTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<Employee>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
    }

    private fun getList(){
        if(sort_switch.isChecked){
            val sorted = temps.sortedBy { it.name }
            recyclerview.adapter = EmployeeRecyclerViewAdapter(sorted as MutableList<Employee>){
                showDialog(it)
            }
        }else{
            recyclerview.adapter = EmployeeRecyclerViewAdapter(temps as MutableList<Employee>){
                showDialog(it)
            }
        }
        employeeAdapter.notifyDataSetChanged()
    }

    override fun showEmployeeLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideEmployeeLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        val temp: List<Employee> = data?.employees ?: emptyList()
        if (temp.isEmpty()){
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
        }else{
            employeesList.addAll(temp)
            employeesTemp.addAll(temp)
            temps.addAll(temp)
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = EmployeeRecyclerViewAdapter(employeesList){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun employeeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(employee: Employee){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_employee, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val phoneNumber = dialog.findViewById<TextView>(R.id.phone_number)
        val role = dialog.findViewById<TextView>(R.id.role)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)

        name.text = employee.name.toString()
        address.text = employee.address.toString()
        birthdate.text = employee.birthdate.toString()
        phoneNumber.text = employee.phone_number.toString()
        role.text = employee.role.toString()

        if (employee.deleted_at != null){
            btnEdit.visibility = View.GONE
        }

        val infoDialog= AlertDialog.Builder(this)
            .setView(dialog)
            .show()

        btnEdit.setOnClickListener {
            startActivity<EditEmployeeActivity>("employee" to employee)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }
}
