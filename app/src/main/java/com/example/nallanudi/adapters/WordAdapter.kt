package com.example.nallanudi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nallanudi.R
import com.example.nallanudi.model.Word

class WordAdapter(private val onClick: (Word, View) -> Unit) :
    ListAdapter<Word, WordAdapter.WordViewHolder>(WordDiffCallback()) {

    class WordViewHolder(itemView: View, val onClick: (Word, View) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvEnglish: TextView = itemView.findViewById(R.id.itemEnglish)
        private val tvKannada: TextView = itemView.findViewById(R.id.itemKannada)
        private var currentWord: Word? = null

        init {
            itemView.setOnClickListener {
                currentWord?.let { onClick(it, itemView) }
            }
        }

        fun bind(word: Word) {
            currentWord = word
            tvEnglish.text = word.english
            tvKannada.text = word.kannada
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = getItem(position)
        holder.bind(word)
        
        // Simple scale-in animation
        holder.itemView.alpha = 0f
        holder.itemView.scaleX = 0.95f
        holder.itemView.scaleY = 0.95f
        holder.itemView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setStartDelay(position.toLong() * 30) // Staggered entrance
            .start()
    }

    class WordDiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.english == newItem.english
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }
}
