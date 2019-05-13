package edu.uw.ischool.elisat15.quizdrone


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 *
 */
class overview : Fragment() {

    val quizApp: QuizApp = QuizApp()
    private var overviewListener: OnBeginQuizClickListener? = null

    interface OnBeginQuizClickListener {
        fun onBeginBtnClick(category: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_overview, container, false)

        val category = arguments?.getString("category")
        Log.d("debug", category)
        quizApp.topicRepository.fetchData(this.activity!!)
        val quizInfo = quizApp.accessRepository(category!!)
        val quizLength = quizInfo.questions.size

        val categoryQuiz = v.findViewById<TextView>(R.id.overview_header)
        categoryQuiz.text =  "${quizInfo.title} Quiz"

        val categoryQuizDescription = v.findViewById<TextView>(R.id.overview_description)
        categoryQuizDescription.text = quizInfo.description

        val numQuestion = v.findViewById<TextView>(R.id.overview_numQuestion)
        numQuestion.text = numQuestion.text.toString() + quizLength

        val beginBtn = v.findViewById<Button>(R.id.begin_quiz_btn)
        beginBtn.setOnClickListener() {
            overviewListener?.onBeginBtnClick(category)
        }

        // Inflate the layout for this fragment
        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBeginQuizClickListener) {
            overviewListener = context
        } else {
            throw error(context.toString() + " must implement OnBeginQuizClickListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        overviewListener =  null
    }

}
