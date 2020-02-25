package com.example.kouveemanagement.pet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.PetSize
import com.example.kouveemanagement.model.PetSizeResponse
import com.example.kouveemanagement.model.PetType
import com.example.kouveemanagement.model.PetTypeResponse
import com.example.kouveemanagement.presenter.PetSizePresenter
import com.example.kouveemanagement.presenter.PetSizeView
import com.example.kouveemanagement.presenter.PetTypePresenter
import com.example.kouveemanagement.presenter.PetTypeView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_all_pet_size.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddPetFragment : Fragment(), PetSizeView, PetTypeView {

    private lateinit var petSize: PetSize
    private lateinit var presenterSize: PetSizePresenter

    private lateinit var petType: PetType
    private lateinit var presenterType: PetTypePresenter

    companion object {
        fun newInstance() = AddPetFragment()
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

        btn_add_s.setOnClickListener {
            getPetSize()
            presenterSize = PetSizePresenter(this, Repository())
            presenterSize.addPetSize(petSize)
        }

        btn_add_t.setOnClickListener {
            getPetType()
            presenterType = PetTypePresenter(this, Repository())
            presenterType.addPetType(petType)
        }

        btn_close.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

    }

    private fun getPetSize(){
        val name = name.text.toString()
        petSize = PetSize(null, name, null, null, null)
    }

    private fun getPetType(){
        val name = name.text.toString()
        petType = PetType(null, name, null, null, null)
    }

    override fun showLoading() {
        btn_add_s.visibility = View.INVISIBLE
        btn_add_t.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        btn_add_s.visibility = View.VISIBLE
        btn_add_t.visibility = View.VISIBLE
        progressbar.visibility = View.INVISIBLE
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<PetManagementActivity>()
    }

    override fun petTypeFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        startActivity<PetManagementActivity>()
    }

    override fun petSizeFailed() {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

}
