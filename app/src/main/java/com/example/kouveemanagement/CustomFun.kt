package com.example.kouveemanagement

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kouveemanagement.presenter.LoginPresenter
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import java.text.NumberFormat
import java.util.*

object CustomFun {

    //SNACK BAR
    fun successSnackBar(viewInput: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(viewInput,textInput, Snackbar.LENGTH_SHORT)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.colorGrey)
        )
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_success)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        setTextView(textView)
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
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        setTextView(textView)
        snackBar.show()
    }

    fun warningSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_SHORT)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.black)
        )
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_warning)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.black))
        setTextView(textView)
        snackBar.show()
    }

    fun warningLongSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.black)
        )
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_warning)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.black))
        setTextView(textView)
        snackBar.show()
    }

    fun neutralSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_SHORT)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.black)
        )
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_neutral)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        setTextView(textView)
        snackBar.show()
    }

    fun welcomeSnackBar(input: View, baseContext: Context, textInput: String){
        val snackBar = Snackbar.make(input, textInput, Snackbar.LENGTH_SHORT)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, android.R.color.white)
        )
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_welcome)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        setTextView(textView)
        snackBar.show()
    }

    fun failedLoginSnackBar(input: View, baseContext: Context, id: String, pass: String, presenter: LoginPresenter, msg: String){
        val snackBar = Snackbar.make(input, msg, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(baseContext, R.color.colorGrey)
        )
        snackBar.setAction("TRY AGAIN"){
            presenter.loginPost(id, pass)
        }
        val view = snackBar.view
        view.setBackgroundResource(R.drawable.snack_bar_failed)
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(baseContext, android.R.color.white))
        setTextView(textView)
        snackBar.show()
    }

    //TEXT FOR SNACK BAR
    private fun setTextView(text: TextView){
        text.maxLines = 1
        text.setTypeface(null, Typeface.ITALIC)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    }

    //SWIPE LOADING
    fun setSwipe(swipe: SwipeRefreshLayout){
        swipe.setColorSchemeColors(Color.WHITE)
        swipe.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
    }

    //NETWORK CHECK
    fun verifiedNetwork(activity: AppCompatActivity): Boolean{
        val connectManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectManager.getNetworkCapabilities(network)
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }

    //CONVERTER
    fun changeToRp(number: Double): String{
        val id =  Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(id)
        return formatter.format(number).toString()+",-"
    }

    //TOOL TIPS
    fun createToolTips(context: Context, type: String): Balloon {
        return createBalloon(context) {
            setArrowSize(5)
            setWidthRatio(0.5f)
            setHeight(40)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setTextTypeface(Typeface.BOLD)
            setTextColorResource(android.R.color.white)
            setBackgroundColorResource(R.color.colorGreyDark)
            setBalloonAnimation(BalloonAnimation.CIRCULAR)
            setAutoDismissDuration(1000L)
            when (type) {
                "L" -> {
                    setArrowPosition(0.25f)
                    setText("Show all data")
                }
                "C" -> {
                    setArrowPosition(0.5f)
                    setText("Show enable data")
                }
                "R" -> {
                    setArrowPosition(0.5f)
                    setText("Show deleted data")
                }
                "M" -> {
                    setArrowPosition(0.5f)
                    setText("Show minimum product")
                }
                "P" -> {
                    setArrowPosition(0.25f)
                    setText("Pending")
                }
                "O" -> {
                    setArrowPosition(0.5f)
                    setText("On Delivery")
                }
                "A" -> {
                    setArrowPosition(0.5f)
                    setText("Arrived")
                }
            }
            setLifecycleOwner(lifecycleOwner)
        }
    }

}