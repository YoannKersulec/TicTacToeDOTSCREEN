package com.example.tictactoe

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import android.widget.Chronometer.OnChronometerTickListener
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    var hasToRun = false

    var timeLaunched = false

    var gameActive = true

    var currentPlayer = 1
    var gameState: IntArray = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    var xWin = 0
    var oWin = 0

    private var winPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    var pressCounter = 0

    fun resetButton(view: View) {
        timeLaunched = false
    }

    private fun setScore() {
        var xScoreV =  findViewById<TextView>(R.id.xScore)
        xScoreV.text = xWin.toString()
        var oScoreV =  findViewById<TextView>(R.id.oScore)
        oScoreV.text = oWin.toString()
    }

    private fun init() {
        oWin = 0
        xWin = 0
        gameReset()
        hasToRun = true
        timeLaunched = true
        val resetBtn = findViewById<Button>(R.id.btnReset)
        resetBtn.setOnClickListener {
//            init()
        }
        resetBtn.visibility = View.GONE
        val chrono = findViewById<Chronometer>(R.id.view_timer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chrono.isCountDown = true
        }
        chrono.base = SystemClock.elapsedRealtime() + 10000
        chrono.start()
        chrono.onChronometerTickListener = OnChronometerTickListener {
            val currentTime = chrono.text.toString()
            if (currentTime == "00:00") {
                hasToRun = false
                chrono.stop()
                var finalMessage = ""
                if (xWin == oWin) {
                    finalMessage = "${getString(R.string.draw)} $xWin | $oWin"
                } else if (xWin > oWin) {
                    finalMessage = "${getString(R.string.x_won)} $xWin / $oWin"
                } else {
                    finalMessage = "${getString(R.string.o_won)} $oWin / $xWin"
                }
                Toast.makeText(this, finalMessage, Toast.LENGTH_LONG).show()
//                resetBtn.visibility = View.VISIBLE
            }
        }
    }

    fun playerTap(view : View) {
        if (!timeLaunched) {
            init()
        }
        if (!hasToRun)
            return
        val img = view as ImageView
        val tappedImage = Integer.parseInt(img.tag.toString())

        if (!gameActive) {
            gameReset()
            pressCounter = 0
        }

        if (gameState[tappedImage] == 2) {
            pressCounter++

            if (pressCounter == 9) {
                gameActive = false;
            }

            gameState[tappedImage] = currentPlayer

            img.translationY = -1000f

            if (currentPlayer == 0) {
                img.setImageResource(R.drawable.circle)
                currentPlayer = 1
                val status = findViewById<TextView>(R.id.status)

                status.text = "X's Turn - Tap to play"
            } else {
                img.setImageResource(R.drawable.cross)
                currentPlayer = 0
                val status = findViewById<TextView>(R.id.status)

                status.text = "O's Turn - Tap to play"
            }
            img.animate().translationYBy(1000f).duration = 300
        }
        checkWin()
    }

    private fun checkWin() {
        var flag = 0
        for (winPosition in winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                gameState[winPosition[1]] == gameState[winPosition[2]] &&
                gameState[winPosition[0]] != 2) {
                flag = 1

                var winnerStr = ""

                gameActive = false
                if (gameState[winPosition[0]] == 0) {
                    winnerStr = getString(R.string.o_won_round)
                    oWin++
                    setScore()
                } else {
                    winnerStr = getString(R.string.x_won_round)
                    xWin++
                    setScore()
                }
                val status = findViewById<TextView>(R.id.status)
                status.text = winnerStr
            }
        }
        if (pressCounter == 9 && flag == 0) {
            val status = findViewById<TextView>(R.id.status)
            status.text = getString(R.string.draw_round)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun gameReset() {
        gameActive = true
        currentPlayer = 1
        for (i in gameState.indices) {
            gameState[i] = 2
        }
        (findViewById<View>(R.id.imageView0) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView1) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView2) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView3) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView4) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView5) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView6) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView7) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView8) as ImageView).setImageResource(0)
        val status = findViewById<TextView>(R.id.status)
        status.text = "X's Turn - Tap to play"
    }
}