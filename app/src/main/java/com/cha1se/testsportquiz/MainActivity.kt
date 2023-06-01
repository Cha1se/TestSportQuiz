package com.cha1se.testsportquiz

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var playButton: ImageView
    private lateinit var wallpapersBuyButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.playButton)
        wallpapersBuyButton = findViewById(R.id.wallpapersBuy)
        scoreText = findViewById(R.id.scoreTextView)
        var db = AppDatabase.getAppDatabase(this)

        playButton.setOnClickListener {
            showDifficultDialog()
        }

        wallpapersBuyButton.setOnClickListener {
            var intent = Intent(this@MainActivity, WallpapersActivity::class.java)
            startActivity(intent)
        }
        onFirstStartShowDeaflultCards()

    }

    fun onFirstStartShowDeaflultCards() {
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            setDefaultDataInDB()
        } else {
            setScore()
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()
    }

    fun setScore() {
        var score: Int = 0
        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            score = quizDAO.getQuiz().last().score

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                scoreText.text = score.toString()
            }
            .subscribe()
    }

    fun setDefaultDataInDB() {
        Observable.fromCallable {

            val quizDAO = AppDatabase.getAppDatabase(this)!!.quizDao()

            quizDAO.insertUnblockedWalls(
                UnblockedWalls(
                    id = 0,
                    isUnblockedWall = false
                )
            )
            quizDAO.insertUnblockedWalls(
                UnblockedWalls(
                    id = 1,
                    isUnblockedWall = false
                )
            )
            quizDAO.insertUnblockedWalls(
                UnblockedWalls(
                    id = 2,
                    isUnblockedWall = false
                )
            )
            quizDAO.insertUnblockedWalls(
                UnblockedWalls(
                    id = 3,
                    isUnblockedWall = false
                )
            )

            quizDAO.insertQuiz(
                Quiz(
                    id = 0,
                    score = 0
                )
            )

            quizDAO.insertVariantsSimple(
                VariantsSimple(
                    id = 0,
                    variant1 = "16 дюймов",
                    variant2 = "18.5 дюймов",
                    variant3 = "16.25 дйюмов",
                    variant4 = "17 дюймов"
                )
            )

            quizDAO.insertSimpleQuiz(
                SimpleQuiz(
                    id = 0,
                    questions = "Какой диаметр баскетбольного кольца в дюймах?",
                    answers = "18 дюймов"
                )
            )


            quizDAO.insertVariantsSimple(
                VariantsSimple(
                    id = 1,
                    variant1 = "50 метров в длину и 25 метров в ширину",
                    variant2 = "40 метров в длину и 15 метров в ширину",
                    variant3 = "50 метров в длину и 15 метров в ширину",
                    variant4 = "40 метров в длину и 30 метров в ширину"
                )
            )

            quizDAO.insertSimpleQuiz(
                SimpleQuiz(
                    id = 1,
                    questions = "Насколько велик бассейн олимпийских размеров в метрах?",
                    answers = "50 метров в длину и 25 метров в ширину"
                )
            )


            quizDAO.insertVariantsSimple(
                VariantsSimple(
                    id = 2,
                    variant1 = "10 футов",
                    variant2 = "20 футов",
                    variant3 = "11 футов",
                    variant4 = "22 фута"
                )
            )

            quizDAO.insertSimpleQuiz(
                SimpleQuiz(
                    id = 2,
                    questions = "Насколько высоко баскетбольное кольцо находится в профессиональном баскетболе над землей?",
                    answers = "10 футов"
                )
            )


            quizDAO.insertVariantsSimple(
                VariantsSimple(
                    id = 3,
                    variant1 = "от 9 до 9 1/4 дюйма в окружности",
                    variant2 = "от 10 до 10 1/4 дюйма в окружности",
                    variant3 = "от 12 до 13 1/2 дюйма в окружности",
                    variant4 = "от 9 до 10 1/4 дюйма в окружности"
                )
            )

            quizDAO.insertSimpleQuiz(
                SimpleQuiz(
                    id = 3,
                    questions = "Насколько велик бейсбол?",
                    answers = "от 9 до 9 1/4 дюйма в окружности"
                )
            )


            quizDAO.insertVariantsSimple(
                VariantsSimple(
                    id = 4,
                    variant1 = "3 года",
                    variant2 = "8 года",
                    variant3 = "2 года",
                    variant4 = "4 года"
                )
            )

            quizDAO.insertSimpleQuiz(
                SimpleQuiz(
                    id = 4,
                    questions = "Сколько лет проводятся Олимпийские игры?",
                    answers = "4 года"
                )
            )


            // Medium
            quizDAO.insertVariantsMedium(
                VariantsMedium(
                    id = 0,
                    variant1 = "Юлиус Эрвинг",
                    variant2 = "Гай Полай",
                    variant3 = "Юлиус Джет",
                    variant4 = "Джо Эрвинг"
                )
            )

            quizDAO.insertMediumQuiz(
                MediumQuiz(
                    id = 0,
                    questions = "Все мы знаем доктора Дж., Но как его настоящее имя?",
                    answers = "Юлиус Эрвинг"
                )
            )


            quizDAO.insertVariantsMedium(
                VariantsMedium(
                    id = 1,
                    variant1 = "100 баллов",
                    variant2 = "99 баллов",
                    variant3 = "84 баллов",
                    variant4 = "94 баллов"
                )
            )

            quizDAO.insertMediumQuiz(
                MediumQuiz(
                    id = 1,
                    questions = "Каков рекорд Уилта Чемберлена по количеству очков в одной игре?",
                    answers = "100 баллов"
                )
            )

            quizDAO.insertVariantsMedium(
                VariantsMedium(
                    id = 2,
                    variant1 = "Джордж Вашингтон",
                    variant2 = "Джордж Ярдли",
                    variant3 = "Эмиль Рахмедов",
                    variant4 = "Махмед Магомедов"
                )
            )

            quizDAO.insertMediumQuiz(
                MediumQuiz(
                    id = 2,
                    questions = "Кто был первым игроком НБА, набравшим 2019 очков за один сезон?",
                    answers = "Джордж Ярдли"
                )
            )


            quizDAO.insertVariantsMedium(
                VariantsMedium(
                    id = 3,
                    variant1 = "Джордж Вашингтон",
                    variant2 = "Чарльз Баркли",
                    variant3 = "Григорий Крутой",
                    variant4 = "Степан Абрамов"
                )
            )

            quizDAO.insertMediumQuiz(
                MediumQuiz(
                    id = 3,
                    questions = "Считающийся одним из величайших подбирающих игроков всех времен, этот игрок был известен своим имиджем плохого парня и тем, что он стал самым ценным игроком Матча звезд в 1991 году",
                    answers = "Чарльз Баркли"
                )
            )


            quizDAO.insertVariantsMedium(
                VariantsMedium(
                    id = 4,
                    variant1 = "6 личных фолов",
                    variant2 = "8 личных фолов",
                    variant3 = "11 личных фолов",
                    variant4 = "16 личных фолов"
                )
            )

            quizDAO.insertMediumQuiz(
                MediumQuiz(
                    id = 4,
                    questions = "Сколько личных фолов удастся исключить из баскетбольного матча НБА?",
                    answers = "6 личных фолов"
                )
            )


            // Hard
            quizDAO.insertVariantsHard(
                VariantsHard(
                    id = 0,
                    variant1 = "Скейтбординг",
                    variant2 = "Лыжи",
                    variant3 = "Сноубординг",
                    variant4 = "Гольф"
                )
            )

            quizDAO.insertHardQuiz(
                HardQuiz(
                    id = 0,
                    questions = "В каком виде спорта обычно используются термины «несвежая рыба» и «буханка»?",
                    answers = "Сноубординг"
                )
            )


            quizDAO.insertVariantsHard(
                VariantsHard(
                    id = 1,
                    variant1 = "Футбол",
                    variant2 = "Регби",
                    variant3 = "Поло",
                    variant4 = "Дон"
                )
            )

            quizDAO.insertHardQuiz(
                HardQuiz(
                    id = 1,
                    questions = "В какой вид спорта играют на самом большом поле, если рассматривать его с точки зрения площади покрытия?",
                    answers = "Поло"
                )
            )


            quizDAO.insertVariantsHard(
                VariantsHard(
                    id = 2,
                    variant1 = "155",
                    variant2 = "156",
                    variant3 = "255",
                    variant4 = "225"
                )
            )

            quizDAO.insertHardQuiz(
                HardQuiz(
                    id = 2,
                    questions = "Каков максимальный брейк, который может получить игрок в игре в снукер?",
                    answers = "155"
                )
            )


            quizDAO.insertVariantsHard(
                VariantsHard(
                    id = 3,
                    variant1 = "11 индивидуальных ставок",
                    variant2 = "13 индивидуальных ставок",
                    variant3 = "Все на одного",
                    variant4 = "Ни один не победил"
                )
            )

            quizDAO.insertHardQuiz(
                HardQuiz(
                    id = 3,
                    questions = "Что такое янки в спортивных скачках?",
                    answers = "11 индивидуальных ставок"
                )
            )


            quizDAO.insertVariantsHard(
                VariantsHard(
                    id = 4,
                    variant1 = "7 минут",
                    variant2 = "10 минут",
                    variant3 = "Нет времени",
                    variant4 = "5 минут"
                )
            )

            quizDAO.insertHardQuiz(
                HardQuiz(
                    id = 4,
                    questions = "Знаете ли вы, что игрокам в гольф дается только определенный период времени, чтобы найти свой мяч для гольфа? Как долго игрокам разрешено искать мяч?",
                    answers = "5 минут"
                )
            )

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                setScore()
            }
            .subscribe()
    }

    fun showDifficultDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.difficult_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val simpleDiff: TextView = dialog.findViewById(R.id.simpleDiff)
        val mediumDiff: TextView = dialog.findViewById(R.id.mediumDiff)
        val hardDiff: TextView = dialog.findViewById(R.id.hardDiff)

        simpleDiff.setOnClickListener {
            var intent = Intent(this@MainActivity, GameActivity::class.java)
            intent.putExtra("difficult", 1)
            startActivity(intent)
        }
        mediumDiff.setOnClickListener {
            var intent = Intent(this@MainActivity, GameActivity::class.java)
            intent.putExtra("difficult", 2)
            startActivity(intent)
        }
        hardDiff.setOnClickListener {
            var intent = Intent(this@MainActivity, GameActivity::class.java)
            intent.putExtra("difficult", 3)
            startActivity(intent)
        }
        dialog.show()

    }
}