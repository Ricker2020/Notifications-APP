package com.example.notifications.clases.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User (
    @PrimaryKey
    var email: String,
    var password: String
): Serializable
{
    override fun toString(): String {
        return "${email}"
    }
}