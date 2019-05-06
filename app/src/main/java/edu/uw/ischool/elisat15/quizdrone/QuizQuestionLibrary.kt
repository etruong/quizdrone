package edu.uw.ischool.elisat15.quizdrone

class QuizQuestion constructor(val category: String) {

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
            "marvel" -> questionSet = marvelQuestions
        }
        return questionSet[questionNum]
    }

    fun getAnswer(questionNum: Int): String {
        var questionAnswer = mathAnswers[questionNum]
        when (category.toLowerCase()) {
            "physics" -> questionAnswer = physicsAnswers[questionNum]
            "marvel" -> questionAnswer = marvelAnswers[questionNum]
        }
        return questionAnswer
    }

    fun getChoices(questionNum: Int): Array<String> {
        var questionChoices = mathOptions[questionNum]
        when (category.toLowerCase()) {
            "physics" -> questionChoices = physicsOptions[questionNum]
            "marvel" -> questionChoices = marvelOptions[questionNum]
        }
        return questionChoices
    }

}