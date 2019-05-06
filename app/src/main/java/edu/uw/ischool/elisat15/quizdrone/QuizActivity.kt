package edu.uw.ischool.elisat15.quizdrone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.Button
import android.widget.TextView

class QuizActivity : AppCompatActivity(),
    overview.OnBeginQuizClickListener, question.SubmitAnswerListener, answer.AnswerListener {

    var categoryLibrary: QuizQuestionLibrary? = null
    var fragmentManager: FragmentManager = supportFragmentManager
    var questionNum: Int = 0
    var answersCorrect: Int = 0

    override fun nextQuestionListener() {
        if (categoryLibrary?.getQuizSize() == questionNum + 1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            questionNum += 1
            val questionBundle = createQuestionBundle(categoryLibrary?.category)
            val questionFragment = question()
            questionFragment.arguments = questionBundle
            replaceFragments(questionFragment)
        }
    }

    override fun submitAnswer(selected: String?) {
        if (selected == categoryLibrary?.getAnswer(questionNum)) {
            answersCorrect += 1
        }

        val answerBundle = Bundle()
        answerBundle.putString("category", categoryLibrary?.category)
        answerBundle.putString("selectedAnswer", selected)
        answerBundle.putString("correctAnswer", categoryLibrary?.getAnswer(questionNum))
        answerBundle.putInt("questionNum", questionNum + 1)
        answerBundle.putString("question", categoryLibrary?.getQuestion(questionNum))
        answerBundle.putInt("totalAnswersCorrect", answersCorrect)
        answerBundle.putBoolean("lastQuestion", categoryLibrary?.getQuizSize() == questionNum + 1)

        val answerFragment = answer()
        answerFragment.arguments = answerBundle
        replaceFragments(answerFragment)
    }

    private fun createQuestionBundle(category: String?): Bundle {
        val questionBundle = Bundle()
        questionBundle.putString("category", category)
        questionBundle.putString("question", categoryLibrary?.getQuestion(questionNum))
        questionBundle.putStringArray("choices", categoryLibrary?.getChoices(questionNum))
        questionBundle.putString("answer", categoryLibrary?.getAnswer(questionNum))
        questionBundle.putInt("questionNum", questionNum)
        return questionBundle
    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.quiz_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBeginBtnClick(category: String?) {
        val questionBundle = createQuestionBundle(category)

        val questionFragment = question()
        questionFragment.arguments = questionBundle

        replaceFragments(questionFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val data = QuizApp()
        data.initData()
        Log.d("QuizActivity", data.chosenQuiz)

        val selectedCategory = intent.getStringExtra(SELECTED_CATEGORY)
        categoryLibrary = QuizQuestionLibrary(selectedCategory)

        val fragmentTransaction = fragmentManager.beginTransaction()

        val overviewBundle = Bundle()
        overviewBundle.putString("category", selectedCategory)
        overviewBundle.putInt("quizLength", categoryLibrary!!.getQuizSize())

        val quizOverviewFragment = overview()
        quizOverviewFragment.arguments = overviewBundle
        fragmentTransaction.add(R.id.quiz_container, quizOverviewFragment)
        fragmentTransaction.commit()
    }


}

class QuizQuestionLibrary constructor(val category: String) {
    // Math Questions below
    private val mathQuestions: Array<String> = arrayOf(
        "What is 5 + 5?",
        "What is 3 * 10?",
        "What is 10 + 1 * 9?",
        "What is -20 * -1 * 30?",
        "What is 5 + (20 * -1) * 8?"
    )
    private val mathOptions: Array<Array<String>> = arrayOf(
        arrayOf("10", "20", "13", "5"),
        arrayOf("0", "10", "3", "30"),
        arrayOf("10", "99", "19", "20"),
        arrayOf("50", "333", "500", "600"),
        arrayOf("111", "555", "155", "0")
    )
    private val mathAnswers: Array<String> = arrayOf("10", "30", "19", "600", "155")

    // Physics Questions below
    private val physicsQuestions: Array<String> = arrayOf(
        "What is the force of gravity?",
        "Sound travels at the fastest speed in...",
        "What is inertia?",
        "What is torque?"
    )
    private val physicsOptions: Array<Array<String>> = arrayOf(
        arrayOf("10 m/s", "15 m/s", "80 m/s", "9.8 m/s"),
        arrayOf("Steel", "Water", "Air", "Vaccum"),
        arrayOf(
            "Something that has to do with how wind moves",
            "Not sure",
            "The tendency for an object to remain in a uniform state",
            "In ert ia..."
        ),
        arrayOf(
            "The twisting force that leads to rotation",
            "Does it have something to do with twerking?",
            "Maybe about a turtle",
            "Not sure"
        )
    )
    private val physicsAnswers: Array<String> = arrayOf(
        "9.8 m/s",
        "Steel",
        "The tendency for an object to remain in a uniform state",
        "The twisting force that leads to rotation"
    )

    // Marvel Questions below
    private val marvelQuestions: Array<String> = arrayOf(
        "Which of these Marvel characters carry a hammer?",
        "Which of these Marvel characters can shoot webs out of his hands?",
        "Is Thanos the good guy?",
        "Which of the following characters below is not a Marvel character?"
    )
    private val marvelOptions: Array<Array<String>> = arrayOf(
        arrayOf("Thor", "Loki", "Bob the Builder", "Spiderman"),
        arrayOf("Doctor Strange", "Iron man", "Bruce Wayne", "Spiderman"),
        arrayOf("NO!", "I guess", "Yes", "Obviously"),
        arrayOf("Black Widow", "Captain America", "Jake", "Buckey"
        )
    )
    private val marvelAnswers: Array<String> = arrayOf(
        "Thor",
        "Spiderman",
        "NO!",
        "The twisting force that leads to rotation",
        "Jake"
    )

    fun getQuestion(questionNum: Int): String {
        var questionSet = mathQuestions
        when (category.toLowerCase()) {
            "physics" -> questionSet = physicsQuestions
            "marvel super heroes" -> questionSet = marvelQuestions
        }
        return questionSet[questionNum]
    }

    fun getQuizSize(): Int {
        var size = mathQuestions.size
        when (category.toLowerCase()) {
            "physics" -> size = physicsQuestions.size
            "marvel super heroes" -> size = marvelQuestions.size
        }
        return size
    }

    fun getAnswer(questionNum: Int): String {
        var questionAnswer = mathAnswers[questionNum]
        when (category.toLowerCase()) {
            "physics" -> questionAnswer = physicsAnswers[questionNum]
            "marvel super heroes" -> questionAnswer = marvelAnswers[questionNum]
        }
        return questionAnswer
    }

    fun getChoices(questionNum: Int): Array<String> {
        var questionChoices = mathOptions[questionNum]
        when (category.toLowerCase()) {
            "physics" -> questionChoices = physicsOptions[questionNum]
            "marvel super heroes" -> questionChoices = marvelOptions[questionNum]
        }
        return questionChoices
    }
}
