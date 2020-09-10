package com.marasigan.tableregistration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reserve.*

class ReserveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        reserve_button.setOnClickListener {
            val customerName = customerName_editText.text.toString().trim()
            if (!customerName.isEmpty()){
                val data = Intent().apply {
                    putExtra("customerName", customerName)
                }
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

    }
}