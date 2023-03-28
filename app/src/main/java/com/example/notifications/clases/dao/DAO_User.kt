package com.example.notifications.clases.dao

import androidx.room.*
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.entity.User


@Dao
interface DAO_User {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun get(email: String): List<User>

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

}