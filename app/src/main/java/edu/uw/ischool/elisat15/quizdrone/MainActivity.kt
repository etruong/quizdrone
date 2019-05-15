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
import java.io.IOException
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

const val SELECTED_CATEGORY: String = "currentCategory"

class MainActivity : AppCompatActivity() {

    companion object {
        const val USER_PREF_KEY = "USER_PREFERENCES_KEY"
        private const val FETCH_TIME_KEY: String = "fetchTime"
        private const val FETCH_BOOLEAN: String = "fetching"
    }

    private fun fetchData() {
        val timer = Timer()
        val context = this
        val quizApp = QuizApp()
        val sharedPreferences = context.getSharedPreferences(USER_PREF_KEY, Context.MODE_PRIVATE)
        val fetchTime = sharedPreferences.getInt(FETCH_TIME_KEY, 5).times(60000).toLong()
        timer.scheduleAtFixedRate(0, fetchTime) {
            val fetching = sharedPreferences.getBoolean(FETCH_BOOLEAN, false)
            if (fetching) {
                sharedPreferences.edit().putBoolean(FETCH_BOOLEAN, false).commit()
            } else {
                quizApp.topicRepository.fetchData(context)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val quizApp = QuizApp()
        fetchData()
        val topics = quizApp.topicRepository.fetchData(this)
        Log.d("hi size", "${topics.size}")
        findViewById<Button>(R.id.topic1Btn).text = topics[0].title
        findViewById<Button>(R.id.topic2Btn).text = topics[1].title
        findViewById<Button>(R.id.topic3Btn).text = topics[2].title

        // Sets on click listeners for
        setListener(findViewById(R.id.topic1Btn))
        setListener(findViewById(R.id.topic2Btn))
        setListener(findViewById(R.id.topic3Btn))
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
