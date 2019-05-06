package edu.uw.ischool.elisat15.quizdrone

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log

const val TAG: String = "QuizApp"

// Math Quiz Data
val mathQuestionObjects = arrayOf<QuizApp.Question>(
    QuizApp.Question("What is 5 + 5?", arrayOf("10", "20", "13", "5"), 0),
    QuizApp.Question("What is 3 * 10?", arrayOf("0", "10", "3", "30"), 3),
    QuizApp.Question("What is 10 + 1 * 9?", arrayOf("10", "99", "19", "20"), 2),
    QuizApp.Question("What is -20 * -1 * 30?", arrayOf("50", "333", "500", "600"), 3),
    QuizApp.Question("What is 5 + (20 * -1) * 8?", arrayOf("111", "555", "155", "0"), 2)
)

val mathTopic = QuizApp.Topic(
    "Math",
    "The math quiz contains 5 easy multiplication and addition math problems.",
    mathQuestionObjects
)

// Physics Quiz Data
val physicQuestionObjects = arrayOf<QuizApp.Question>(
    QuizApp.Question("What is the force of gravity?", arrayOf("10 m/s", "15 m/s", "80 m/s", "9.8 m/s"), 3),
    QuizApp.Question("Sound travels at the fastest speed in...", arrayOf("Steel", "Water", "Air", "Vaccum"), 1),
    QuizApp.Question(
        "What is inertia?",
        arrayOf(
            "Something that has to do with how wind moves",
            "Not sure",
            "The tendency for an object to remain in a uniform state",
            "In ert ia..."
        ), 2
    ),
    QuizApp.Question(
        "What is torque?",
        arrayOf(
            "The twisting force that leads to rotation",
            "Does it have something to do with twerking?",
            "Maybe about a turtle",
            "Not sure"
        ), 1
    )
)

val physicTopic = QuizApp.Topic(
    "Physic", "The physics quiz contains 5 easy questions about physic topics",
    physicQuestionObjects
)

// Marvel Quiz Data
val marvelQuestionObject = arrayOf(
    QuizApp.Question(
        "Which of these Marvel characters carry a hammer?",
        arrayOf("Thor", "Loki", "Bob the Builder", "Spiderman"), 0
    ),
    QuizApp.Question(
        "Which of these Marvel characters can shoot webs out of his hands?",
        arrayOf("Doctor Strange", "Iron man", "Bruce Wayne", "Spiderman"), 3
    ),
    QuizApp.Question("Is Thanos the good guy?", arrayOf("NO!", "I guess", "Yes", "Obviously"), 0),
    QuizApp.Question(
        "Which of the following characters below is not a Marvel character?",
        arrayOf("Black Widow", "Captain America", "Jake", "Buckey"), 2
    )
)

val marvelTopic = QuizApp.Topic(
    "Marvel SuperHeroes", "The Marvel quiz contains 5 easy questions about Marvel characters.",
    marvelQuestionObject
)

interface TopicRepository {

    var marvelQuiz: QuizApp.Topic?
    var mathQuiz: QuizApp.Topic?
    var physicsQuiz: QuizApp.Topic?
    var userResponse: MutableList<Int>?

    fun initData() {
        marvelQuiz = marvelTopic
        mathQuiz = mathTopic
        physicsQuiz = physicTopic
        userResponse = mutableListOf()
    }

    fun updateChosenQuiz(quizName: String)
    fun updateUserResponse(questionNum: Int, answer: Int)

}

class QuizApp() : Application(), TopicRepository {

    override var marvelQuiz: Topic? = null
    override var mathQuiz: Topic? = null
    override var physicsQuiz: Topic? = null
    override var userResponse: MutableList<Int>? = null
    var chosenQuiz: String? = null

    fun getSelectedQuiz(): Topic {
        var selectedQuiz = marvelQuiz
        when(chosenQuiz) {
            "Math" -> selectedQuiz = mathQuiz
            "Physics" -> selectedQuiz = physicsQuiz
        }
        return selectedQuiz!!
    }

    override fun updateUserResponse(questionNum: Int, answer: Int) {
        userResponse!![questionNum] = answer
    }

    override fun updateChosenQuiz(quizName: String) {
        chosenQuiz = quizName
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "An QuizApp was created!")
    }

    class Question (val question: String, val choices: Array<String>, val answer: Int) {}

    class Topic (val title: String, val description: String, val questions: Array<Question>) {}

}
