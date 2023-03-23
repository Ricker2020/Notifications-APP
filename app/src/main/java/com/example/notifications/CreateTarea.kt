package com.example.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.example.notifications.clases.entity.Seccion
import java.util.Calendar

class CreateTarea : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tarea)

        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion

        getDate()
        getTime()
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
                val selectedTime = "$hourSelected:$minuteSelected"
                time.setText(selectedTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }
    }
}