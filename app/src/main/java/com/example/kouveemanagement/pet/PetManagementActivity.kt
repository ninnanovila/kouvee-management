package com.example.kouveemanagement.pet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
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
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_pet_management.*
import org.jetbrains.anko.startActivity

class PetManagementActivity : AppCompatActivity(), PetSizeView, PetTypeView {

    private var petsizes: MutableList<PetSize> = mutableListOf()
    private lateinit var presentersize: PetSizePresenter

    private var pettypes: MutableList<PetType> = mutableListOf()
    private lateinit var presentertype: PetTypePresenter

    private lateinit var dialog: View
    private var isRotate = false

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
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.INVISIBLE
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
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
                    Toast.makeText(this, it.id, Toast.LENGTH_LONG).show()
                })
        }
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun petSizeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        val temp: List<PetType> = data?.pettype ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                pettypes.add(i, temp[i])
            }

            recyclerviewtype.layoutManager = LinearLayoutManager(this)
            recyclerviewtype.adapter = PetRecyclerViewAdapter("type", pettypes, {
                Toast.makeText(this, it.id, Toast.LENGTH_SHORT).show()
            }, mutableListOf(),{})

        }
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun petTypeFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
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
