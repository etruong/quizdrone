package edu.uw.ischool.elisat15.quizdrone

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import org.w3c.dom.Text

class question : Fragment() {

    interface SubmitAnswerListener {
        fun submitAnswer(selected: String?)
    }

    private var questionListener: SubmitAnswerListener? = null
    val quizApp: QuizApp = QuizApp()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizApp.topicRepository.fetchData(this.activity!!)
        val questionContent = quizApp.accessRepository(arguments?.getString("category")!!).questions[arguments!!.getInt("questionNum")]
        val v = inflater.inflate(R.layout.fragment_question, container, false)

        val questionContainer = v.findViewById<TextView>(R.id.question_container)
        questionContainer.text = questionContent.question

        val header = v.findViewById<TextView>(R.id.question_header)
        header.text = "${arguments?.getString("category")!!} Quiz Question " +
                "${arguments?.getInt("questionNum")?.plus(1)}"

        val choices = questionContent.choices

        val choiceOne = v.findViewById<RadioButton>(R.id.choice1)
        choiceOne.text = choices.get(0)
        val choiceTwo = v.findViewById<RadioButton>(R.id.choice2)
        choiceTwo.text = choices.get(1)
        val choiceThree = v.findViewById<RadioButton>(R.id.choice3)
        choiceThree.text = choices.get(2)
        val choiceFour = v.findViewById<RadioButton>(R.id.choice4)
        choiceFour.text = choices.get(3)

        val choicesContainer = v.findViewById<RadioGroup>(R.id.choices)

        val submitAnswerBtn = v.findViewById<Button>(R.id.answer_btn)
        submitAnswerBtn.setOnClickListener() {
            if (choicesContainer.checkedRadioButtonId != -1) {
                val selectedAnswer = v.findViewById<RadioButton>(choicesContainer.checkedRadioButtonId).text.toString()
                questionListener?.submitAnswer(selectedAnswer)
            }
        }

        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is SubmitAnswerListener)
            questionListener = context
        else
            throw error(context.toString() + " must implement SubmitAnswerListener interface")
    }

    override fun onDetach() {
        super.onDetach()
        questionListener = null
    }

}
