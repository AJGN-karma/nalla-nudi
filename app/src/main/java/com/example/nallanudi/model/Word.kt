package com.example.nallanudi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "words")
data class Word(
    @PrimaryKey
    val english: String,
    val kannada: String,
    val explanation: String,
    val subject: String,
    var isFavorite: Boolean = false
) : Serializable
