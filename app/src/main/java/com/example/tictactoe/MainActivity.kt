package com.example.tictactoe

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import android.widget.Chronometer.OnChronometerTickListener

class MainActivity : AppCompatActivity() {

    var hasToRun = false

    var timeLaunched = false

    var gameActive = true

    var currentPlayer = 1
    var gameState: IntArray = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    var xWin = 0
    var oWin = 0

    val time = 30000

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

    private fun setScore() {
        var xScoreV =  findViewById<TextView>(R.id.xScore)
        xScoreV.text = xWin.toString()
        var oScoreV =  findViewById<TextView>(R.id.oScore)
        oScoreV.text = oWin.toString()
    }

    private fun hideGrid(hasToHide : Boolean) {
        val btnReset = findViewById<Button>(R.id.btnReset)
        val grid = findViewById<LinearLayout>(R.id.gridLayout)
        val imageView = findViewById<ImageView>(R.id.imageView)
        if (hasToHide) {
            btnReset.visibility = View.VISIBLE
            grid.visibility = View.GONE
            imageView.visibility = View.GONE
        } else {
            btnReset.visibility = View.GONE
            grid.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
        }

    }

    private fun resetValue() {
        val chrono = findViewById<Chronometer>(R.id.view_timer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chrono.isCountDown = true
        }
        chrono.base = SystemClock.elapsedRealtime() + time
        chrono.start()
        hasToRun = true
        oWin = 0
        xWin = 0
        setScore()
        pressCounter = 0
        gameActive = false
        gameReset()
        hideGrid(false)
    }

    private fun init() {
        oWin = 0
        xWin = 0
        gameReset()
        hasToRun = true
        timeLaunched = true
        val resetBtn = findViewById<Button>(R.id.btnReset)
        resetBtn.setOnClickListener {
            resetValue()
        }
        resetBtn.visibility = View.GONE
        val chrono = findViewById<Chronometer>(R.id.view_timer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chrono.isCountDown = true
        }
        chrono.base = SystemClock.elapsedRealtime() + time
        chrono.start()
        chrono.onChronometerTickListener = OnChronometerTickListener {
            val currentTime = chrono.text.toString()
            if (currentTime == "00:00") {
                hasToRun = false
                chrono.stop()
                var finalMessage = ""
                finalMessage = if (xWin == oWin)
                    "${getString(R.string.draw)} $xWin | $oWin"
                else if (xWin > oWin)
                    "${getString(R.string.x_won)} $xWin / $oWin"
                else
                    "${getString(R.string.o_won)} $oWin / $xWin"
                hideGrid(true)
                val status = findViewById<TextView>(R.id.status)
                status.text = finalMessage

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
                status.text = getString(R.string.x_turn)
            } else {
                img.setImageResource(R.drawable.cross)
                currentPlayer = 0
                val status = findViewById<TextView>(R.id.status)
                status.text = getString(R.string.o_turn)
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

    private fun onTap(id : Int) {
        val btn = findViewById<ImageView>(id)
        btn.setOnClickListener {
            playerTap(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onTap(R.id.imageView0)
        onTap(R.id.imageView1)
        onTap(R.id.imageView2)
        onTap(R.id.imageView3)
        onTap(R.id.imageView4)
        onTap(R.id.imageView5)
        onTap(R.id.imageView6)
        onTap(R.id.imageView7)
        onTap(R.id.imageView8)
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
        status.text = getString(R.string.x_turn)
    }
}