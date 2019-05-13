package edu.uw.ischool.elisat15.quizdrone

import android.content.Context
import android.util.Log
import java.io.IOException

// Quiz or 'Question' object storing the question, answer choices, and the correct answer
data class Quiz (val question: String, val choices: ArrayList<String>, val answer: Int) {}

// Topic object storing the topic title, description, and the questions associated with topic
data class Topic (val title: String, val description: String, val questions: ArrayList<Quiz>) {}

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

    fun fetchJSON(dataSource: String, context: Context): String? {

        val jsonString: String? = try {
            // grab file from assets folder & read it to a String
            val inputStream = context.assets.open(dataSource)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            null
        }

        return jsonString
    }

}