package edu.uw.ischool.elisat15.quizdrone

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log

const val TAG: String = "QuizApp"

// Quiz or 'Question' object storing the question, answer choices, and the correct answer
data class Quiz (val question: String, val choices: Array<String>, val answer: Int) {}

// Topic object storing the topic title, description, and the questions associated with topic
data class Topic (val title: String, val description: String, val questions: Array<Quiz>) {}

// Math Quiz Data
val mathQuestionObjects = arrayOf<Quiz>(
    Quiz("What is 5 + 5?", arrayOf("10", "20", "13", "5"), 0),
    Quiz("What is 3 * 10?", arrayOf("0", "10", "3", "30"), 3),
    Quiz("What is 10 + 1 * 9?", arrayOf("10", "99", "19", "20"), 2),
    Quiz("What is -20 * -1 * 30?", arrayOf("50", "333", "500", "600"), 3),
    Quiz("What is 5 + (20 * -1) * 8?", arrayOf("111", "555", "155", "0"), 2)
)

val mathTopic = Topic(
    "Math",
    "The math quiz contains 5 easy multiplication and addition math problems.",
    mathQuestionObjects
)

// Physics Quiz Data
val physicQuestionObjects = arrayOf<Quiz>(
    Quiz("What is the force of gravity?", arrayOf("10 m/s^2", "15 m/s^2", "80 m/s^2", "9.8 m/s^2"), 3),
    Quiz("Sound travels at the fastest speed in...", arrayOf("Steel", "Water", "Air", "Vaccum"), 1),
    Quiz(
        "What is inertia?",
        arrayOf(
            "Something that has to do with how wind moves",
            "Not sure",
            "The tendency for an object to remain in a uniform state",
            "In ert ia..."
        ), 2
    ),
    Quiz(
        "What is torque?",
        arrayOf(
            "The twisting force that leads to rotation",
            "Does it have something to do with twerking?",
            "Maybe about a turtle",
            "Not sure"
        ), 1
    )
)

val physicTopic = Topic(
    "Physic", "The physics quiz contains 5 easy questions about physic topics",
    physicQuestionObjects
)

// Marvel Quiz Data
val marvelQuestionObject = arrayOf(
    Quiz(
        "Which of these Marvel characters carry a hammer?",
        arrayOf("Thor", "Loki", "Bob the Builder", "Spiderman"), 0
    ),
    Quiz(
        "Which of these Marvel characters can shoot webs out of his hands?",
        arrayOf("Doctor Strange", "Iron man", "Bruce Wayne", "Spiderman"), 3
    ),
    Quiz("Is Thanos the good guy?", arrayOf("NO!", "I guess", "Yes", "Obviously"), 0),
    Quiz(
        "Which of the following characters below is not a Marvel character?",
        arrayOf("Black Widow", "Captain America", "Jake", "Buckey"), 2
    )
)

val marvelTopic = Topic(
    "Marvel SuperHeroes", "The Marvel quiz contains 5 easy questions about Marvel characters.",
    marvelQuestionObject
)

interface TopicRepository {

    var marvelQuiz: Topic
    var mathQuiz: Topic
    var physicsQuiz: Topic

    fun initData() {
        marvelQuiz = marvelTopic
        mathQuiz = mathTopic
        physicsQuiz = physicTopic
    }

}

class QuizApp() : Application(), TopicRepository {

    override lateinit var marvelQuiz: Topic
    override lateinit var mathQuiz: Topic
    override lateinit var physicsQuiz: Topic

    fun getSelectedQuiz(chosenQuiz: String): Topic {
        var selectedQuiz = marvelQuiz
        when(chosenQuiz) {
            "Math" -> selectedQuiz = mathQuiz
            "Physics" -> selectedQuiz = physicsQuiz
        }
        return selectedQuiz
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "An QuizApp was created!")
    }

}
