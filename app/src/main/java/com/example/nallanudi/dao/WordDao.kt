package com.example.nallanudi.dao

import androidx.room.*
import com.example.nallanudi.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words WHERE english LIKE :query || '%' OR kannada LIKE :query || '%' ORDER BY english ASC")
    fun searchWords(query: String): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Word>>

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    fun getWordOfTheDay(): Flow<Word?>

    @Query("SELECT * FROM words WHERE subject = :subject")
    fun getWordsBySubject(subject: String): Flow<List<Word>>

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<Word>>

    @Update
    suspend fun updateWord(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Word>)

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getCount(): Int
}
