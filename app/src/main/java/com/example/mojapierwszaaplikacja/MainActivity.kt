package com.example.mojapierwszaaplikacja

import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {



    private lateinit var timeLeftTextView: TextView
    private  lateinit var gameScoreTextView: TextView
    private lateinit var tapMeButton: Button

    private var score = 0

    private var gameStarted = false

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60

    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        tapMeButton.setOnClickListener{v -> val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce); v.startAnimation(bounceAnimation)
            incrementscore()
        }




        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        }
        else {
            resetGame()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstance: score: $score, time left: $timeLeft")
    }



    private fun incrementscore(){
        if(!gameStarted){
            startGame()
        }

        score+=1
        val newScore = getString(R.string.your_score,score)
        gameScoreTextView.text= newScore


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val button_width = 2*tapMeButton.width
        val button_height = 2*tapMeButton.height
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        tapMeButton.x = Random.nextInt(button_width/2,width - button_width).toFloat() // [0, 60] + 20 => [20, 80] // set defined layout params to Button
        tapMeButton.y = Random.nextInt(button_height/2, height - button_height).toFloat()


    }

    private fun resetGame(){
//        tapMeButton.x = (mWidth/2).toFloat() // [0, 60] + 20 => [20, 80] // set defined layout params to Button
//        tapMeButton.y = (mHeight/2).toFloat()

        score = 0
        val initialScore = getString(R.string.your_score,score)
        gameScoreTextView.text = initialScore
        val initialTimeLeft = getString(R.string.time_left,60)
        timeLeftTextView.text = initialTimeLeft

        gameStarted=false

        countDownTimer = object : CountDownTimer(initialCountDown,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left,timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

    }

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore

        val restoredTime = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTime

        //singleton timer restore
        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }


    private fun startGame(){
        countDownTimer.start()
        gameStarted = true

    }

    private fun endGame(){
    Toast.makeText(this,getString(R.string.game_over,score),Toast.LENGTH_LONG).show()
        resetGame()
    }
}