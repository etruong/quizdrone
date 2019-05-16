package edu.uw.ischool.elisat15.quizdrone

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import java.io.IOException
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

const val SELECTED_CATEGORY: String = "currentCategory"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    companion object {
        const val USER_PREF_KEY = "USER_PREFERENCES_KEY"
        private const val FETCH_TIME_KEY: String = "fetchTime"
        private const val FETCH_BOOLEAN: String = "fetching"
    }

    val quizApp = QuizApp.instance

    fun startIntervalDownloadData() {
        val grabDataTime = getSharedPreferences(Preferences.USER_PREF_KEY,
            Context.MODE_PRIVATE).getInt(FETCH_TIME_KEY, 60000)
        val alarmIntent = Intent(this, MyAlarmManager::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
            grabDataTime.toLong(), pendingIntent)
    }

    fun checkConnectivity() {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            // fetches data when application loads
            startIntervalDownloadData()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        checkConnectivity()
        quizApp.topicRepository.fetchData(this)
        setButtonTopics()

        // Sets on click listeners for
        setListener(findViewById(R.id.topic1Btn))
        setListener(findViewById(R.id.topic2Btn))
        setListener(findViewById(R.id.topic3Btn))
    }

    private fun setButtonTopics() {
        val topics = quizApp.topicRepository.topics
        findViewById<Button>(R.id.topic1Btn).text = topics[0].title
        findViewById<Button>(R.id.topic2Btn).text = topics[1].title
        findViewById<Button>(R.id.topic3Btn).text = topics[2].title
    }

    private fun setListener(btn: Button) {

        btn.setOnClickListener() {
            val selectedCategory = btn.text.toString()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(SELECTED_CATEGORY, selectedCategory)
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
