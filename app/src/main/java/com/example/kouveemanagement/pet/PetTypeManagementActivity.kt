package com.example.kouveemanagement.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.PetRecyclerViewAdapter
import com.example.kouveemanagement.model.PetType
import com.example.kouveemanagement.model.PetTypeResponse
import com.example.kouveemanagement.presenter.PetTypePresenter
import com.example.kouveemanagement.presenter.PetTypeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_pet_type_management.*
import org.jetbrains.anko.startActivity

class PetTypeManagementActivity : AppCompatActivity(), PetTypeView {

    private val petTypesTemp = ArrayList<PetType>()
    private var temps = ArrayList<PetType>()
    private var petTypesList: MutableList<PetType> = mutableListOf()

    private lateinit var presenterType: PetTypePresenter
    private lateinit var petTypesAdapter: PetRecyclerViewAdapter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private var edit = false

    companion object{
        var petTypes: MutableList<PetType> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_type_management)
        presenterType = PetTypePresenter(this, Repository())
        presenterType.getAllPetType()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        petTypesAdapter = PetRecyclerViewAdapter("type", petTypesList, {}, mutableListOf(), {})
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = PetRecyclerViewAdapter("type", petTypes, {
                    showPetType(it)
                }, mutableListOf(),{})
                query?.let { petTypesAdapter.filterPet(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = PetRecyclerViewAdapter("type", petTypes, {
                    showPetType(it)
                }, mutableListOf(),{})
                newText?.let { petTypesAdapter.filterPet(it) }
                return false
            }
        })
        fabAnimation()
        show_all.setOnClickListener {
            CustomFun.createToolTips(this, "L").showAlignTop(show_all)
            temps = petTypesTemp
            getList()
        }
        show_en.setOnClickListener {
            CustomFun.createToolTips(this, "C").showAlignTop(show_en)
            val filtered = petTypesTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<PetType>
            getList()
        }
        show_dis.setOnClickListener {
            CustomFun.createToolTips(this, "R").showAlignTop(show_dis)
            val filtered = petTypesTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<PetType>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenterType.getAllPetType()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = PetRecyclerViewAdapter("type", temps, {}, mutableListOf()){}
        }else{
            if(sort_switch.isChecked){
                val sorted = temps.sortedBy { it.name }
                recyclerview.adapter = PetRecyclerViewAdapter("type", sorted as MutableList<PetType>, {
                    showPetType(it)
                }, mutableListOf()){}
            }else{
                recyclerview.adapter = PetRecyclerViewAdapter("type", temps, {
                    showPetType(it)
                }, mutableListOf()){}
            }
        }
        petTypesAdapter.notifyDataSetChanged()
    }

    override fun showPetTypeLoading() {
        if (edit){
            CustomFun.warningSnackBar(container, baseContext, "Processing..")
        }else{
            swipe_rv.isRefreshing = true
        }
    }

    override fun hidePetTypeLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        clearList()
        if (!edit){
            val temp: List<PetType> = data?.pettype ?: emptyList()
            if (temp.isEmpty()){
                CustomFun.neutralSnackBar(container, baseContext, "Pet Type empty")
            }else{
                petTypesList.addAll(temp)
                petTypesTemp.addAll(temp)
                temps = petTypesTemp
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = PetRecyclerViewAdapter("type", petTypesList, {
                    showPetType(it)
                    Toast.makeText(this, "Type : " + it.name, Toast.LENGTH_SHORT).show()
                }, mutableListOf(),{})
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }else{
            startActivity<PetTypeManagementActivity>()
            infoDialog.dismiss()
            CustomFun.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun petTypeFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        petTypesList.clear()
        petTypesTemp.clear()
    }

    private fun showPetType(petType: PetType){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnDelete = dialog.findViewById<Button>(R.id.btn_cancel)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        val id = petType.id.toString()
        name.setText(petType.name)
        createdAt.text = petType.created_at
        if (petType.updated_at.isNullOrBlank()){
            updatedAt.text = "-"
        }else{
            updatedAt.text = petType.updated_at
        }
        if (petType.deleted_at.isNullOrBlank()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = petType.deleted_at
        }
        if (petType.deleted_at !== null){
            btnEdit.visibility = View.GONE
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .show()

        btnEdit.setOnClickListener {
            btnEdit.visibility = View.GONE
            name.isEnabled = true
            btnSave.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            edit = true
            val newName = name.text.toString()
            if (newName.isEmpty()){
                name.error = getString(R.string.error_name)
            }else{
                val newPetType = PetType(id, newName)
                presenterType.editPetType(id, newPetType)
            }
        }

        btnDelete.setOnClickListener {
            edit = true
            presenterType.deletePetType(id)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){
        fab_add.setOnClickListener {
            val fragment: Fragment = AddPetFragment.newInstance("type")
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }


}
