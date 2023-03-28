package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.tarea.TareaFunctions
import java.text.SimpleDateFormat
import java.util.*

class EditTarea : AppCompatActivity() {
    private lateinit var tareaSelected: Tarea
    private lateinit var database: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tarea)

        tareaSelected= intent.getSerializableExtra("tareaSelected") as Tarea
        database=AppDataBase.getInstance(this)

        val descriptionEditText=findViewById<EditText>(R.id.create_tarea_description)
        val fechaEditText=findViewById<EditText>(R.id.create_tarea_fecha)
        val horaEditText=findViewById<EditText>(R.id.create_tarea_hora)
        val visibleSwitch=findViewById<Switch>(R.id.create_tarea_bool)

        setInfo(descriptionEditText,fechaEditText,horaEditText,visibleSwitch)
        TareaFunctions.getDate(this, fechaEditText)
        TareaFunctions.getTime(this,horaEditText)

        TareaFunctions.showComponent(this, visibleSwitch,fechaEditText,horaEditText )
        editTarea(descriptionEditText,visibleSwitch,fechaEditText,horaEditText)

        //SESSION
        val btnSession=findViewById<ImageView>(R.id.sesion_user)
        btnSession.visibility = View.INVISIBLE
    }

    fun setInfo(description:EditText,fechaEditText:EditText, horaEditText:EditText,visibleEditText:Switch){

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        val zero:Long=0
        if(tareaSelected.date==zero){
            val currentDate = Date()
            fechaEditText.setText( dateFormat.format(currentDate))
            horaEditText.setText( timeFormat.format(currentDate))
        }
        else{
            val date = Date(tareaSelected.date)
            fechaEditText.setText( dateFormat.format(date))
            horaEditText.setText( timeFormat.format(date))
            visibleEditText.setChecked(true)
        }
        description.setText(tareaSelected.description)

    }

    fun editTarea(descriptionEditText:EditText,visibleEditText: Switch,date:EditText, time:EditText){
        val btnTarea=findViewById<Button>(R.id.btn_add_tarea)

        btnTarea.setOnClickListener {
            tareaSelected.description=descriptionEditText.text.toString()
            tareaSelected.date=TareaFunctions.timeInMillis(this,visibleEditText,date.text.toString(), time.text.toString())

            database.tareaDao.update(tareaSelected)
            Toast.makeText(this, "Tarea Actualizada", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}