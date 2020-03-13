package com.example.kouveemanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.Employee
import kotlinx.android.extensions.LayoutContainer

class EmployeeRecyclerViewAdapter(private val employees : MutableList<Employee>, private val listener: (Employee) -> Unit) : RecyclerView.Adapter<EmployeeRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = employees.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(employees[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var name: TextView = itemView.findViewById(R.id.name)
        private var phoneNumber: TextView = itemView.findViewById(R.id.phone_number)
        private var role: TextView = itemView.findViewById(R.id.role)

        fun bindItem(employee: Employee, listener: (Employee) -> Unit) {
            name.text = employee.name
            phoneNumber.text = employee.phone_number
            role.text = employee.role

            containerView.setOnClickListener{
                listener(employee)
            }
        }

    }

}
