package com.example.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea
import java.text.SimpleDateFormat
import java.util.*

class CreateTarea : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion
    private lateinit var database: AppDataBase
    private lateinit var dateSelected: String
    private lateinit var timeSelected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tarea)

        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion
        database=AppDataBase.getInstance(this)

        getDate()
        getTime()

        addTarea()

    }

    fun addTarea(){
        val btnTarea=findViewById<Button>(R.id.btn_add_tarea)
        val descriptionEditText=findViewById<EditText>(R.id.create_tarea_description)
        btnTarea.setOnClickListener {
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = descriptionEditText.text.toString(), date = timeInMillis()))
            Toast.makeText(this, "Añadió una nueva tarea", Toast.LENGTH_SHORT).show()
        }


    }

    fun timeInMillis(): Long{
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoTiempo = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val fechaCompleta = formatoFecha.parse(dateSelected)!!
        val tiempoCompleto = formatoTiempo.parse(timeSelected)!!
        val calendar = Calendar.getInstance()
        calendar.time = fechaCompleta
        val hora = tiempoCompleto.hours
        val minuto = tiempoCompleto.minutes
        val segundo = tiempoCompleto.seconds
        calendar.set(Calendar.HOUR_OF_DAY, hora)
        calendar.set(Calendar.MINUTE, minuto)
        calendar.set(Calendar.SECOND, segundo)
        return calendar.timeInMillis
    }

    fun getDate(){
        val date=findViewById<EditText>(R.id.create_tarea_fecha)
        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, yearSelected, monthSelected, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthSelected+1}/$yearSelected"
                dateSelected=selectedDate
                date.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

    }

    fun getTime(){
        val time=findViewById<EditText>(R.id.create_tarea_hora)
        time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourSelected, minuteSelected ->
                val selectedTime = "$hourSelected:$minuteSelected:00"
                timeSelected=selectedTime
                time.setText(selectedTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }
    }
}