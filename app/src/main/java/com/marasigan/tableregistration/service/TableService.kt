package com.marasigan.tableregistration.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.IBinder
import com.marasigan.tableregistration.MainActivity
import java.util.ArrayList

class TableService : Service() {
    companion object {
        val TAG: String = TableService::class.java.simpleName
    }

    private var context: Context? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        context = this
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val customerList: ArrayList<String> = intent.getStringArrayListExtra("customerList")
        val broadcastIntent = Intent()

        broadcastIntent.action = MainActivity.BROADCAST_ACTION
        broadcastIntent.putStringArrayListExtra("customerList", intent.getStringArrayListExtra("customerList"))
        sendBroadcast(broadcastIntent)

        return START_NOT_STICKY
    }



}