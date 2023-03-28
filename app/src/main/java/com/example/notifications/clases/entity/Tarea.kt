package com.example.notifications.clases.entity

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "tarea",
    foreignKeys = [
        ForeignKey(
            entity = Seccion::class,
            parentColumns = ["idseccion"],
            childColumns = ["idseccion"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idseccion")]
)
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    var idtarea: Int = 0,

    var idseccion: Int,
    var description: String,
    var date: Long

): Serializable {
    override fun toString(): String {
        return "${description}"
    }
}