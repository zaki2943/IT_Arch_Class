package com.example.it_arch_aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.it_arch_aidl.IMyAidlInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var myService: IMyAidlInterface? = null
    private val connection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = IMyAidlInterface.Stub.asInterface(service)
            Toast.makeText(applicationContext, "Sevice Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myService = null
            Toast.makeText(applicationContext, "Service Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
    }

    override fun onStart() {
        super.onStart()
        if(myService == null){
            val it = Intent("myservice")
            it.setPackage("com.example.it_arch_aidl")
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    fun push_buttun(view: View){
        val calc_text = findViewById<EditText>(R.id.editCalctext)
        val message = calc_text.text.toString()
        val result_str = Calc_str(message)
        textView.apply { text = result_str.toString() }

    }


    private fun Calc_str(str : String) : String? {
        var result_str = "can't calc"
        try {
            val get_str = myService?.basicTypes(str)
            if (get_str != null) {
                Log.d("tag11", get_str)
                result_str = get_str
            }
        } catch (e: RemoteException){
            e.printStackTrace()
            Log.d("tag12", e.toString())
        }
        //val calcFunc = StringCalc()
        //val calcRes = calcFunc.calculate("3 * 4")
        //result_str = "result:$calcRes"
        return  result_str
    }
}