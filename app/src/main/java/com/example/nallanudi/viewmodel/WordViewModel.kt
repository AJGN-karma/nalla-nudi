package com.example.nallanudi.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.nallanudi.database.AppDatabase
import com.example.nallanudi.model.Word
import com.example.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    val searchQuery = MutableStateFlow("")
    val subjectFilter = MutableStateFlow<String?>(null)

    val displayWords: LiveData<List<Word>>
    val allWords: LiveData<List<Word>>
    val favorites: LiveData<List<Word>>
    val wordOfTheDay: LiveData<Word?>

    init {
        val wordDao = AppDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao, application)
        
        displayWords = kotlinx.coroutines.flow.combine(
            repository.allWords,
            searchQuery,
            subjectFilter
        ) { allWords, query, subject ->
            allWords.filter { word ->
                val matchesQuery = query.isEmpty() || 
                    word.english.contains(query, ignoreCase = true) || 
                    word.kannada.contains(query, ignoreCase = true)
                val matchesSubject = subject == null || subject == "All" || word.subject == subject
                matchesQuery && matchesSubject
            }
        }.asLiveData()

        allWords = repository.allWords.asLiveData()
        favorites = repository.favorites.asLiveData()
        wordOfTheDay = repository.wordOfTheDay.asLiveData()

        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    fun toggleFavorite(word: Word) {
        viewModelScope.launch {
            word.isFavorite = !word.isFavorite
            repository.updateFavorite(word)
        }
    }

    fun updateSearch(query: String) {
        searchQuery.value = query
    }
}
