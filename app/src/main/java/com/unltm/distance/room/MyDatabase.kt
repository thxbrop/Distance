package com.unltm.distance.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unltm.distance.application
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.room.converter.StringListConverter
import com.unltm.distance.room.dao.*
import com.unltm.distance.room.entity.*

@Database(
    entities = [
        User::class,
        UserRich::class,
        Message::class,
        Conversation::class,
        Music::class
    ], version = 1, exportSchema = false
)
@TypeConverters(
    StringListConverter::class
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getUserRichDao(): UserRichDao
    abstract fun getMessageDao(): MessageDao
    abstract fun getConversationDao(): ConversationDao
    abstract fun getMusicDao(): MusicDao

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