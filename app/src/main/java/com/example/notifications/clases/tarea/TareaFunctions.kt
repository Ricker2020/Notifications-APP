package com.example.notifications.clases.tarea

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import com.example.notifications.R
import java.text.SimpleDateFormat
import java.util.*


class TareaFunctions {
    companion object {
        fun showComponent(context: Context, visibleEditText: Switch, fechaEditText:EditText, horaEditText: EditText){
            val isChecked = visibleEditText.isChecked
            if (isChecked) {
                fechaEditText.visibility = View.VISIBLE
                horaEditText.visibility = View.VISIBLE
            } else {
                fechaEditText.visibility = View.GONE
                horaEditText.visibility = View.GONE
            }

            visibleEditText.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    fechaEditText.visibility = View.VISIBLE
                    horaEditText.visibility = View.VISIBLE
                } else {
                    fechaEditText.visibility = View.GONE
                    horaEditText.visibility = View.GONE
                }
            }
        }


        fun timeInMillis(context: Context,visibleEditText:Switch,dateSelected: String, hourSelected: String ):Long{
            var timeMillis:Long=0
            //val visibleEditText = findViewById<Switch>(R.id.create_tarea_bool)
            val isChecked = visibleEditText.isChecked
            if (isChecked) {
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formatoTiempo = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val fechaCompleta = formatoFecha.parse(dateSelected)!!
                val tiempoCompleto = formatoTiempo.parse(hourSelected)!!
                val calendar = Calendar.getInstance()
                calendar.time = fechaCompleta
                val hora = tiempoCompleto.hours
                val minuto = tiempoCompleto.minutes
                val segundo = tiempoCompleto.seconds
                calendar.set(Calendar.HOUR_OF_DAY, hora)
                calendar.set(Calendar.MINUTE, minuto)
                calendar.set(Calendar.SECOND, segundo)
                timeMillis=calendar.timeInMillis
            } else {
                timeMillis=0
            }
            return timeMillis
        }

        fun getDate(context: Context, date:EditText ){
            date.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(context, { _, yearSelected, monthSelected, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthSelected+1}/$yearSelected"
                    date.setText(selectedDate)
                }, year, month, day)
                datePickerDialog.show()
            }
        }

        fun getTime(context: Context, time:EditText ){
            time.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(context, { _, hourSelected, minuteSelected ->
                    val selectedTime = "$hourSelected:$minuteSelected:00"
                    time.setText(selectedTime)
                }, hour, minute, true)
                timePickerDialog.show()
            }
        }





    }
}