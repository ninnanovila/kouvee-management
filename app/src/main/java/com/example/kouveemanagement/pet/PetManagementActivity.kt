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
import com.example.kouveemanagement.OwnerActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.PetRecyclerViewAdapter
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetSizeResponse
import com.example.kouveemanagement.model.PetType
import com.example.kouveemanagement.model.PetTypeResponse
import com.example.kouveemanagement.presenter.PetSizePresenter
import com.example.kouveemanagement.presenter.PetSizeView
import com.example.kouveemanagement.presenter.PetTypePresenter
import com.example.kouveemanagement.presenter.PetTypeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.activity_pet_management.*
import kotlinx.android.synthetic.main.dialog_detail_pet.view.*
import org.jetbrains.anko.startActivity

class PetManagementActivity : AppCompatActivity(), PetSizeView, PetTypeView {

    private var petSizesList: MutableList<PetSize> = mutableListOf()
    private lateinit var presentersize: PetSizePresenter

    private var petTypesList: MutableList<PetType> = mutableListOf()
    private lateinit var presentertype: PetTypePresenter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog

    private var isAll = true

    companion object{
        var petSizes: MutableList<PetSize> = mutableListOf()
        var petTypes: MutableList<PetType> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_management)
        presentersize = PetSizePresenter(this, Repository())
        presentersize.getAllPetSize()
        presentertype = PetTypePresenter(this, Repository())
        presentertype.getAllPetType()
        btn_home.setOnClickListener {
            startActivity<OwnerActivity>()
        }
        val adapterSize = PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizesList) {}
        val adapterType = PetRecyclerViewAdapter("size", petTypesList, {}, mutableListOf(), {})
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerviewsize.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizes, {
                    showPetSize(it)
                })
                recyclerviewtype.adapter = PetRecyclerViewAdapter("type", petTypes, {
                    showPetType(it)
                }, mutableListOf(),{})
                query?.let { adapterSize.filterPet(it) }
                query?.let { adapterType.filterPet(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerviewsize.adapter = PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizes, {
                        showPetSize(it)
                })
                recyclerviewtype.adapter = PetRecyclerViewAdapter("type", petTypes, {
                    showPetType(it)
                }, mutableListOf(),{})
                newText?.let { adapterSize.filterPet(it) }
                newText?.let { adapterType.filterPet(it) }
                return false
            }

        })
        fabAnimation()
    }

    override fun showPetSizeLoading() {
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        dialog.btn_save.visibility = View.INVISIBLE
        dialog.btn_delete.visibility = View.INVISIBLE
        dialog.progressbar.visibility = View.VISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hidePetSizeLoading() {
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        dialog.btn_save.visibility = View.VISIBLE
        dialog.btn_delete.visibility = View.VISIBLE
        dialog.progressbar.visibility = View.INVISIBLE
        progressbar.visibility = View.GONE
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {

        if (isAll){
            val temp: List<PetSize> = data?.petsize ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                for (i in temp.indices){
                    petSizesList.add(i, temp[i])
                }
                recyclerviewsize.layoutManager = LinearLayoutManager(this)
                recyclerviewsize.adapter =
                    PetRecyclerViewAdapter("size", mutableListOf(), {}, petSizesList, {
                        showPetSize(it)
                        Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
                    })
            }
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            startActivity<PetManagementActivity>()
        }
    }

    override fun petSizeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun showPetTypeLoading() {
    }

    override fun hidePetTypeLoading() {
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        if (isAll){
            val temp: List<PetType> = data?.pettype ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                for (i in temp.indices){
                    petTypesList.add(i, temp[i])
                }
                recyclerviewtype.layoutManager = LinearLayoutManager(this)
                recyclerviewtype.adapter = PetRecyclerViewAdapter("type", petTypesList, {
                    showPetType(it)
                    Toast.makeText(this, it.id, Toast.LENGTH_SHORT).show()
                }, mutableListOf(),{})
            }
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            startActivity<PetManagementActivity>()
        }
    }

    override fun petTypeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showPetSize(petSize: PetSize){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        val id = petSize.id.toString()
        name.setText(petSize.name)
        createdAt.text = petSize.created_at
        updatedAt.text = petSize.updated_at
        deletedAt.text = petSize.deleted_at

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Pet Info")
            .show()

        btnSave.setOnClickListener {
            isAll = false
            val newName = name.text.toString()
            val newPetSize = PetSize(id, newName)
            presentersize.editPetSize(id, newPetSize)
        }

        btnDelete.setOnClickListener {
            isAll = false
            presentersize.deletePetSize(id)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun showPetType(petType: PetType){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val createdAt = dialog.findViewById<TextView>(R.id.created_at)
        val updatedAt = dialog.findViewById<TextView>(R.id.updated_at)
        val deletedAt = dialog.findViewById<TextView>(R.id.deleted_at)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close)

        val id = petType.id.toString()
        name.setText(petType.name)
        createdAt.text = petType.created_at
        updatedAt.text = petType.updated_at
        deletedAt.text = petType.deleted_at

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Pet Info")
            .show()

        btnSave.setOnClickListener {
            isAll = false
            val newName = name.text.toString()
            val newPetType = PetType(id, newName)
            presentertype.editPetType(id, newPetType)
        }

        btnDelete.setOnClickListener {
            isAll = false
            presentertype.deletePetType(id)
        }

        btnClose.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){
        fab_add.setOnClickListener {
            val fragment: Fragment = AddPetFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<OwnerActivity>()
    }


}
