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
        quizApp.initData()
        quizApp.updateChosenQuiz(arguments?.getString("category"))
        val category = quizApp.chosenQuiz
        val quizLength = quizApp.getSelectedQuiz().questions.size

        val categoryQuiz = v.findViewById<TextView>(R.id.overview_header)
        categoryQuiz.text =  "${quizApp.getSelectedQuiz().title} Quiz"

        val categoryQuizDescription = v.findViewById<TextView>(R.id.overview_description)
        categoryQuizDescription.text = quizApp.getSelectedQuiz().description
//        setDescription(category, categoryQuizDescription)

        val numQuestion = v.findViewById<TextView>(R.id.overview_numQuestion)
        numQuestion.text = numQuestion.text.toString() + quizLength

        val beginBtn = v.findViewById<Button>(R.id.begin_quiz_btn)
        beginBtn.setOnClickListener() {
            overviewListener?.onBeginBtnClick(category)
        }

        // Inflate the layout for this fragment
        return v
    }

//    private fun setDescription(category: String?, description: TextView) {
//        var descriptionText = quizApp.mathQuiz!!.description
//        when (category?.toLowerCase()) {
//            "physics" -> descriptionText = quizApp.physicsQuiz!!.description
//            "marvel super heroes" -> descriptionText = quizApp.marvelQuiz!!.description
//        }
//        description.text = descriptionText
//    }

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
