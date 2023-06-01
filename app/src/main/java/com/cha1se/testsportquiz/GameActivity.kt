package com.cha1se.testsportquiz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GameActivity : AppCompatActivity() {

    private lateinit var timerGame: TextView
    private lateinit var questionText: TextView
    private lateinit var answerA: TextView
    private lateinit var answerB: TextView
    private lateinit var answerC: TextView
    private lateinit var answerD: TextView
    private lateinit var nextButton: TextView

    private var rightAnswers: Int = 0
    private var questionNumber: Int = 0
    private lateinit var chooseVariant: TextView
    private lateinit var answer: String
    private var difficult: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        timerGame = findViewById(R.id.timerGame)
        questionText = findViewById(R.id.questionTextView)
        answerA = findViewById(R.id.answer_a)
        answerB = findViewById(R.id.answer_b)
        answerC = findViewById(R.id.answer_c)
        answerD = findViewById(R.id.answer_d)
        nextButton = findViewById(R.id.nextButton)

        difficult = intent.getIntExtra("difficult", 1)

        nextButton.setOnClickListener {
            if (chooseVariant.text != nextButton.text) {
                checkRight()
            }
            questionNumber++
            if (questionNumber <= 4) {
                getDataFromDB()
            } else {
                Toast.makeText(this, "Вы заработали - ${rightAnswers} очков!", Toast.LENGTH_LONG)
                    .show()
                setScore()
            }

        }

        answerA.setOnClickListener {
            onClickVariant(it)
        }
        answerB.setOnClickListener {
            onClickVariant(it)
        }
        answerC.setOnClickListener {
            onClickVariant(it)
        }
        answerD.setOnClickListener {
            onClickVariant(it)
        }

        getDataFromDB()

    }

    fun setScore() {
        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            var quiz = quizDAO.getQuiz().last()

            quizDAO.updateQuiz(quiz.copy(score = quiz.score + rightAnswers))

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .subscribe()
    }

    fun checkRight() {

        if (chooseVariant.text == answer) {
            rightAnswers++
        }
        resetVariants()

    }

    fun onClickVariant(view: View) {
        var variant = view as TextView
        variant.setBackgroundColor(Color.GRAY)
        chooseVariant = variant
    }

    fun resetVariants() {
        answerA.setBackgroundColor(Color.WHITE)
        answerB.setBackgroundColor(Color.WHITE)
        answerC.setBackgroundColor(Color.WHITE)
        answerD.setBackgroundColor(Color.WHITE)
        chooseVariant = nextButton
    }

    fun getDataFromDB() {

        lateinit var question: String
        lateinit var variants: List<String>

        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()

            when (difficult) {
                1 -> {
                    question =
                        quizDAO.getSimpleQuizById(questionNumber).last().questions //TODO edit

                    variants = listOf(
                        quizDAO.getVariantsSimpleById(questionNumber).last().variant1,
                        quizDAO.getVariantsSimpleById(questionNumber).last().variant2,
                        quizDAO.getVariantsSimpleById(questionNumber).last().variant3,
                        quizDAO.getVariantsSimpleById(questionNumber).last().variant4
                    )
                    answer = quizDAO.getSimpleQuizById(questionNumber).last().answers
                }

                2 -> {
                    question =
                        quizDAO.getMediumQuizById(questionNumber).last().questions //TODO edit

                    variants = listOf(
                        quizDAO.getVariantsMediumById(questionNumber).last().variant1,
                        quizDAO.getVariantsMediumById(questionNumber).last().variant2,
                        quizDAO.getVariantsMediumById(questionNumber).last().variant3,
                        quizDAO.getVariantsMediumById(questionNumber).last().variant4
                    )
                    answer = quizDAO.getMediumQuizById(questionNumber).last().answers
                }

                3 -> {
                    question = quizDAO.getHardQuizById(questionNumber).last().questions //TODO edit

                    variants = listOf(
                        quizDAO.getVariantsHardById(questionNumber).last().variant1,
                        quizDAO.getVariantsHardById(questionNumber).last().variant2,
                        quizDAO.getVariantsHardById(questionNumber).last().variant3,
                        quizDAO.getVariantsHardById(questionNumber).last().variant4
                    )
                    answer = quizDAO.getHardQuizById(questionNumber).last().answers
                }
            }


        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {

                questionText.text = question
                answerA.text = variants[0]
                answerB.text = variants[1]
                answerC.text = variants[2]
                answerD.text = variants[3]

            }
            .subscribe()

    }
}