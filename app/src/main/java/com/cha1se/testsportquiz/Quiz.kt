package com.cha1se.testsportquiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val score: Int
)

@Entity
data class UnblockedWalls(
    @PrimaryKey
    val id: Int,
    val isUnblockedWall: Boolean
)

@Entity
data class SimpleQuiz(
    @PrimaryKey
    val id: Int,
    val questions: String,
    val answers: String
)

@Entity
data class MediumQuiz(
    @PrimaryKey
    val id: Int,
    val questions: String,
    val answers: String
)

@Entity
data class HardQuiz(
    @PrimaryKey
    val id: Int,
    val questions: String,
    val answers: String
)

@Entity
data class VariantsSimple(
    @PrimaryKey
    val id: Int,
    val variant1: String,
    val variant2: String,
    val variant3: String,
    val variant4: String
)

@Entity
data class VariantsMedium(
    @PrimaryKey
    val id: Int,
    val variant1: String,
    val variant2: String,
    val variant3: String,
    val variant4: String
)

@Entity
data class VariantsHard(
    @PrimaryKey
    val id: Int,
    val variant1: String,
    val variant2: String,
    val variant3: String,
    val variant4: String
)