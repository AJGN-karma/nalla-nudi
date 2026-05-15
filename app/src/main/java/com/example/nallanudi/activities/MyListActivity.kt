package com.example.nallanudi.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nallanudi.adapters.WordAdapter
import com.example.nallanudi.databinding.ActivityMyListBinding
import com.example.nallanudi.viewmodel.WordViewModel

class MyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyListBinding
    private val viewModel: WordViewModel by lazy {
        ViewModelProvider(this)[WordViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = WordAdapter { word, view ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("word", word)
            val pair = androidx.core.util.Pair.create(view.findViewById<android.view.View>(com.example.nallanudi.R.id.itemEnglish), "shared_english")
            val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair)
            startActivity(intent, options.toBundle())
        }

        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter

        viewModel.favorites.observe(this) { favorites ->
            if (favorites.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
            adapter.submitList(favorites)
        }
    }
}
