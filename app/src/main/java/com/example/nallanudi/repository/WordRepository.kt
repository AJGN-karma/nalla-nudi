package com.example.nallanudi.repository

import android.content.Context
import com.example.nallanudi.dao.WordDao
import com.example.nallanudi.model.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao, private val context: Context) {

    val favorites: Flow<List<Word>> = wordDao.getFavorites()
    val wordOfTheDay: Flow<Word?> = wordDao.getWordOfTheDay()
    val allWords: Flow<List<Word>> = wordDao.getAllWords()
    
    fun getWordsBySubject(subject: String): Flow<List<Word>> {
        return wordDao.getWordsBySubject(subject)
    }

    fun searchWords(query: String): Flow<List<Word>> {
        return wordDao.searchWords(query)
    }

    suspend fun updateFavorite(word: Word) {
        wordDao.updateWord(word)
    }

    suspend fun checkAndPrepopulate() {
        if (wordDao.getCount() == 0) {
            val jsonString = context.assets.open("words.json").bufferedReader().use { it.readText() }
            val wordType = object : TypeToken<List<Word>>() {}.type
            val words: List<Word> = Gson().fromJson(jsonString, wordType)
            wordDao.insertAll(words)
        }
    }
}
