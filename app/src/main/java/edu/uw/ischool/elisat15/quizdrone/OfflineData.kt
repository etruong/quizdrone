package edu.uw.ischool.elisat15.quizdrone

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OfflineData() : TopicRepository {

    // mathTopic
    private var mathTopic = Topic(
        "Math",
        "The math quiz contains 5 easy multiplication and addition math problems.",
        arrayListOf<Quiz>(
            Quiz("What is 5 + 5?", arrayListOf("10", "20", "13", "5"), 0),
            Quiz("What is 3 * 10?", arrayListOf("0", "10", "3", "30"), 3),
            Quiz("What is 10 + 1 * 9?", arrayListOf("10", "99", "19", "20"), 2),
            Quiz("What is -20 * -1 * 30?", arrayListOf("50", "333", "500", "600"), 3),
            Quiz("What is 5 + (20 * -1) * 8?", arrayListOf("111", "555", "155", "0"), 2)
        )
    )

    // Physics Quiz Data
    private var physicTopic = Topic(
        "Physics", "The physics quiz contains 5 easy questions about physic topics",
        arrayListOf<Quiz>(
            Quiz("What is the force of gravity?", arrayListOf("10 m/s^2", "15 m/s^2", "80 m/s^2", "9.8 m/s^2"), 3),
            Quiz("Sound travels at the fastest speed in...", arrayListOf("Steel", "Water", "Air", "Vaccum"), 1),
            Quiz(
                "What is inertia?",
                arrayListOf(
                    "Something that has to do with how wind moves",
                    "Not sure",
                    "The tendency for an object to remain in a uniform state",
                    "In ert ia..."
                ), 2
            ),
            Quiz(
                "What is torque?",
                arrayListOf(
                    "The twisting force that leads to rotation",
                    "Does it have something to do with twerking?",
                    "Maybe about a turtle",
                    "Not sure"
                ), 1
            )
        )
    )

    private var marvelTopic = Topic(
        "Marvel Super Heroes", "The Marvel quiz contains 5 easy questions about Marvel characters.",
        arrayListOf(
            Quiz(
                "Which of these Marvel characters carry a hammer?",
                arrayListOf("Thor", "Loki", "Bob the Builder", "Spiderman"), 0
            ),
            Quiz(
                "Which of these Marvel characters can shoot webs out of his hands?",
                arrayListOf("Doctor Strange", "Iron man", "Bruce Wayne", "Spiderman"), 3
            ),
            Quiz("Is Thanos the good guy?", arrayListOf("NO!", "I guess", "Yes", "Obviously"), 0),
            Quiz(
                "Which of the following characters below is not a Marvel character?",
                arrayListOf("Black Widow", "Captain America", "Jake", "Buckey"), 2
            )
        )
    )

    override var topics = arrayListOf<Topic>(mathTopic, physicTopic, marvelTopic)
    override var dataSource: String = "local"

    companion object {
        private const val USER_PREF_KEY: String = "USER_PREFERENCES_KEY"
        private const val DATA_SOURCE_KEY: String = "dataSource"
    }

    override fun fetchData(context: Context): ArrayList<Topic> {
        val sharedPreferences = context.getSharedPreferences(USER_PREF_KEY, Context.MODE_PRIVATE)
        dataSource = sharedPreferences.getString(DATA_SOURCE_KEY, "local")!!
        Log.d("offlineData", dataSource)
        if (dataSource == "local" || dataSource == "") {
            topics = arrayListOf<Topic>(mathTopic, physicTopic, marvelTopic)
        } else {
            val jsonString = fetchJSON(dataSource, context)
            jsonString?.let {

                val topicList = arrayListOf<Topic>()

                // Get JSON array
                val topicJSONArray = JSONArray(jsonString)

                // Read JSON array
                for (topic in 0 until topicJSONArray.length()) {
                    // get data of array value at index
                    val topicJSONObject = topicJSONArray.get(topic) as JSONObject

                    val title = topicJSONObject.getString("title")
                    val description = topicJSONObject.getString("desc")

                    val questionsList = arrayListOf<Quiz>()
                    val questionsJSONArray = topicJSONObject.getJSONArray("questions")

                    for (questionNum in 0 until questionsJSONArray.length()) {
                        val question = questionsJSONArray.get(questionNum) as JSONObject
                        val questionText = question.get("text") as String
                        val answer = (question.get("answer") as String).toInt()
                        val choicesJSONArray = question.get("answers") as JSONArray
                        val choicesList = arrayListOf<String>()

                        for (choicesNum in 0 until choicesJSONArray.length()) {
                            choicesList.add(choicesJSONArray.getString(choicesNum).toString())
                        }
                        val choicesArray = arrayOf<String>()
                        choicesList.toArray<String>(choicesArray)

                        val quizObject = Quiz(questionText, choicesList, answer)

                        questionsList.add(quizObject)
                    }
                    val questionsArray = arrayOf<Quiz>()
                    questionsList.toArray<Quiz>(questionsArray)
                    Log.d("questionArray", title + " " + questionsArray.size + " list size = " + questionsList.size)
                    topicList.add(Topic(title, description, questionsList))
                }

                topics = topicList
            }

        }
        Log.d("json", topics[0].title)
        return topics
    }

}