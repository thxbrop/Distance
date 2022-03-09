package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val username: String
) : Serializable {

    fun asRich() = run {
        UserRich(
            id = this.id,
            email = this.email,
            username = this.username,
            password = ""
        )
    }
}