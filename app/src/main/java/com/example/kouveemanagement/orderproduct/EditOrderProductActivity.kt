package com.example.kouveemanagement.orderproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kouveemanagement.R
import com.example.kouveemanagement.model.DetailOrderProduct
import com.example.kouveemanagement.presenter.DetailOrderProductPresenter
import com.example.kouveemanagement.presenter.OrderProductPresenter

class EditOrderProductActivity : AppCompatActivity() {

    private lateinit var presenter: OrderProductPresenter

    private var detailOrderProducts: MutableList<DetailOrderProduct> = mutableListOf()
    private lateinit var presenterD: DetailOrderProductPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order_product)
    }
}
