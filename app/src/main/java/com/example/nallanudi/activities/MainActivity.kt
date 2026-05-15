package com.example.nallanudi.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nallanudi.R
import com.example.nallanudi.adapters.WordAdapter
import com.example.nallanudi.databinding.ActivityMainBinding
import com.example.nallanudi.viewmodel.WordViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WordViewModel by lazy {
        ViewModelProvider(this)[WordViewModel::class.java]
    }
    private lateinit var adapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        setupWordOfTheDay()
        setupBottomNavigation()
        setupSubjectFilters()
    }

    private fun setupSubjectFilters() {
        binding.chipGroupSubjects.setOnCheckedStateChangeListener { group, checkedIds ->
            val chipId = checkedIds.firstOrNull()
            val subject = when (chipId) {
                R.id.chipDaily -> "Daily Life"
                R.id.chipTech -> "Technology"
                R.id.chipEducation -> "Education"
                else -> "All"
            }
            viewModel.subjectFilter.value = subject
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> true
                R.id.nav_favorites -> {
                    startActivity(Intent(this, MyListActivity::class.java))
                    false
                }
                R.id.nav_flashcards -> {
                    startActivity(Intent(this, FlashcardActivity::class.java))
                    false
                }
                R.id.nav_quiz -> {
                    startActivity(Intent(this, QuizActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = WordAdapter { word, view ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("word", word)
            
            val pair = androidx.core.util.Pair.create(view.findViewById<android.view.View>(R.id.itemEnglish), "shared_english")
            val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair)
            startActivity(intent, options.toBundle())
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.displayWords.observe(this) { words ->
            adapter.submitList(words)
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateSearch(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupWordOfTheDay() {
        viewModel.wordOfTheDay.observe(this) { word ->
            word?.let {
                binding.wotdEnglish.text = it.english
                binding.wotdKannada.text = it.kannada
                binding.wotdCard.setOnClickListener {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("word", word)
                    startActivity(intent)
                }
            }
        }
    }
}
