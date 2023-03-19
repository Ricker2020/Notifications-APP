package com.example.notifications.data.dao

import androidx.room.*
import com.example.notifications.data.entity.Seccion


@Dao
interface DAO_Seccion {
    @Query("SELECT * FROM seccion")
    fun getAll(): List<Seccion>

    @Insert
    fun insert(secciones: Seccion)

    @Update
    fun update(seccion: Seccion)

    @Delete
    fun delete(seccion: Seccion)

}