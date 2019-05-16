package edu.uw.ischool.elisat15.quizdrone

import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.app.PendingIntent
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings


@Suppress("DEPRECATION")
class Preferences : AppCompatActivity() {

    companion object {
        const val USER_PREF_KEY = "USER_PREFERENCES_KEY"
        private const val DATA_SOURCE_KEY: String = "dataSource"
        private const val FETCH_TIME_KEY: String = "fetchTime"
        private const val FETCH_BOOLEAN: String = "fetching"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        setSavePreferenceButton()

        val sharedPreferences = getSharedPreferences(Companion.USER_PREF_KEY, Context.MODE_PRIVATE)
        val dataCurrentSource = sharedPreferences.getString(DATA_SOURCE_KEY, "")
        val currentDataTime = sharedPreferences.getInt(FETCH_TIME_KEY, 60000).div(60000)

        val dataURL = findViewById<EditText>(R.id.dataSourceInput)
        val grabDataTime = findViewById<EditText>(R.id.updateDataTime)

        dataURL.setText(dataCurrentSource)
        grabDataTime.setText("$currentDataTime")

    }

    fun checkConnectivity(grabDataTime: Int) {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            // fetches data when application loads
            val alarmIntent = Intent(this, MyAlarmManager::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.cancel(pendingIntent)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                grabDataTime.toLong(), pendingIntent)
        } else {

            if (Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0) {

                val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                AlertDialog.Builder(this)
                    .setTitle("Airplane Mode?")
                    .setMessage("You are currently have no access to the Internet and are in airplane mode. " +
                            "Would you like to turn airplane mode off?")
                    .setPositiveButton(android.R.string.yes) { dialog: DialogInterface, which: Int ->
                        startActivity(intent)
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("You currently have no access to the Internet! Unable to download data due to no connection! ")
                    .setPositiveButton(android.R.string.yes, null)
                    .setNegativeButton(android.R.string.no, null)
                    .show()

            }

        }
    }


    private fun setSavePreferenceButton() {
        val saveBtn = findViewById<Button>(R.id.saveSettingBtn)
        val sharedPreferences = getSharedPreferences(Companion.USER_PREF_KEY, Context.MODE_PRIVATE)
        val dataEditText = findViewById<EditText>(R.id.dataSourceInput)
        val dataTimeEditText = findViewById<EditText>(R.id.updateDataTime)



        saveBtn.setOnClickListener {
            val dataURL = dataEditText.text.toString()
            var grabDataTime = dataTimeEditText.text.toString()

            if (grabDataTime == "") {
                grabDataTime = "1"
            }


            checkConnectivity(grabDataTime.toInt().times(60000))

            sharedPreferences.edit().putString(DATA_SOURCE_KEY, dataURL).apply()
            sharedPreferences.edit().putInt(FETCH_TIME_KEY, grabDataTime.toInt().times(60000)).apply()
            sharedPreferences.edit().putBoolean(FETCH_BOOLEAN, true).apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_preferences -> {
            // User chose the "Settings" item, show the app settings UI...
            val intent = Intent(this, Preferences::class.java)
            startActivity(intent)
            true
        }
        R.id.action_home -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
