package edu.uw.ischool.elisat15.quizdrone

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Context
import android.util.Log

const val TAG: String = "QuizApp"

class QuizApp() : Application() {

    var context: Context = this
    var topicRepository: TopicRepository = OfflineData()
        private set

    companion object {
        lateinit var instance: QuizApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val d = Log.d(TAG, "An QuizApp was created!")
        instance = this
    }

    fun accessRepository(selectedQuiz: String): Topic {
//        topicRepository.fetchData(context)
        return topicRepository.getSelectedQuiz(selectedQuiz)
    }
}
