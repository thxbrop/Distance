package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.dao.UserDao
import com.unltm.distance.room.entity.User

class AccountStorage {
    private var userRichDao: UserDao = MyDatabase.getInstance().getUserDao()

    fun saveAccount(vararg user: User) {
        userRichDao.insert(*user)
    }

    suspend fun removeAccount(vararg user: User) {
        userRichDao.delete(*user)
    }

    suspend fun getAllAccount(): List<User> {
        return userRichDao.getAllUser()
    }

    suspend fun getAccountById(id: String): User? {
        return userRichDao.getUserById(id)
    }

}