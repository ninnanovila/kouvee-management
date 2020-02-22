package com.example.kouveemanagement.pet


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kouveemanagement.R
import com.example.kouveemanagement.adapter.PetRecyclerViewAdapter
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetSizeResponse
import com.example.kouveemanagement.presenter.PetSizePresenter
import com.example.kouveemanagement.presenter.PetSizeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_pet_size.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

/**
 * A simple [Fragment] subclass.
 */
class AllPetSizeFragment : Fragment(), PetSizeView {

    private lateinit var petSize: PetSize
    private var petSizes: MutableList<PetSize> = mutableListOf()
    private lateinit var presenter: PetSizePresenter

    companion object {
        fun newInstance() = AllPetSizeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_pet_size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PetSizePresenter(this, Repository())
        presenter.getAllPetSize()

        btn_add.setOnClickListener {
            getData()
            presenter.addPetSize(petSize)
        }
    }

    fun getData(){
        val name = name.text.toString()
        petSize = PetSize(null, name)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun petSizeSuccess(data: PetSizeResponse?) {

        val temp: List<PetSize> = data?.petsize ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                petSizes.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                PetRecyclerViewAdapter("size", emptyList(), {}, petSizes){
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
                }
            }

            recyclerview.adapter?.notifyDataSetChanged()
        }

        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun petSizeFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(petSize: PetSize){

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val btn_save = dialog.findViewById<Button>(R.id.btn_save)
        val btn_delete = dialog.findViewById<Button>(R.id.btn_delete)

        val id = petSize.id.toString()
        name.setText(petSize.name)

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Pet Size Info")
            .show()

        btn_save.setOnClickListener {
            val newName = name.text.toString()
            val newPetSize = PetSize(id, newName)
            presenter.editPetSize(id, newPetSize)
        }

        btn_delete.setOnClickListener {
            presenter.deletePetSize(id)
        }
    }


}
