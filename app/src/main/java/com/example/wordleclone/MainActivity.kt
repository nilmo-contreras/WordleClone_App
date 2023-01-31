package com.example.wordleclone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var attempts = 1
    private var wordToGuess = FourLetterWordList.getRandomFourLetterWord()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userGuess = findViewById<EditText>(R.id.editTextUserGuess)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val correctWord = findViewById<TextView>(R.id.correctWord)
        val attemptsCounter = findViewById<TextView>(R.id.attemptsCounter)
        val firstGuess = findViewById<TextView>(R.id.attempt1)
        val secondGuess = findViewById<TextView>(R.id.attempt2)
        val thirdGuess = findViewById<TextView>(R.id.attempt3)
        val fourthGuess = findViewById<TextView>(R.id.attempt4)

        correctWord.text = wordToGuess

        submitButton.setOnClickListener {
            val guess = userGuess.text.toString().uppercase()
            if (guess.length == 4){
                attemptsCounter.text = "Attempt: $attempts"

                if (guessCorrectness(guess).toString() == "OOOO") {
                    submitButton.visibility = View.GONE
                    resetButton.visibility = View.VISIBLE
                    attemptsCounter.text = "You Got it Right!!! 🎉"
                }

                if (attempts == 1)
                    firstGuess.text = guessCorrectness(guess)
                if (attempts == 2)
                    secondGuess.text = guessCorrectness(guess)
                if (attempts == 3)
                    thirdGuess.text = guessCorrectness(guess)
                if (attempts >= 3) {
                    attemptsCounter.text = "No more attempts 😞"
                    submitButton.visibility = View.GONE
                    correctWord.visibility = View.VISIBLE
                }

                attempts++
            } else {
                userGuess.error = "Guess must be 4 letters long"
                userGuess.text.clear()
            }
        }

        resetButton.setOnClickListener {
            attemptsCounter.text = "Guess the Word 😉"
            userGuess.text.clear()
            firstGuess.text = ""
            secondGuess.text = ""
            thirdGuess.text = ""
            fourthGuess.text = ""
            submitButton.visibility = View.VISIBLE
            correctWord.visibility = View.INVISIBLE
            attempts = 1
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        }
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }

    private fun guessCorrectness(guess: String) : SpannableString {
        val correctness = SpannableString(guess)
        if (checkGuess(guess) == "OOOO") {
            correctness.setSpan(ForegroundColorSpan(Color.GREEN), 0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        var j = 0
        for (i in checkGuess(guess)) {
            if (i == 'O') {
                correctness.setSpan(ForegroundColorSpan(Color.GREEN), j, j+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            if (i == '+') {
                correctness.setSpan(ForegroundColorSpan(Color.YELLOW), j, j+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            if (i == 'X') {
                correctness.setSpan(ForegroundColorSpan(Color.RED), j, j+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            j++
        }

        return correctness
    }
}