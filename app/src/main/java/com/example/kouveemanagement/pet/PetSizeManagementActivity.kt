package com.example.kouveemanagement.pet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.PetRecyclerViewAdapter
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetSizeResponse
import com.example.kouveemanagement.presenter.PetSizePresenter
import com.example.kouveemanagement.presenter.PetSizeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_pet_size_management.*
import org.jetbrains.anko.startActivity

class PetSizeManagementActivity : AppCompatActivity(), PetSizeView {

    private var petSizesList: MutableList<PetSize> = mutableListOf()
    private val petSizesTemp = ArrayList<PetSize>()
    private var temps = ArrayList<PetSize>()

    private lateinit var presenterSize: PetSizePresenter
    private lateinit var petSizesAdapter: PetRecyclerViewAdapter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private var edit = false

    companion object{
        var petSizes: MutableList<PetSize> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_size_management)
        presenterSize = PetSizePresenter(this, Repository())
        presenterSize.getAllPetSize()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        petSizesAdapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizesList) {}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {},
                    petSizes, {
                    showPetSize(it)
                })
                query?.let { petSizesAdapter.filterPet(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sort_switch.isChecked = false
                recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {},
                    petSizes, {
                    showPetSize(it)
                })
                newText?.let { petSizesAdapter.filterPet(it) }
                return false
            }
        })
        fab_add.setOnClickListener {
            val fragment: Fragment = AddPetFragment.newInstance("size")
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
        show_all.setOnClickListener {
            CustomFun.createToolTips(this, "L").showAlignTop(show_all)
            temps = petSizesTemp
            getList()
        }
        show_en.setOnClickListener {
            CustomFun.createToolTips(this, "C").showAlignTop(show_en)
            val filtered = petSizesTemp.filter { it.deleted_at === null }
            temps = filtered as ArrayList<PetSize>
            getList()
        }
        show_dis.setOnClickListener {
            CustomFun.createToolTips(this, "R").showAlignTop(show_dis)
            val filtered = petSizesTemp.filter { it.deleted_at !== null }
            temps = filtered as ArrayList<PetSize>
            getList()
        }
        sort_switch.setOnClickListener {
            getList()
        }
        swipe_rv.setOnRefreshListener {
            presenterSize.getAllPetSize()
        }
        CustomFun.setSwipe(swipe_rv)
    }

    private fun getList(){
        if (temps.isNullOrEmpty()){
            CustomFun.warningSnackBar(container, baseContext, "Empty data")
            recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, temps){}
        }else{
            if(sort_switch.isChecked){
                val sorted = temps.sortedBy { it.name }
                recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, sorted as MutableList<PetSize>){
                    showPetSize(it)
                }
            }else{
                recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, temps){
                    showPetSize(it)
                }
            }
        }
        petSizesAdapter.notifyDataSetChanged()
    }

    override fun showPetSizeLoading() {
        if (edit){
            CustomFun.warningSnackBar(container, baseContext, "Processing..")
        }else{
            swipe_rv.isRefreshing = true
        }
    }

    override fun hidePetSizeLoading() {
        swipe_rv.isRefreshing = false
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
        clearList()
        if (!edit){
            val temp: List<PetSize> = data?.petsize ?: emptyList()
            if (temp.isEmpty()){
                CustomFun.neutralSnackBar(container, baseContext, "Pet sizes empty")
            }else{
                petSizesList.addAll(temp)
                petSizesTemp.addAll(temp)
                temps = petSizesTemp
                recyclerview.layoutManager = LinearLayoutManager(this)
                recyclerview.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizesList, {
                    showPetSize(it)
                    Toast.makeText(this, "Size : "+it.name, Toast.LENGTH_LONG).show()
                })
                CustomFun.successSnackBar(container, baseContext, "Ok, success")
            }
        }else{
            startActivity<PetSizeManagementActivity>()
            infoDialog.dismiss()
            CustomFun.successSnackBar(container, baseContext, "Ok, success")
        }
    }

    override fun petSizeFailed(data: String) {
        CustomFun.failedSnackBar(container, baseContext, data)
    }

    private fun clearList(){
        petSizesList.clear()
        petSizesTemp.clear()
    }

    private fun showPetSize(petSize: PetSize){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnDelete = dialog.findViewById<Button>(R.id.btn_cancel)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit)
        val id = petSize.id.toString()
        name.setText(petSize.name)
        createdAt.text = petSize.created_at
        if (petSize.updated_at.isNullOrBlank()){
            updatedAt.text = "-"
        }else{
            updatedAt.text = petSize.updated_at
        }
        if (petSize.deleted_at.isNullOrBlank()){
            deletedAt.text = "-"
        }else{
            deletedAt.text = petSize.deleted_at
        }

        if (petSize.deleted_at != null){
            btnEdit.visibility = View.GONE
        }

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
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
                val newPetSize = PetSize(id, newName)
                presenterSize.editPetSize(id, newPetSize)
            }
        }

        btnDelete.setOnClickListener {
            edit = true
            presenterSize.deletePetSize(id)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }
}
