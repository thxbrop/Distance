package com.unltm.distance.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.unltm.distance.room.entity.UserRich

@Dao
interface UserRichDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg user: UserRich)

    @Delete
    suspend fun delete(vararg user: UserRich)

    @Query("SELECT * FROM UserRich ORDER BY id")
    suspend fun getAllUserRich(): List<UserRich>

    @Query("SELECT * FROM UserRich WHERE id = :id")
    suspend fun getUserRichById(id: String): UserRich?
}