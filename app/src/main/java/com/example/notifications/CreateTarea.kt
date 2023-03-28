package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.tarea.TareaFunctions
import java.text.SimpleDateFormat
import java.util.*

class CreateTarea : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion
    private lateinit var database: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tarea)

        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion
        database=AppDataBase.getInstance(this)

        val fechaEditText=findViewById<EditText>(R.id.create_tarea_fecha)
        val horaEditText=findViewById<EditText>(R.id.create_tarea_hora)
        setInfo(fechaEditText,horaEditText)
        TareaFunctions.getDate(this, fechaEditText)
        TareaFunctions.getTime(this,horaEditText)

        val visibleSwitch=findViewById<Switch>(R.id.create_tarea_bool)
        TareaFunctions.showComponent(this, visibleSwitch,fechaEditText,horaEditText )
        addTarea(visibleSwitch,fechaEditText,horaEditText)

        //SESSION
        val btnSession=findViewById<ImageView>(R.id.sesion_user)
        btnSession.visibility = View.INVISIBLE
    }

    fun setInfo(fechaEditText:EditText, horaEditText:EditText){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        fechaEditText.setText( dateFormat.format(currentDate))
        horaEditText.setText( timeFormat.format(currentDate))
    }

    fun addTarea(visibleEditText: Switch,date:EditText, time:EditText){
        val btnTarea=findViewById<Button>(R.id.btn_add_tarea)
        val descriptionEditText=findViewById<EditText>(R.id.create_tarea_description)

        btnTarea.setOnClickListener {
            database.tareaDao.insert(Tarea(
                idseccion = seccionSelected.idseccion,
                description = descriptionEditText.text.toString(),
                date = TareaFunctions.timeInMillis(this,visibleEditText,date.text.toString(), time.text.toString())))
            Toast.makeText(this, "Añadió una nueva tarea", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}