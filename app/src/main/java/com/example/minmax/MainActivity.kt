package com.example.minmax

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var arr = arrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ')
    private var no = 1
    private val bot = 'x'
    private val pla = '0'
    private var aimode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonTextSize(50f)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            val attrib = window.attributes
            attrib.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        rgUnits?.setOnCheckedChangeListener { _, checkedid ->
            if (checkedid == R.id.twoplayer) {
//                makeVisibleMetricUnitsView()
                twoPlayerMode()
            } else {
//                makeVisibleUSUnitsView()
                aiMode()
            }
        }
        btnrestart.setOnClickListener {
            clearAndSetup()
            btnrestart.visibility = View.GONE
            setClickable(true)
        }

    }

    private fun twoPlayerMode() {
        aimode = false
        clearAndSetup()
        setClickable(true)
        Toast.makeText(this, "TWO PLAYER MODE", Toast.LENGTH_SHORT).show()
    }

    private fun aiMode() {
        aimode = true
        clearAndSetup()
        setClickable(true)
        Toast.makeText(this, "AI MODE ", Toast.LENGTH_SHORT).show()
    }

    fun onclick(view: View) {
        if ((view as Button).text.isNotEmpty()) {
            // Button is already clicked, return immediately
            return
        }

        if (aimode) {
            view.text = "0"
            view.isClickable = false  // Disable the button after clicking
            val position = view.tag.toString().toInt()
            arr[position] = '0'

            if (chekWhoWon(pla)) {
                Toast.makeText(this, "0 WON ", Toast.LENGTH_SHORT).show()
                btnrestart.visibility = View.VISIBLE
                setClickable(false)
            } else {
                no++
                comMove()
                if (chekWhoWon(bot)) {
                    Toast.makeText(this, "X WON ", Toast.LENGTH_SHORT).show()
                    btnrestart.visibility = View.VISIBLE
                    setClickable(false)
                }
            }
        } else {
            if (no % 2 == 0) {
                view.text = "0"
                view.isClickable = false  // Disable the button after clicking
                val position = view.tag.toString().toInt()
                arr[position] = '0'
                if (checkIfWon()) {
                    Toast.makeText(this, "0 WON ", Toast.LENGTH_SHORT).show()
                    btnrestart.visibility = View.VISIBLE
                    setClickable(false)
                }
                no++
            } else {
                view.text = "x"
                view.isClickable = false  // Disable the button after clicking
                val position = view.tag.toString().toInt()
                arr[position] = 'x'
                if (checkIfWon()) {
                    Toast.makeText(this, "X WON ", Toast.LENGTH_SHORT).show()
                    btnrestart.visibility = View.VISIBLE
                    setClickable(false)
                }
                no++
            }
        }

        if (checkForDraw()) {
            Toast.makeText(this, "MATCH DRAW ", Toast.LENGTH_SHORT).show()
            btnrestart.visibility = View.VISIBLE
            setClickable(false)
        }
    }


    private fun clearAndSetup() {
        arr = arrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ')
        id0.text = ""
        id1.text = ""
        id2.text = ""
        id3.text = ""
        id4.text = ""
        id5.text = ""
        id6.text = ""
        id7.text = ""
        id8.text = ""
        no = 1
//        comMove()
        setButtonTextSize(50f)
    }


    private fun checkForDraw(): Boolean {
        for (i in arr) {
            if (i == ' ') {
                return false
            }
        }
        return true
    }

    private fun checkIfWon(): Boolean {
        if (arr[0] == arr[1] && arr[1] == arr[2] && arr[0] != ' ')
            return true
        if (arr[3] == arr[4] && arr[4] == arr[5] && arr[3] != ' ')
            return true
        if (arr[6] == arr[7] && arr[7] == arr[8] && arr[6] != ' ')
            return true
        if (arr[0] == arr[3] && arr[3] == arr[6] && arr[0] != ' ')
            return true
        if (arr[1] == arr[4] && arr[4] == arr[7] && arr[1] != ' ')
            return true
        if (arr[2] == arr[5] && arr[5] == arr[8] && arr[2] != ' ')
            return true
        if (arr[0] == arr[4] && arr[4] == arr[8] && arr[0] != ' ')
            return true
        if (arr[2] == arr[4] && arr[4] == arr[6] && arr[2] != ' ')
            return true
        return false
    }

    private fun chekWhoWon(mark: Char): Boolean {
        if (arr[0] == arr[1] && arr[1] == arr[2] && arr[0] == mark)
            return true
        if (arr[3] == arr[4] && arr[4] == arr[5] && arr[3] == mark)
            return true
        if (arr[6] == arr[7] && arr[7] == arr[8] && arr[6] == mark)
            return true
        if (arr[0] == arr[3] && arr[3] == arr[6] && arr[0] == mark)
            return true
        if (arr[1] == arr[4] && arr[4] == arr[7] && arr[1] == mark)
            return true
        if (arr[2] == arr[5] && arr[5] == arr[8] && arr[2] == mark)
            return true
        if (arr[0] == arr[4] && arr[4] == arr[8] && arr[0] == mark)
            return true
        if (arr[2] == arr[4] && arr[4] == arr[6] && arr[2] == mark)
            return true
        return false
    }


    private fun comMove() {
        var bestscore = -1000
        var bestmove = -1  // Initialize to -1 to catch any errors

        for (i in arr.indices) {
            if (arr[i] == ' ') {  // Only consider empty spots
                arr[i] = bot
                val score = minimax(false)
                arr[i] = ' '
                Log.e("AI Move", "Position: ${i}, Score: ${score}")

                if (score > bestscore) {
                    bestscore = score
                    bestmove = i
                }
            }
        }

        if (bestmove != -1) {  // Ensure a valid move is found
            insertLetter(bot, bestmove)
        } else {
            Log.e("AI Error", "No valid moves available for AI")
        }
    }

    private fun setButtonTextSize(size: Float) {
        val buttons = arrayOf(id0, id1, id2, id3, id4, id5, id6, id7, id8)
        for (button in buttons) {
            button.textSize = size
        }
    }


    private fun insertLetter(ch: Char, bestmove: Int) {
        if (bestmove == 0) {
            id0.text = ch.toString()
        } else if (bestmove == 1) {
            id1.text = ch.toString()
        } else if (bestmove == 2) {
            id2.text = ch.toString()
        } else if (bestmove == 3) {
            id3.text = ch.toString()
        } else if (bestmove == 4) {
            id4.text = ch.toString()
        } else if (bestmove == 5) {
            id5.text = ch.toString()
        } else if (bestmove == 6) {
            id6.text = ch.toString()
        } else if (bestmove == 7) {
            id7.text = ch.toString()
        } else if (bestmove == 8) {
            id8.text = ch.toString()
        }
        arr[bestmove] = ch
    }




    private fun minimax(isMax: Boolean): Int {
        Log.e("bestmove", "best move is calculating ")
        if (chekWhoWon(bot))
            return 1
        if (chekWhoWon(pla))
            return -1
        if (checkForDraw())
            return 0
        if (isMax) {
            var bestscore = -1000
            for (i in arr.indices) {

                if (arr[i] == ' ') {
                    arr[i] = bot
                    val score = minimax(false)
                    arr[i] = ' '
                    if (score > bestscore) {
                        bestscore = score
                    }
                }
            }
            return bestscore

        } else {
            var bestscore = 1000
            for (i in arr.indices) {

                if (arr[i] == ' ') {
                    arr[i] = pla
                    val score = minimax(true)
                    arr[i] = ' '
                    if (score < bestscore) {
                        bestscore = score

                    }
                }
            }
            return bestscore
        }

    }

    private fun setClickable(isClickable: Boolean) {
        id0.isClickable = isClickable
        id1.isClickable = isClickable
        id2.isClickable = isClickable
        id3.isClickable = isClickable
        id4.isClickable = isClickable
        id5.isClickable = isClickable
        id6.isClickable = isClickable
        id7.isClickable = isClickable
        id8.isClickable = isClickable
    }

}