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
import com.example.kouveemanagement.model.PetType
import com.example.kouveemanagement.model.PetTypeResponse
import com.example.kouveemanagement.presenter.PetTypePresenter
import com.example.kouveemanagement.presenter.PetTypeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_pet_type.*

/**
 * A simple [Fragment] subclass.
 */
class AllPetTypeFragment : Fragment(), PetTypeView {

    private lateinit var petType: PetType
    private var petTypes: MutableList<PetType> = mutableListOf()
    private lateinit var presenter: PetTypePresenter

    companion object{
        fun newInstance() = AllPetTypeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_pet_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PetTypePresenter(this, Repository())
        presenter.getAllPetType()

        btn_add.setOnClickListener {
            getData()
            presenter.addPetType(petType)
        }
    }

    fun getData(){
        val name = name.text.toString()
        petType = PetType(null, name)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {


        val temp: List<PetType> = data?.pettype ?: emptyList()

        if (temp.isEmpty()){
            Toast.makeText(context, "No Result", Toast.LENGTH_SHORT).show()
        }else{

            for (i in temp.indices){
                petTypes.add(i, temp[i])
            }

            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = context?.let {
                PetRecyclerViewAdapter("type", petTypes, {
                    showDialog(it)
                    Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
                }, emptyList(),{})
            }

        }

        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun petTypeFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(petType: PetType){

        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_detail_pet, null)

        val name = dialog.findViewById<EditText>(R.id.name)
        val btn_save = dialog.findViewById<Button>(R.id.btn_save)
        val btn_delete = dialog.findViewById<Button>(R.id.btn_delete)

        name.setText(petType.name)

        val newName = name.text.toString()

        val petType = PetType(petType.id.toString(), newName)

        AlertDialog.Builder(context)
            .setView(dialog)
            .setTitle("Pet Type Info")
            .show()

        btn_save.setOnClickListener {
            presenter.editPetType(petType.id.toString(), petType)
        }

        btn_delete.setOnClickListener {
            presenter.deletePetType(petType.id.toString())
        }
    }

}
