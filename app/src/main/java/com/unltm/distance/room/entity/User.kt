package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey val id: String,
    val phoneNumber: Long? = null,
    val introduce: String = "",
    val avatars: List<String> = emptyList(),
    val lastOnlineAt: Long = 0,
    val email: String,
    val password: String,
    val username: String,
    val isRich: Boolean
) : Serializable {
//    fun toSafeTyped() = run {
//        UserRich(
//            id = if (this.id.isNull) "" else id,
//            phoneNumber,
//            introduce = if (this.introduce.isNull) "" else introduce,
//            avatars = if (this.avatars.isNull) emptyList() else avatars,
//            lastOnlineAt = if (this.lastOnlineAt.isNull) 0 else lastOnlineAt,
//            email = if (this.email.isNull) "" else email,
//            password = if (this.password.isNull) "" else password,
//            username = if (this.username.isNull) "" else username,
//
//            )
//    }

}