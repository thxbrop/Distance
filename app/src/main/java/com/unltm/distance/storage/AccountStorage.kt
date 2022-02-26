package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.dao.UserRichDao
import com.unltm.distance.room.entity.UserRich

class AccountStorage {
    private var userRichDao: UserRichDao = MyDatabase.getInstance().getUserRichDao()

    fun saveAccount(vararg user: UserRich) {
        userRichDao.insert(*user)
    }

    suspend fun removeAccount(vararg user: UserRich) {
        userRichDao.delete(*user)
    }

    suspend fun getAllAccount(): List<UserRich> {
        return userRichDao.getAllUserRich()
    }

    suspend fun getAccountById(id: String): UserRich? {
        return userRichDao.getUserRichById(id)
    }

}