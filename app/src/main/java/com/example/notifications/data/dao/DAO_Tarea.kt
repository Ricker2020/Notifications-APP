package com.example.notifications.data.dao

import androidx.room.*
import com.example.notifications.data.entity.Tarea


@Dao
interface DAO_Tarea {
    @Query("SELECT * FROM tarea")
    fun getAll(): List<Tarea>

    @Query("SELECT * FROM tarea WHERE idseccion = :idseccion")
    fun get(idseccion: Int): List<Tarea>

    @Insert
    fun insert(tarea: Tarea)

    @Update
    fun update(tarea: Tarea)

    @Delete
    fun delete(tarea: Tarea)

}