package com.unltm.distance.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unltm.distance.application
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.room.dao.UserDao
import com.unltm.distance.room.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        private const val DB_NAME = "db_name"
        private var INSTANCE: MyDatabase? = null
        fun getInstance() = run {
            if (INSTANCE.isNull) {
                INSTANCE =
                    Room.databaseBuilder(
                        application.applicationContext,
                        MyDatabase::class.java,
                        DB_NAME
                    ).build()
            }
            INSTANCE!!
        }
    }
}