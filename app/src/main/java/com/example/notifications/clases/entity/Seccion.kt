package com.example.notifications.clases.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "seccion")
data class Seccion (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idseccion")
    var id: Int=0,

    @ColumnInfo(name = "nameseccion")
    var nameseccion: String
): Serializable
{
    override fun toString(): String {
        return "${nameseccion}"
    }
}