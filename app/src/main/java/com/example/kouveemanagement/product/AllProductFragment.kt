package com.example.kouveemanagement.product


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.kouveemanagement.R

/**
 * A simple [Fragment] subclass.
 */
class AllProductFragment : Fragment() {

    companion object {
        fun newInstance() = AllProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_product, container, false)
    }


}
