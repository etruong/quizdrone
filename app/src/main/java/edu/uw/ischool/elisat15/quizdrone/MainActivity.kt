package edu.uw.ischool.elisat15.quizdrone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val SELECTED_CATEGORY: String = "currentCategory"

class MainActivity : AppCompatActivity() {

    val quizApp: QuizApp = QuizApp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sets on click listeners for
        setListener(findViewById(R.id.mathBtn))
        setListener(findViewById(R.id.physicsBtn))
        setListener(findViewById(R.id.marvelBtn))
    }

    private fun setListener(btn: Button) {
        btn.setOnClickListener() {

            val selectedCategory = btn.text.toString()
            quizApp.updateChosenQuiz(selectedCategory)
            Log.d("MainActivity", quizApp.chosenQuiz)
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(SELECTED_CATEGORY, selectedCategory)
            startActivity(intent)
        }
    }

}
