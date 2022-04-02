package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.dao.UserDao
import com.unltm.distance.room.entity.User

class AuthStorage {
    private var userDao: UserDao = MyDatabase.getInstance().getUserDao()

    fun saveAccount(vararg user: User) {
        userDao.insert(*user)
    }

    suspend fun removeAccount(vararg user: User) {
        userDao.delete(*user)
    }

    suspend fun getAllAccount(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun getAccountById(id: String): User? {
        return userDao.getUserById(id)
    }

}