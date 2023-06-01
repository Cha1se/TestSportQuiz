package com.cha1se.testsportquiz

import android.app.Dialog
import android.app.WallpaperManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.drawToBitmap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class WallpapersActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var wallpapersLay: ConstraintLayout
    private lateinit var wall1: ImageView
    private lateinit var wall2: ImageView
    private lateinit var wall3: ImageView
    private lateinit var wall4: ImageView

    private lateinit var unblockedWallsList: List<UnblockedWalls>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpapers)

        wallpapersLay = findViewById(R.id.wallpaperLay)
        scoreText = findViewById(R.id.scoreTextViewWallpaper)
        wall1 = findViewById(R.id.wall1)
        wall2 = findViewById(R.id.wall2)
        wall3 = findViewById(R.id.wall3)
        wall4 = findViewById(R.id.wall4)

        wall1.setOnClickListener {
            checkIsNotBlocked(it)
        }
        wall2.setOnClickListener {
            checkIsNotBlocked(it)
        }
        wall3.setOnClickListener {
            checkIsNotBlocked(it)
        }
        wall4.setOnClickListener {
            checkIsNotBlocked(it)
        }

        setScore()
        getUnblockedWalls()
    }

    fun checkIsNotBlocked(wall: View) {
        var wallpaper = wall as ImageView
        var idWall = wallpapersLay.indexOfChild(wall) - 2

        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            for (el in quizDAO.getUnblockedWalls()) {
                Log.e("TAG", "checkIsNotBlocked: ${el.id} wall = ${idWall}")
                if (el.id == idWall) {
                    if(el.isUnblockedWall) {
                        setWallpaper(wallpaper)
                    }

                }
            }

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun setWallpaper(view: View) {
        Observable.fromCallable {

            val wallpaperManager = WallpaperManager.getInstance(this)
            wallpaperManager.setBitmap(view.drawToBitmap())

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Toast.makeText(this, "Wallpaper is set", Toast.LENGTH_SHORT).show()
            }
            .subscribe()
    }

    fun getBitmapFromView(view: View): Bitmap? {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

    fun DpToPx(dp: Float): Float {
        return if (this != null) {
            val resources = this.resources
            val metrics = resources.displayMetrics
            dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        } else {
            val metrics = Resources.getSystem().displayMetrics
            dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

    fun setScore() {
        var score: Int = 0
        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            score = quizDAO.getQuiz().last().score
            Log.e("TAG", "setScore: ${score}")

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                scoreText.text = score.toString()
            }
            .subscribe()
    }

    fun getUnblockedWalls() {

        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            unblockedWallsList = quizDAO.getUnblockedWalls()

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                for (el in unblockedWallsList) {
                    if (!el.isUnblockedWall) {
                        when (el.id) {
                            0 -> {
                                setBlock(wall1, 10, 0)
                            }
                            1 -> {
                                setBlock(wall2, 20, 1)
                            }
                            2 -> {
                                setBlock(wall3, 30, 2)
                            }
                            3 -> {
                                setBlock(wall4, 40, 3)
                            }
                        }

                    }
                }

            }
            .subscribe()
    }

    fun setBlock(currentWall: View, price: Int, idWall: Int) {

        var blockWall = currentWall as ImageView

        var blockedWallLay = ConstraintLayout(this)
        blockedWallLay.id = ConstraintLayout.generateViewId()
        blockedWallLay.setBackgroundColor(Color.parseColor("#1B1B1B"))

        wallpapersLay.addView(
            blockedWallLay,
            DpToPx(120f).toInt(),
            DpToPx(40f).toInt()
        )

        blockedWallLay.setOnClickListener {
            showBuyDialog(blockedWallLay, idWall, price)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(wallpapersLay)
        constraintSet.connect(
            blockedWallLay.id,
            ConstraintSet.RIGHT,
            blockWall.id,
            ConstraintSet.RIGHT,
            0
        )
        constraintSet.connect(
            blockedWallLay.id,
            ConstraintSet.TOP,
            blockWall.id,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            blockedWallLay.id,
            ConstraintSet.BOTTOM,
            blockWall.id,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            blockedWallLay.id,
            ConstraintSet.LEFT,
            blockWall.id,
            ConstraintSet.LEFT,
            0
        )
        constraintSet.applyTo(wallpapersLay)

        var starImg = ImageView(this)
        starImg.id = ImageView.generateViewId()
        starImg.setImageResource(R.drawable.ico_star)

        blockedWallLay.addView(starImg, DpToPx(30f).toInt(), DpToPx(30f).toInt())

        val constraintSetImg = ConstraintSet()
        constraintSetImg.clone(blockedWallLay)
        constraintSetImg.connect(
            starImg.id,
            ConstraintSet.LEFT,
            blockedWallLay.id,
            ConstraintSet.LEFT,
            DpToPx(8f).toInt()
        )
        constraintSetImg.connect(
            starImg.id,
            ConstraintSet.TOP,
            blockedWallLay.id,
            ConstraintSet.TOP,
            0
        )
        constraintSetImg.connect(
            starImg.id,
            ConstraintSet.BOTTOM,
            blockedWallLay.id,
            ConstraintSet.BOTTOM,
            0
        )

        constraintSetImg.applyTo(blockedWallLay)

        var priceText = TextView(this)
        priceText.id = TextView.generateViewId()
        priceText.text = price.toString()
        priceText.setTextColor(Color.WHITE)
        priceText.textSize = 20f

        blockedWallLay.addView(priceText)

        val constraintSetText = ConstraintSet()
        constraintSetText.clone(blockedWallLay)
        constraintSetText.connect(
            priceText.id,
            ConstraintSet.RIGHT,
            blockedWallLay.id,
            ConstraintSet.RIGHT,
            0
        )
        constraintSetText.connect(
            priceText.id,
            ConstraintSet.TOP,
            blockedWallLay.id,
            ConstraintSet.TOP,
            0
        )
        constraintSetText.connect(
            priceText.id,
            ConstraintSet.BOTTOM,
            blockedWallLay.id,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSetText.connect(
            priceText.id,
            ConstraintSet.LEFT,
            starImg.id,
            ConstraintSet.LEFT,
            0
        )

        constraintSetText.applyTo(blockedWallLay)
    }

    fun showBuyDialog(blockedWallLay: ConstraintLayout, idWall: Int, price: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.buy_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val buyButton: TextView = dialog.findViewById(R.id.buyButton)
        val cancelButton: TextView = dialog.findViewById(R.id.cancel)

        buyButton.setOnClickListener {
            buyWall(blockedWallLay, idWall, price)
            dialog.cancel()
        }
        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    fun buyWall(blockedWallLay: ConstraintLayout, idWall: Int, price: Int) {
        var score: Int = 0
        Observable.fromCallable {

            var db = AppDatabase.getAppDatabase(this)
            var quizDAO = db!!.quizDao()
            score = quizDAO.getQuiz().last().score

            if (quizDAO.getQuiz().last().score >= price) {
                quizDAO.updateUnblockedWalls(UnblockedWalls(
                    id = idWall,
                    isUnblockedWall = true
                ))
                quizDAO.updateQuiz(quizDAO.getQuiz().last().copy(
                    score = quizDAO.getQuiz().last().score - price
                )
                )
            }

        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                if (score >= price) {
                    wallpapersLay.removeView(blockedWallLay)
                }

            }
            .subscribe()
    }

}