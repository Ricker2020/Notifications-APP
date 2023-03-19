package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.notifications.data.AppDataBase
import com.example.notifications.data.entity.Seccion
import com.example.notifications.data.entity.Tarea

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDataBase.getInstance(this)

        var secciones = database.tareaDao.getAll().toMutableList()

        if(secciones.isEmpty()){

            database.seccionDao.insert(Seccion(nameseccion = "Elementos enviados"))
            database.tareaDao.insert(Tarea(idseccion = 1,description="Acabar este proyecto", date = 170093782))
            database.tareaDao.insert(Tarea(idseccion = 1,description="Simon", date = 170093782))

            secciones = database.tareaDao.getAll().toMutableList()
        }
        var text_list=""
        for(seccion in secciones){
            text_list+=seccion.description
        }
        val text_view=findViewById<TextView>(R.id.text_view)
        text_view.text=text_list




    }
}