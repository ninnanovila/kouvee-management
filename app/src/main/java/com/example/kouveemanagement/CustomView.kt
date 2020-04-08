package com.example.kouveemanagement

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kouveemanagement.presenter.LoginPresenter
import com.google.android.material.snackbar.Snackbar

object CustomView {

    fun successSnackBar(viewInput: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(viewInput,textInput, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.colorGrey)
        )
        snackBar.setAction("Close"){
            snackBar.dismiss()
        }
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_success)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 1
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
    }

    fun failedSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.colorGrey)
        )
        snackBar.setAction("Close"){
            snackBar.dismiss()
        }
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_failed)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 1
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
    }

    fun neutralSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.black)
        )
        snackBar.setAction("Close"){
            snackBar.dismiss()
        }
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_neutral)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 1
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
    }

    fun failedLoginSnackBar(input: View, baseContext: Context, id: String, pass: String, presenter: LoginPresenter){
        val snackBar = Snackbar.make(input, "Check again", Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.colorGrey)
        )
        snackBar.setAction("TRY AGAIN"){
            presenter.loginPost(id, pass)
        }
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_failed)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 1
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
    }

    fun setSwipe(swipe: SwipeRefreshLayout){
        swipe.setColorSchemeColors(Color.WHITE)
        swipe.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
    }

}