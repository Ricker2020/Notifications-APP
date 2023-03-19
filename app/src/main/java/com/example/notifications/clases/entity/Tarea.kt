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
    @ColumnInfo(name = "idtarea")
    var idtarea: Int = 0,
    @ColumnInfo(name = "idseccion")
    var idseccion: Int,

    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "date")
    var date: Int

): Serializable {
    override fun toString(): String {
        return "${description}"
    }
}