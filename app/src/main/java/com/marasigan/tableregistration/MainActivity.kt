package com.marasigan.tableregistration

import android.app.AlertDialog
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.marasigan.tableregistration.service.TableService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.marasigan.tableregistration.database.CustomersDBHelper

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
        private const val RESERVE_REQUEST = 642
        private const val MAX_PAX = 5
        const val BROADCAST_ACTION = "BROADCAST_ACTION"
    }

    private lateinit var context: Context

    private lateinit var dbHelper: CustomersDBHelper

    private val intentFilter: IntentFilter = IntentFilter()
    private var tableListIntent: Intent = Intent()

    private var table1List: ArrayList<String> = arrayListOf()
    private var table2List: ArrayList<String> = arrayListOf()
    private var table3List: ArrayList<String> = arrayListOf()
    private var table4List: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        dbHelper = CustomersDBHelper(context)

        intentFilter.addAction(BROADCAST_ACTION)

        // show customer list
        table1_button.setOnClickListener {
            tableListIntent = Intent(this, TableService::class.java).apply {
                putStringArrayListExtra("customerList", table1List)
            }
            startService(tableListIntent)
//            val intent = Intent(this, CustomerListActivity::class.java).apply {
//                putStringArrayListExtra("customerList", table1List)
//            }
//            startActivity(intent)
        }
        table2_button.setOnClickListener {
            tableListIntent = Intent(this, TableService::class.java).apply {
                putStringArrayListExtra("customerList", table2List)
            }
            startService(tableListIntent)
        }
        table3_button.setOnClickListener {
            tableListIntent = Intent(this, TableService::class.java).apply {
                putStringArrayListExtra("customerList", table3List)
            }
            startService(tableListIntent)
        }
        table4_button.setOnClickListener {
            tableListIntent = Intent(this, TableService::class.java).apply {
                putStringArrayListExtra("customerList", table4List)
            }
            startService(tableListIntent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // inflate menu - add items to the action bar if present
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reserve -> {
                val reserveIntent = Intent(this, ReserveActivity::class.java)
                startActivityForResult(reserveIntent, RESERVE_REQUEST)
                true
            }
            R.id.action_clear -> {
                // clear all reservatios
                table1List.clear()
                table2List.clear()
                table3List.clear()
                table4List.clear()
                // delete from db
                dbHelper.deleteAll()
                Toast.makeText(applicationContext, "Deleted all reservations.", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESERVE_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                      val customerName = data.getStringExtra("customerName").toString()
                        // check for table availability
                        if (table1List.size < MAX_PAX) {
                            table1List.add(customerName)
                            // insert customer to database
                            dbHelper.insertCustomer(customerName, 1)
                            showAlertDialog(customerName, 1, table1List)
                        } else if (table2List.size < MAX_PAX) {
                            table2List.add(customerName)
                            dbHelper.insertCustomer(customerName, 2)
                            showAlertDialog(customerName, 2, table2List)
                        } else if (table3List.size < MAX_PAX) {
                            table3List.add(customerName)
                            dbHelper.insertCustomer(customerName, 3)
                            showAlertDialog(customerName, 3, table3List)
                        } else if (table4List.size < MAX_PAX) {
                            table4List.add(customerName)
                            dbHelper.insertCustomer(customerName, 4)
                            showAlertDialog(customerName, 4, table4List)
                        } else {
                            showAlertDialog(customerName, 0, arrayListOf())
                        }
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val mainReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "Broadcast Received")
            val customerList = intent.getStringArrayListExtra("customerList") as? ArrayList<String>
            if (customerList != null) {
                if (customerList.size != 0) {
                    val intent = Intent(context, CustomerListActivity::class.java).apply {
                        putStringArrayListExtra("customerList", customerList)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Table is empty.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mainReceiver, intentFilter)

        // load customers per table
        table1List = dbHelper.getCustomersByTable(1)
        table2List = dbHelper.getCustomersByTable(2)
        table3List = dbHelper.getCustomersByTable(3)
        table4List = dbHelper.getCustomersByTable(4)

    }

    override fun onPause() {
        unregisterReceiver(mainReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(tableListIntent)
    }

    private fun showAlertDialog(customerName: String, tableNum: Int, table: ArrayList<String>) {
        val dialogBuilder = AlertDialog.Builder(this)


        if (tableNum == 0 || table == null) {
            dialogBuilder.setMessage("Sorry all tables are full.")
                .setCancelable(true)
                .setPositiveButton("OK", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })

        } else {
            dialogBuilder.setMessage("Table #"+ tableNum + " reserved for " + customerName + ".\n" +
                    "Occupied Seats: "+ table.size)
                .setCancelable(true)
                .setPositiveButton("OK", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })
        }

        // create dialog box
        val alert = dialogBuilder.create()
        alert.show()

    }
}