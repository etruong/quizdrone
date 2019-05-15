package edu.uw.ischool.elisat15.quizdrone

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
        val currentDataTime = sharedPreferences.getInt(FETCH_TIME_KEY, 5)

        val dataURL = findViewById<EditText>(R.id.dataSourceInput)
        var grabDataTime = findViewById<EditText>(R.id.updateDataTime)

        dataURL.setText(dataCurrentSource)
        grabDataTime.setText("" + currentDataTime)



    }

    private fun setSavePreferenceButton() {
        val saveBtn = findViewById<Button>(R.id.saveSettingBtn)
        saveBtn.setOnClickListener {
            val dataURL = findViewById<EditText>(R.id.dataSourceInput).text.toString()
            var grabDataTime = findViewById<EditText>(R.id.updateDataTime).text.toString()

            if (grabDataTime == "")
                grabDataTime = "1"

            val sharedPreferences = getSharedPreferences(Companion.USER_PREF_KEY, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(DATA_SOURCE_KEY, dataURL).commit()
            sharedPreferences.edit().putInt(FETCH_TIME_KEY, grabDataTime.toInt()).commit()
            sharedPreferences.edit().putBoolean(FETCH_BOOLEAN, true).commit()
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
