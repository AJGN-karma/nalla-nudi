package com.example.nallanudi.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nallanudi.R
import com.example.nallanudi.databinding.ActivityDetailBinding
import com.example.nallanudi.model.Word
import com.example.nallanudi.utils.TtsEngine
import com.example.nallanudi.viewmodel.WordViewModel
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: WordViewModel by lazy {
        ViewModelProvider(this)[WordViewModel::class.java]
    }
    private var ttsEngine: TtsEngine? = null
    private lateinit var currentWord: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wordExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("word", Word::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("word") as? Word
        }
        
        if (wordExtra != null) {
            currentWord = wordExtra
        } else {
            finish()
            return
        }

        ttsEngine = TtsEngine(this)

        displayWord()

        binding.btnSpeak.setOnClickListener {
            ttsEngine?.speak(currentWord.english)
        }

        binding.btnShare.setOnClickListener {
            shareWord()
        }

        binding.fabFavorite.setOnClickListener {
            viewModel.toggleFavorite(currentWord)
            updateFavoriteIcon()
        }
        
        runEntryAnimations()
    }

    private fun shareWord() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareText = "Learning Kannada: \"${currentWord.english}\" means \"${currentWord.kannada}\" - ${currentWord.explanation}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, "Share Word"))
    }

    private fun runEntryAnimations() {
        // Staggered fade in and slide up
        val views = listOf(binding.tvSubject, binding.tvEnglish, binding.tvKannada, binding.divider, binding.labelExplanation, binding.tvExplanation)
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 50f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(100L * index)
                .start()
        }
    }

    private fun displayWord() {
        binding.tvEnglish.text = currentWord.english
        binding.tvKannada.text = currentWord.kannada
        binding.tvExplanation.text = currentWord.explanation
        binding.tvSubject.text = getString(R.string.subject_format, currentWord.subject)
        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        if (currentWord.isFavorite) {
            binding.fabFavorite.setIconResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.fabFavorite.setIconResource(android.R.drawable.btn_star_big_off)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsEngine?.shutdown()
    }
}
