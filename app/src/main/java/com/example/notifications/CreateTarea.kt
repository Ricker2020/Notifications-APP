package com.example.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
    private lateinit var dateSelected: String
    private lateinit var hourSelected: String
    //private var timeMillis: Long=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tarea)

        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion
        database=AppDataBase.getInstance(this)

        val date=findViewById<EditText>(R.id.create_tarea_fecha)
        val time=findViewById<EditText>(R.id.create_tarea_hora)
        TareaFunctions.getDate(this, date)
        TareaFunctions.getTime(this,time)

        val visibleEditText=findViewById<Switch>(R.id.create_tarea_bool)
        val fechaEditText=findViewById<EditText>(R.id.create_tarea_fecha)
        val horaEditText=findViewById<EditText>(R.id.create_tarea_hora)

        TareaFunctions.showComponent(this, visibleEditText,fechaEditText,horaEditText )

        addTarea()

    }

    fun addTarea(){
        val btnTarea=findViewById<Button>(R.id.btn_add_tarea)
        val descriptionEditText=findViewById<EditText>(R.id.create_tarea_description)
        val visibleEditText = findViewById<Switch>(R.id.create_tarea_bool)

        val date=findViewById<EditText>(R.id.create_tarea_fecha)
        val time=findViewById<EditText>(R.id.create_tarea_hora)

        btnTarea.setOnClickListener {


            database.tareaDao.insert(Tarea(
                idseccion = seccionSelected.id,
                description = descriptionEditText.text.toString(),
                date = TareaFunctions.timeInMillis(this,visibleEditText,date.text.toString(), time.text.toString())))
            Toast.makeText(this, "Añadió una nueva tarea", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}