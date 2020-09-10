package com.marasigan.tableregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.marasigan.tableregistration.R
import kotlinx.android.synthetic.main.activity_customer_list.*

class CustomerListActivity : AppCompatActivity() {

    private lateinit var customerList: ArrayList<String>

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        customerList = intent.getStringArrayListExtra("customerList") as ArrayList<String>

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(customerList)
        recyclerView.adapter = adapter

    }
}