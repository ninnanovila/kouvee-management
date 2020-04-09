package com.example.kouveemanagement.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomView
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
import kotlinx.android.synthetic.main.fragment_add_pet.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class AddPetFragment : Fragment(), PetSizeView, PetTypeView {

    private lateinit var petSize: PetSize
    private lateinit var presenterSize: PetSizePresenter

    private lateinit var petType: PetType
    private lateinit var presenterType: PetTypePresenter

    private lateinit var type: String

    companion object{
        private const val type = "input"
        fun newInstance(input: String) : AddPetFragment{
            val fragment = AddPetFragment()
            val bundle = Bundle().apply {
                putString(type, input)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        type = arguments?.getString("input")!!
        return inflater.inflate(R.layout.fragment_add_pet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (type == "type"){
            btn_add_s.visibility = View.GONE
        }else{
            btn_add_t.visibility = View.GONE
        }
        btn_add_s.setOnClickListener {
            if (isValid()){
                getPetSize()
                presenterSize = PetSizePresenter(this, Repository())
                presenterSize.addPetSize(petSize)
            }
        }
        btn_add_t.setOnClickListener {
            if (isValid()){
                getPetType()
                presenterType = PetTypePresenter(this, Repository())
                presenterType.addPetType(petType)
            }
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

    private fun isValid(): Boolean{
        if (name.text.isNullOrEmpty()){
            name.error = getString(R.string.error_name)
            return false
        }
        return true
    }

    override fun showPetTypeLoading() {
        btn_add_t.startAnimation()
    }

    override fun hidePetTypeLoading() {
    }

    override fun petTypeSuccess(data: PetTypeResponse?) {
        startActivity<PetTypeManagementActivity>()
    }

    override fun petTypeFailed() {
        btn_add_t.revertAnimation()
        context?.let { view?.let { itView -> CustomView.failedSnackBar(itView, it, "Please try again") } }
    }

    override fun showPetSizeLoading() {
        btn_add_s.startAnimation()
    }

    override fun hidePetSizeLoading() {
    }

    override fun petSizeSuccess(data: PetSizeResponse?) {
        startActivity<PetSizeManagementActivity>()
    }

    override fun petSizeFailed() {
        btn_add_s.revertAnimation()
        context?.let { view?.let { itView -> CustomView.failedSnackBar(itView, it, "Please try again") } }
    }

}
