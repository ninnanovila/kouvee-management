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
import com.example.kouveemanagement.Animation
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
import org.jetbrains.anko.support.v4.startActivity

class PetManagementActivity : AppCompatActivity(), PetSizeView, PetTypeView {

    private var petsizes: MutableList<PetSize> = mutableListOf()
    private lateinit var presentersize: PetSizePresenter

    private var pettypes: MutableList<PetType> = mutableListOf()
    private lateinit var presentertype: PetTypePresenter

    private lateinit var dialog: View
    private lateinit var infoDialog: AlertDialog
    private var isRotate = false
    private var isAll = true

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

        fabAnimation()
    }

    override fun showLoading() {
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        dialog.btn_save.visibility = View.INVISIBLE
        dialog.btn_delete.visibility = View.INVISIBLE
        dialog.progressbar.visibility = View.VISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        dialog.btn_save.visibility = View.VISIBLE
        dialog.btn_delete.visibility = View.VISIBLE
        dialog.progressbar.visibility = View.INVISIBLE
        progressbar.visibility = View.INVISIBLE
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {

        if (isAll){
            val temp: List<PetSize> = data?.petsize ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                for (i in temp.indices){
                    petsizes.add(i, temp[i])
                }
                recyclerviewsize.layoutManager = LinearLayoutManager(this)
                recyclerviewsize.adapter =
                    PetRecyclerViewAdapter("size", mutableListOf(), {}, petsizes, {
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

    override fun petTypeSuccess(data: PetTypeResponse?) {
        if (isAll){
            val temp: List<PetType> = data?.pettype ?: emptyList()
            if (temp.isEmpty()){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
            }else{
                for (i in temp.indices){
                    pettypes.add(i, temp[i])
                }
                recyclerviewtype.layoutManager = LinearLayoutManager(this)
                recyclerviewtype.adapter = PetRecyclerViewAdapter("type", pettypes, {
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
        val created_at = dialog.findViewById<TextView>(R.id.created_at)
        val updated_at = dialog.findViewById<TextView>(R.id.updated_at)
        val deleted_at = dialog.findViewById<TextView>(R.id.deleted_at)
        val btn_save = dialog.findViewById<Button>(R.id.btn_save)
        val btn_delete = dialog.findViewById<Button>(R.id.btn_delete)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)

        val id = petSize.id.toString()
        name.setText(petSize.name)
        created_at.text = petSize.created_at
        updated_at.text = petSize.updated_at
        deleted_at.text = petSize.deleted_at

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Pet Info")
            .show()

        btn_save.setOnClickListener {
            isAll = false
            val newName = name.text.toString()
            val newPetSize = PetSize(id, newName)
            presentersize.editPetSize(id, newPetSize)
        }

        btn_delete.setOnClickListener {
            isAll = false
            presentersize.deletePetSize(id)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun showPetType(petType: PetType){
        dialog = LayoutInflater.from(this).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val created_at = dialog.findViewById<TextView>(R.id.created_at)
        val updated_at = dialog.findViewById<TextView>(R.id.updated_at)
        val deleted_at = dialog.findViewById<TextView>(R.id.deleted_at)
        val btn_save = dialog.findViewById<Button>(R.id.btn_save)
        val btn_delete = dialog.findViewById<Button>(R.id.btn_delete)
        val btn_close = dialog.findViewById<ImageButton>(R.id.btn_close)

        val id = petType.id.toString()
        name.setText(petType.name)
        created_at.text = petType.created_at
        updated_at.text = petType.updated_at
        deleted_at.text = petType.deleted_at

        infoDialog = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Pet Info")
            .show()

        btn_save.setOnClickListener {
            isAll = false
            val newName = name.text.toString()
            val newPetType = PetType(id, newName)
            presentertype.editPetType(id, newPetType)
        }

        btn_delete.setOnClickListener {
            isAll = false
            presentertype.deletePetType(id)
        }

        btn_close.setOnClickListener {
            infoDialog.dismiss()
        }
    }

    private fun fabAnimation(){

        Animation.init(fab_add)
        Animation.init(fab_search)

        fab_menu.setOnClickListener {
            isRotate = Animation.rotateFab(it, !isRotate)
            if (isRotate){
                Animation.showIn(fab_add)
                Animation.showIn(fab_search)
            }else{
                Animation.showOut(fab_add)
                Animation.showOut(fab_search)
            }
        }

        fab_add.setOnClickListener {
            isRotate = Animation.rotateFab(fab_menu, !isRotate)
            Animation.showOut(fab_add)
            Animation.showOut(fab_search)
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
