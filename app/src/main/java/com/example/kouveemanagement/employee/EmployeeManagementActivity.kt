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
import com.example.kouveemanagement.CustomView
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
        if (!CustomView.verifiedNetwork(this)){
            CustomView.warningSnackBar(container, baseContext, "Please check internet connection")
        }
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
        swipe_rv.setOnRefreshListener {
            presenter.getAllEmployee()
        }
        CustomView.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomView.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = EmployeeRecyclerViewAdapter(temps as MutableList<Employee>){}
        }else{
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
        }
        employeeAdapter.notifyDataSetChanged()
    }

    override fun showEmployeeLoading() {
        swipe_rv.isRefreshing = true
    }

    override fun hideEmployeeLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun employeeSuccess(data: EmployeeResponse?) {
        val temp: List<Employee> = data?.employees ?: emptyList()
        if (temp.isEmpty()){
            CustomView.neutralSnackBar(container, baseContext, "Employee empty")
        }else{
            clearList()
            employeesList.addAll(temp)
            employeesTemp.addAll(temp)
            temps = employeesTemp
            recyclerview.layoutManager = LinearLayoutManager(this)
            recyclerview.adapter = EmployeeRecyclerViewAdapter(employeesList){
                showDialog(it)
                Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
            }
            CustomView.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun employeeFailed(data: String) {
        CustomView.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        employeesList.clear()
        employeesTemp.clear()
    }

    private fun showDialog(employee: Employee){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_employee, null)
        val name = dialog.findViewById<TextView>(R.id.name)
        val address = dialog.findViewById<TextView>(R.id.address)
        val birthdate = dialog.findViewById<TextView>(R.id.birthdate)
        val phoneNumber = dialog.findViewById<TextView>(R.id.phone_number)
        val role = dialog.findViewById<TextView>(R.id.role)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        name.text = employee.name.toString()
        address.text = employee.address.toString()
        birthdate.text = employee.birthdate.toString()
        phoneNumber.text = employee.phone_number.toString()
        role.text = employee.role.toString()
        createdAt.text = employee.created_at
        updatedAt.text = employee.updated_at
        if (employee.deleted_at.isNullOrEmpty()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = employee.deleted_at
        }
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
