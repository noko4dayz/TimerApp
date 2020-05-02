package com.raywenderlich.timefighter

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*


// ADD OST TAKEOVER TO THIS APP AND IT IS PERFECT
class MainActivity : AppCompatActivity() {
  internal lateinit var tapMeButton: Button
  internal lateinit var timeLeftTextView: TextView
    internal lateinit var personaBG: ImageView
  internal lateinit var bruhText: TextView

  internal var score = 0
  internal var gameStarted = false

  internal lateinit var countDownTimer: CountDownTimer
  internal val initialCountDown: Long = 60000
  internal val countDownInterval: Long = 1000
  internal var timeLeftOnTimer: Long = 60000

  var mediaPlayer: MediaPlayer = MediaPlayer()
  companion object {
    private val TAG = MainActivity::class.java.simpleName
    private const val SCORE_KEY = "SCORE_KEY"
    private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    Log.d(TAG, "onCreate called. Score is: $score")


    tapMeButton = findViewById(R.id.tapMeButton)
    timeLeftTextView = findViewById(R.id.timeLeftTextView)
      personaBG = findViewById<ImageView>(R.id.imageView)
      bruhText = findViewById<TextView>(R.id.textView2)

    mediaPlayer.isLooping = true
    mediaPlayer = MediaPlayer.create(applicationContext, R.raw.talk)
    mediaPlayer.start()
    tapMeButton.setOnClickListener { v ->
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
      v.startAnimation(bounceAnimation)
      incrementScore()
    }

    if (savedInstanceState != null) {
      score = savedInstanceState.getInt(SCORE_KEY)
      timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
      restoreGame()
    } else {
      resetGame()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    outState.putInt(SCORE_KEY, score)
    outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
    countDownTimer.cancel()

    Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy called.")
  }

  private fun incrementScore() {
    if (!gameStarted) {
      startGame()
    }

    score += 1
    val newScore = getString(R.string.yourScore, score)

    val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
  }

  private fun resetGame() {
    // Reset score
    score = 0

    // Show score

    // Show initial time left
    val initialTimeLeft = initialCountDown / 1000
    timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

    countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
      override fun onTick(millisUntilFinished: Long) {
        timeLeftOnTimer = millisUntilFinished
        val timeLeft = millisUntilFinished / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
      }

      override fun onFinish() {
        endGame()
      }
    }

    gameStarted = false
  }

  private fun restoreGame() {

    val restoredTime = timeLeftOnTimer / 1000
    timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

    countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
      override fun onTick(millisUntilFinished: Long) {
        timeLeftOnTimer = millisUntilFinished
        val timeLeft = millisUntilFinished / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
      }

      override fun onFinish() {
        endGame()
      }
    }

    countDownTimer.start()
    gameStarted = true
  }

  private fun startGame() {
    countDownTimer.start()
    gameStarted = true
  }

  fun endGame() {

    // Add evreything here
      personaBG.isInvisible = false
      tapMeButton.isEnabled = false
      tapMeButton.isInvisible = true
      tapMeButton.isClickable = false

    bruhText.isInvisible = true
    mediaPlayer.stop()
    mediaPlayer = MediaPlayer.create(applicationContext, R.raw.takeover)
    mediaPlayer.start()

    Toast.makeText(this, "Now listen to this song", Toast.LENGTH_LONG).show()
    resetGame()
  }
}
