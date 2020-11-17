package com.example.it_arch_aidl

import android.app.Service
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import android.util.Log

import com.example.it_arch_aidl.StringCalc

class RemoteService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
    private val binder : IMyAidlInterface.Stub = object : IMyAidlInterface.Stub(){
        fun getPid() : Int = Process.myPid()

        override fun basicTypes(aString: String?):  String{
            val calcFunc = StringCalc()
            var recMes = "result："
            val calcRes = aString?.let { calcFunc.calculate(it) }
            recMes += "$calcRes"
            Log.d("tag11", recMes)
            return recMes
        }
    }
}

var iRemoteService: IMyAidlInterface? = null

val mConnection = object : ServiceConnection {

    // Called when the connection with the service is established
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        // Following the example above for an AIDL interface,
        // this gets an instance of the IRemoteInterface, which we can use to call on the service
        iRemoteService = IMyAidlInterface.Stub.asInterface(service)
    }

    // Called when the connection with the service disconnects unexpectedly
    override fun onServiceDisconnected(className: ComponentName) {
        Log.e(TAG, "Service has unexpectedly disconnected")
        iRemoteService = null
    }
}
