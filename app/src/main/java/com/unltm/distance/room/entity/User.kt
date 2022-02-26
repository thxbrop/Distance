package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val username: String
) : Serializable