package com.example.kouveemanagement.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kouveemanagement.CustomFun
import com.example.kouveemanagement.MainActivity
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.LoginResponse
import com.example.kouveemanagement.presenter.LoginPresenter
import com.example.kouveemanagement.presenter.LoginView
import com.example.kouveemanagement.repository.Repository
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : Fragment(), LoginView {

    private var presenter = LoginPresenter(this, Repository())

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_close.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
        btn_save.setOnClickListener {
            if (isValid()){
                presenter.changePassword(MainActivity.loggedInUser.id.toString(), old_password.text.toString(), new_password.text.toString())
            }
        }
    }

    private fun isValid(): Boolean{
        return when {
            old_password.text.isNullOrEmpty() -> {
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please fill old password")
                false
            }
            new_password.text.isNullOrEmpty() -> {
                CustomFun.failedSnackBar(requireView(), requireContext(), "Please fill new password")
                false
            }
            old_password.text.toString() == new_password.text.toString() -> {
                CustomFun.warningSnackBar(requireView(), requireContext(), "Password should be different")
                false
            }
            else -> true
        }
    }

    override fun showLoginLoading() {
        btn_save.startAnimation()
    }

    override fun hideLoginLoading() {
    }

    override fun loginSuccess(data: LoginResponse?) {
        if (data != null) {
            MainActivity.loggedInUser = data.employee
        }
        showAlert()
    }

    override fun loginFailed(data: String) {
        btn_save.revertAnimation()
        CustomFun.failedSnackBar(requireView(), requireContext(), data)
    }

    private fun showAlert(){
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.update)
            .setTitle("Information")
            .setMessage("Password has been change")
            .setPositiveButton("OK"){_, _ ->
                startActivity<ProfileActivity>()
            }
            .show()
    }

}
