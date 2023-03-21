package com.example.notifications.clases.dao

import androidx.room.*
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea


@Dao
interface DAO_Seccion {
    @Query("SELECT * FROM seccion")
    fun getAll(): List<Seccion>

    @Query("SELECT * FROM seccion WHERE nameseccion = :name")
    fun get(name: String): List<Seccion>

    @Insert
    fun insert(secciones: Seccion)

    @Update
    fun update(seccion: Seccion)

    @Delete
    fun delete(seccion: Seccion)

}