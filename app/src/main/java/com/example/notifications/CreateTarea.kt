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

        getDate()
        getTime()

        showComponent()

        addTarea()

    }

    fun addTarea(){

        val btnTarea=findViewById<Button>(R.id.btn_add_tarea)
        val descriptionEditText=findViewById<EditText>(R.id.create_tarea_description)
        btnTarea.setOnClickListener {
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = descriptionEditText.text.toString(), date = timeInMillis()))
            Toast.makeText(this, "Añadió una nueva tarea", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
    fun showComponent(){
        val visibleEditText = findViewById<Switch>(R.id.create_tarea_bool)
        val fechaEditText = findViewById<EditText>(R.id.create_tarea_fecha)
        val horaEditText = findViewById<EditText>(R.id.create_tarea_hora)


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

    fun timeInMillis():Long{
        var timeMillis:Long=0
        val visibleEditText = findViewById<Switch>(R.id.create_tarea_bool)
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
                this.hourSelected =selectedTime
                time.setText(selectedTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }
    }
}