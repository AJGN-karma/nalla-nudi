package com.example.nallanudi.activities

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nallanudi.databinding.ActivityFlashcardBinding
import com.example.nallanudi.model.Word
import com.example.nallanudi.viewmodel.WordViewModel
import kotlin.math.abs

class FlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardBinding
    private val viewModel: WordViewModel by lazy {
        ViewModelProvider(this)[WordViewModel::class.java]
    }
    private var wordsList: List<Word> = emptyList()
    private var currentIndex = 0
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGestureDetector()

        viewModel.favorites.observe(this) { favorites ->
            if (favorites.isEmpty()) {
                Toast.makeText(this, "Add words to Favorites to use Flashcards!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                wordsList = favorites
                showWord()
            }
        }

        binding.btnShowMeaning.setOnClickListener {
            flipCard()
        }

        binding.btnNext.setOnClickListener {
            showNextWord()
        }

        binding.btnPrev.setOnClickListener {
            showPrevWord()
        }

        // Apply touch listener to root and the card itself
        val touchListener = View.OnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            // Return true to indicate we handled the scroll/swipe
            true
        }
        
        binding.root.setOnTouchListener(touchListener)
        binding.flashcard.setOnTouchListener(touchListener)
    }

    private fun setupGestureDetector() {
        val swipeThreshold = 100
        val swipeVelocityThreshold = 100

        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        // Swipe Right -> Previous
                        showPrevWord()
                    } else {
                        // Swipe Left -> Next
                        showNextWord()
                    }
                    return true
                }
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                // Tapping also flips the card
                flipCard()
                return true
            }
        }
        gestureDetector = GestureDetector(this, gestureListener)
    }

    private fun showNextWord() {
        if (currentIndex < wordsList.size - 1) {
            currentIndex++
            showWord()
        } else {
            Toast.makeText(this, "Last word reached", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPrevWord() {
        if (currentIndex > 0) {
            currentIndex--
            showWord()
        } else {
            Toast.makeText(this, "First word reached", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWord() {
        if (wordsList.isNotEmpty()) {
            val word = wordsList[currentIndex]
            binding.flashcardEnglish.text = word.english
            binding.flashcardKannada.text = word.kannada
            binding.flashcardKannada.visibility = View.INVISIBLE
            
            // Update progress
            val progress = ((currentIndex + 1).toFloat() / wordsList.size * 100).toInt()
            binding.progressBar.setProgress(progress, true)

            // Animation for transition
            binding.flashcard.alpha = 0f
            binding.flashcard.translationX = 100f
            binding.flashcard.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(300)
                .start()
        }
    }

    private fun flipCard() {
        binding.flashcard.animate()
            .rotationY(90f)
            .setDuration(150)
            .withEndAction {
                binding.flashcardKannada.visibility = View.VISIBLE
                binding.flashcard.rotationY = -90f
                binding.flashcard.animate()
                    .rotationY(0f)
                    .setDuration(150)
                    .start()
            }
            .start()
    }
}
