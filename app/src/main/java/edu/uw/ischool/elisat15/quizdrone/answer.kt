package edu.uw.ischool.elisat15.quizdrone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class answer : Fragment() {

    private var listener: AnswerListener? = null
    val quizApp: QuizApp = QuizApp()

    interface AnswerListener {
        fun nextQuestionListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizApp.initData()
        val questionContent = quizApp.getSelectedQuiz(arguments?.getString("category")!!).questions[arguments!!.getInt("questionNum")]
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_answer, container, false)

        val categoryHeader = v.findViewById<TextView>(R.id.answer_header)
        categoryHeader.text = "${arguments?.getString("category")} Quiz Question" +
                " ${arguments?.getInt("questionNum")!!.plus(1)}"

        val question = v.findViewById<TextView>(R.id.question)
        question.text = questionContent.question

        val correctAnswer = v.findViewById<TextView>(R.id.correct_answer)
        correctAnswer.text = "The correct answer is: ${questionContent.choices[questionContent.answer]}"

        val yourAnswer = v.findViewById<TextView>(R.id.your_answer)
        yourAnswer.text = "Your answer was: ${arguments?.getString("selectedAnswer")}"

        val stats = v.findViewById<TextView>(R.id.stats)
        stats.text = "You have ${arguments?.getInt("totalAnswersCorrect")} out of " +
                "${arguments?.getInt("questionNum")!!.plus(1)}" +
                " questions correct!"

        val lastQuestion = quizApp.getSelectedQuiz(arguments?.getString("category")!!).questions.size ==
                (arguments?.getInt("questionNum")!!.plus(1))
        val nextQuestionbtn = v.findViewById<Button>(R.id.next_btn)
        if (lastQuestion) {
            nextQuestionbtn.text = "Finish Quiz"
        }

        nextQuestionbtn.setOnClickListener() {
            listener?.nextQuestionListener()
        }

        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AnswerListener) {
            listener = context
        } else {
            throw error(context.toString() + " must implement AnswerListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener =  null
    }

}
