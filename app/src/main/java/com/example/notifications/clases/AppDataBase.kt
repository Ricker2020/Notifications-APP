package com.example.notifications.clases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notifications.clases.dao.DAO_Seccion
import com.example.notifications.clases.dao.DAO_Tarea
import com.example.notifications.clases.dao.DAO_User
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.entity.User

@Database(
    //IMPORTANT
    entities=[Seccion::class, Tarea::class, User::class],
    version=2,
    exportSchema = false
)

abstract class AppDataBase: RoomDatabase(){
    abstract val seccionDao: DAO_Seccion
    abstract val tareaDao: DAO_Tarea
    abstract val userDao: DAO_User

    companion object {
        const val DATABASE_NAME="db-notifications"
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}