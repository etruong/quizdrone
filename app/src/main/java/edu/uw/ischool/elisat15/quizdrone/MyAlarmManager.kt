package edu.uw.ischool.elisat15.quizdrone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

@Suppress("DEPRECATION")
class MyAlarmManager: BroadcastReceiver() {

    companion object {
        private const val DATA_SOURCE_KEY: String = "dataSource"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true


        val sharedPreferences = context?.getSharedPreferences(Preferences.USER_PREF_KEY,
            Context.MODE_PRIVATE)
        var dataCurrentSource = sharedPreferences?.getString(Companion.DATA_SOURCE_KEY, "")
        if (dataCurrentSource == "") {
            dataCurrentSource = "Default data currently being fetched"
        }
        Log.v("Alarm Manager", "onRecieve Alarm Manager")

        QuizApp.instance.topicRepository.fetchData(context!!)

        Toast.makeText(context, dataCurrentSource, Toast.LENGTH_SHORT).show()

    }

}