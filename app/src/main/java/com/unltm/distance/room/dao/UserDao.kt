package com.unltm.distance.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.unltm.distance.room.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg user: User)

    @Delete
    suspend fun delete(vararg user: User)

    @Query("SELECT * FROM User ORDER BY id")
    suspend fun getAllUser(): List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: String): User?
}