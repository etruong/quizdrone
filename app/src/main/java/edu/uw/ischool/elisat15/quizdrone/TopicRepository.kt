package edu.uw.ischool.elisat15.quizdrone

import android.Manifest
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.IOException
import android.Manifest.permission
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.pm.PackageManager
import android.widget.Toast
import java.io.FileNotFoundException
import java.lang.Exception


// Quiz or 'Question' object storing the question, answer choices, and the correct answer
data class Quiz (val question: String, val choices: ArrayList<String>, val answer: Int)

// Topic object storing the topic title, description, and the questions associated with topic
data class Topic (val title: String, val description: String, val questions: ArrayList<Quiz>)

interface TopicRepository {

    var topics: ArrayList<Topic>
    var dataSource: String

    fun getSelectedQuiz(selectedQuiz: String): Topic {
        for (i in 0 until topics.size) {
            if (topics[i].title == selectedQuiz)
                return topics[i]
        }
        throw error("Chosen Quiz passed in is set not in topics database")
    }

    fun getAllTopics(): ArrayList<Topic> {
        return topics
    }

    fun fetchData(context: Context): ArrayList<Topic>

    fun downloadJSON(context: Context) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val sharedPreference = context.getSharedPreferences("USER_PREFERENCES_KEY",
            Context.MODE_PRIVATE)
        val url = sharedPreference.getString("dataSource", "")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(

            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val fileName = "questions.json"
                val file = File(context.filesDir, fileName)
                file.appendText(response)

                try {
                    val fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE)
                    fileOutputStream.write(response.toByteArray())
                    fileOutputStream.close()
                    Log.v("downloadData", "Successfully downloaded!")
                } catch(e: FileNotFoundException) {
                    Log.v("downloadData", e.message)
                } catch (e: IOException) {
                    Log.v("downloadData", e.message)
                }

            },
            Response.ErrorListener { Log.v("requesting", "That didn't work!") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun fetchJSON(dataSource: String, context: Context): String? {

        downloadJSON(context)
        val fileLocation = context.getFilesDir().toString() + "/" + "questions.json";
        var jsonString = File(fileLocation).readText()

//        Grab file locally
//
//        var jsonString: String? = try {
//            // grab file from assets folder & read it to a String
//            val inputStream = context.assets.open(dataSource)
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//
//            String(buffer, Charsets.UTF_8)
//        } catch (e: IOException) {
//            null
//        }

        return jsonString
    }

}