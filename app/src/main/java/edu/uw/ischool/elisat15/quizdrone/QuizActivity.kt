package edu.uw.ischool.elisat15.quizdrone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class QuizActivity : AppCompatActivity(),
    overview.OnBeginQuizClickListener, question.SubmitAnswerListener, answer.AnswerListener {

    var fragmentManager: FragmentManager = supportFragmentManager
    var questionNum: Int = 0
    var answersCorrect: Int = 0
    var selectedCategory: String? = null
    val quizApp: QuizApp = QuizApp()
    lateinit var quizInformation: Topic

    override fun nextQuestionListener() {
        if (quizApp.accessRepository(selectedCategory!!).questions.size == questionNum + 1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            questionNum += 1
            val questionBundle = createQuestionBundle(selectedCategory)
            val questionFragment = question()
            questionFragment.arguments = questionBundle
            replaceFragments(questionFragment)
        }
    }

    override fun submitAnswer(selected: String?) {

        var questionAnswer = quizApp.accessRepository(selectedCategory!!).questions[questionNum]
        if (selected == questionAnswer.choices[questionAnswer.answer]) {
            answersCorrect += 1
        }
        Log.d("hello", quizInformation.title)
        val answerBundle = Bundle()
        answerBundle.putString("category", quizInformation.title)
        answerBundle.putString("selectedAnswer", selected)
        answerBundle.putInt("questionNum", questionNum)
        answerBundle.putInt("totalAnswersCorrect", answersCorrect)

        val answerFragment = answer()
        answerFragment.arguments = answerBundle
        replaceFragments(answerFragment)
    }

    private fun createQuestionBundle(category: String?): Bundle {
        val questionBundle = Bundle()
        questionBundle.putString("category", category)
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
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val fragmentTransaction = fragmentManager.beginTransaction()
        selectedCategory = intent.getStringExtra(SELECTED_CATEGORY)
        quizApp.topicRepository.fetchData(this)
        quizInformation = quizApp.accessRepository(selectedCategory!!)

        val overviewBundle = Bundle()
        overviewBundle.putString("category", selectedCategory)
        val quizOverviewFragment = overview()
        quizOverviewFragment.arguments = overviewBundle
        fragmentTransaction.add(R.id.quiz_container, quizOverviewFragment)
        fragmentTransaction.commit()
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