package com.example.nallanudi.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.nallanudi.R
import com.example.nallanudi.databinding.ActivityQuizBinding
import com.example.nallanudi.model.Word
import com.example.nallanudi.viewmodel.WordViewModel

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModelByLazy: WordViewModel by lazy {
        ViewModelProvider(this)[WordViewModel::class.java]
    }
    
    private var allWords: List<Word> = emptyList()
    private lateinit var currentQuestion: Word
    private var options: List<Word> = emptyList()
    private var score = 0

    private var currentQuestionIndex = 0
    private val totalQuestions = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelByLazy.allWords.observe(this) { words ->
            if (words.isNotEmpty()) {
                allWords = words.shuffled()
                startNewQuestion()
            }
        }

        binding.btnOption1.setOnClickListener { checkAnswer(0) }
        binding.btnOption2.setOnClickListener { checkAnswer(1) }
        binding.btnOption3.setOnClickListener { checkAnswer(2) }
    }

    private fun startNewQuestion() {
        if (allWords.size < 3 || currentQuestionIndex >= totalQuestions) {
            // End quiz logic could go here
            currentQuestionIndex = 0
            score = 0
            binding.tvScore.text = "Score: 0"
        }

        currentQuestion = allWords[currentQuestionIndex % allWords.size]
        val distractors = allWords.filter { it.english != currentQuestion.english }.shuffled().take(2)
        options = (distractors + currentQuestion).shuffled()

        binding.tvQuestionWord.text = currentQuestion.english
        binding.btnOption1.text = options[0].kannada
        binding.btnOption2.text = options[1].kannada
        binding.btnOption3.text = options[2].kannada

        binding.quizProgress.setProgress(((currentQuestionIndex.toFloat() / totalQuestions) * 100).toInt(), true)
        
        resetButtons()
        runEntranceAnimations()
    }

    private fun runEntranceAnimations() {
        binding.questionCard.alpha = 0f
        binding.questionCard.translationY = -30f
        binding.questionCard.animate().alpha(1f).translationY(0f).setDuration(400).start()
        
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3)
        buttons.forEachIndexed { index, button ->
            button.alpha = 0f
            button.translationX = if (index % 2 == 0) -50f else 50f
            button.animate().alpha(1f).translationX(0f).setDuration(400).setStartDelay(100L * index).start()
        }
    }

    private fun checkAnswer(index: Int) {
        val selectedWord = options[index]
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3)
        
        currentQuestionIndex++

        if (selectedWord.english == currentQuestion.english) {
            score++
            binding.tvScore.text = "Score: $score"
            buttons[index].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_500))
            buttons[index].setTextColor(ContextCompat.getColor(this, R.color.white))
            
            // Animation for correct answer
            buttons[index].animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).withEndAction {
                buttons[index].animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }.start()
        } else {
            buttons[index].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_500))
            buttons[index].setTextColor(ContextCompat.getColor(this, R.color.white))
            
            // Shake animation for wrong answer
            buttons[index].animate().translationX(20f).setDuration(50).withEndAction {
                buttons[index].animate().translationX(-20f).setDuration(50).withEndAction {
                    buttons[index].animate().translationX(0f).setDuration(50).start()
                }.start()
            }.start()
            
            // Show correct answer
            val correctIndex = options.indexOfFirst { it.english == currentQuestion.english }
            buttons[correctIndex].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_500))
            buttons[correctIndex].setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startNewQuestion()
        }, 1200)
    }

    private fun resetButtons() {
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3)
        buttons.forEach {
            it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            it.setTextColor(ContextCompat.getColor(this, R.color.slate_800))
        }
    }
}
