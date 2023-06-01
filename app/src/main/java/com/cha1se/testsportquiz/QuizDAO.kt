package com.cha1se.testsportquiz

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuizDAO {

    @Query("SELECT * FROM Quiz")
    fun getQuiz(): List<Quiz>

    @Query("SELECT * FROM UnblockedWalls")
    fun getUnblockedWalls(): List<UnblockedWalls>

    @Query("SELECT * FROM VariantsSimple WHERE id=:id")
    fun getVariantsSimpleById(id: Int): List<VariantsSimple>

    @Query("SELECT * FROM VariantsMedium WHERE id=:id")
    fun getVariantsMediumById(id: Int): List<VariantsMedium>

    @Query("SELECT * FROM VariantsHard WHERE id=:id")
    fun getVariantsHardById(id: Int): List<VariantsHard>

    @Query("SELECT * FROM SimpleQuiz WHERE id=:id")
    fun getSimpleQuizById(id: Int): List<SimpleQuiz>

    @Query("SELECT * FROM MediumQuiz WHERE id=:id")
    fun getMediumQuizById(id: Int): List<MediumQuiz>

    @Query("SELECT * FROM HardQuiz WHERE id=:id")
    fun getHardQuizById(id: Int): List<HardQuiz>

    @Query("SELECT * FROM UnblockedWalls WHERE id=:id")
    fun getUnblockedWallsById(id: Int): List<UnblockedWalls>


    @Insert
    fun insertQuiz(quiz: Quiz)

    @Insert
    fun insertSimpleQuiz(simpleQuiz: SimpleQuiz)

    @Insert
    fun insertMediumQuiz(mediumQuiz: MediumQuiz)

    @Insert
    fun insertHardQuiz(hardQuiz: HardQuiz)

    @Insert
    fun insertVariantsSimple(variants: VariantsSimple)

    @Insert
    fun insertVariantsMedium(variants: VariantsMedium)

    @Insert
    fun insertVariantsHard(variants: VariantsHard)

    @Insert
    fun insertUnblockedWalls(walls: UnblockedWalls)


    @Update
    fun updateQuiz(quiz: Quiz)

    @Update
    fun updateSimpleQuiz(simpleQuiz: SimpleQuiz)

    @Update
    fun updateMediumQuiz(mediumQuiz: MediumQuiz)

    @Update
    fun updateHardQuiz(hardQuiz: HardQuiz)

    @Update
    fun updateUnblockedWalls(walls: UnblockedWalls)

}