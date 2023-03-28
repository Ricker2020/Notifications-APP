package com.example.notifications.clases.entity

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "seccion",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("email")]
)
data class Seccion (
    @PrimaryKey(autoGenerate = true)
    var idseccion: Int=0,

    var email:String,
    var nameseccion: String

): Serializable
{
    override fun toString(): String {
        return "${nameseccion}"
    }
}