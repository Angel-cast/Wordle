package com.example.wordle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var wordToGuess: String
    private var guessNumber = 1
    private lateinit var textInputEditText: TextInputEditText
    private lateinit var guess1TextView: TextView
    private lateinit var guess2TextView: TextView
    private lateinit var guess3TextView: TextView
    private lateinit var check1TextView: TextView
    private lateinit var check2TextView: TextView
    private lateinit var check3TextView: TextView
    private lateinit var wordTextView: TextView
    private lateinit var guessButton: Button
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize UI elements
        guess1TextView = findViewById(R.id.textView12)
        guess2TextView = findViewById(R.id.textView13)
        guess3TextView = findViewById(R.id.textView10)
        check1TextView = findViewById(R.id.textView11)
        check2TextView = findViewById(R.id.textView9)
        check3TextView = findViewById(R.id.textView14)
        wordTextView = findViewById(R.id.textView15)
        guessButton = findViewById(R.id.button)
        restartButton = findViewById(R.id.restartButton)
        textInputEditText = findViewById(R.id.textInputEditText)

        //assign word to guess
        assignWordToGuess()

        //set click listeners for guess and restart buttons
        guessButton.setOnClickListener {
            handleGuess()
        }
        restartButton.setOnClickListener {
            restartGame()
        }
    }

    //assigns a word for the player to guess
    private fun assignWordToGuess() {
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        updateWordDisplay()
    }

    //updates the displayed word
    private fun updateWordDisplay() {
        wordTextView.text = wordToGuess
    }

    //shows how correct users guess is
    private fun handleGuess() {
        val guess = textInputEditText.text.toString().uppercase()

        if(isValidGuess(guess)){
            val correctness = checkGuess(guess)

            when (guessNumber) {
                1 -> {
                    guess1TextView.text = guess
                    check1TextView.text = correctness
                    guess1TextView.visibility = View.VISIBLE
                    check1TextView.visibility = View.VISIBLE
                }

                2 -> {
                    guess2TextView.text = guess
                    check2TextView.text = correctness
                    guess2TextView.visibility = View.VISIBLE
                    check2TextView.visibility = View.VISIBLE
                }

                3 -> {
                    guess3TextView.text = guess
                    check3TextView.text = correctness
                    guess3TextView.visibility = View.VISIBLE
                    check3TextView.visibility = View.VISIBLE
                    //show the correct word after three guesses
                    wordTextView.visibility = View.VISIBLE
                }
            }

            //clear EditText field
            textInputEditText.text?.clear()

            //hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(textInputEditText.windowToken, 0)

            guessNumber++

            if (guess == wordToGuess || guessNumber > 3) {
                //disable guess button and hide it after guess is correct
                guessButton.isEnabled = false
                guessButton.visibility = View.INVISIBLE
                //show restart button
                restartButton.visibility = View.VISIBLE
            }
        }

        else {
            showToast("Invalid guess. Must be a four letter word.")
        }
    }

    //displays a toast message if guess is not valid
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //checks guess validity
    private fun isValidGuess(guess: String): Boolean {
        return guess.length == 4 && guess.all { it.isLetter() }
    }

    //restarts the game after guess is correct or after three guesses
    private fun restartGame() {
        //hide restart button and show guess button
        restartButton.visibility = View.GONE
        guessButton.visibility = View.VISIBLE
        guessButton.isEnabled = true
        guessNumber = 1
        assignWordToGuess()
        if (wordTextView.visibility == View.VISIBLE) {
            wordTextView.visibility = View.INVISIBLE
        }

        //clear previous textViews
        guess1TextView.text = ""
        check1TextView.text = ""
        guess2TextView.text = ""
        check2TextView.text = ""
        guess3TextView.text = ""
        check3TextView.text = ""
        guess1TextView.visibility = View.INVISIBLE
        check1TextView.visibility = View.INVISIBLE
        guess2TextView.visibility = View.INVISIBLE
        check2TextView.visibility = View.INVISIBLE
        guess3TextView.visibility = View.INVISIBLE
        check3TextView.visibility = View.INVISIBLE

        //update the displayed word
        updateWordDisplay()
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a CharSequence with colored letters, where:
     *   Green represents the right letter in the right place
     *   Red represents the right letter in the wrong place
     *   Grey represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : CharSequence {
        val result = SpannableStringBuilder()

        for (i in 0..3) {
            val guessChar = guess[i]
            val targetChar = wordToGuess[i]

            val span = if (guessChar == targetChar) {
                ForegroundColorSpan(Color.GREEN)
            }
            else if (guessChar in wordToGuess) {
                ForegroundColorSpan(Color.RED)
            }
            else {
                ForegroundColorSpan(Color.GRAY)
            }

            result.append(guessChar.toString(), span, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return result
    }
}