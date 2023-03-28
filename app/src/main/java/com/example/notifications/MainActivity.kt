package com.example.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.adapter.RcVwAdapterSeccion
import com.example.notifications.clases.component.SeccionDialog
import com.example.notifications.clases.component.SessionDialog
import com.example.notifications.clases.component.TareaNotification
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.User
import com.example.notifications.clases.session.Session
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var selectedSeccion: Seccion
    private lateinit var database: AppDataBase
    private lateinit var adapterSeccion: RcVwAdapterSeccion

/*    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database=AppDataBase.getInstance(this)

        //USER DEFAULT
        if(database.userDao.getAll().isEmpty()){
            database.userDao.insert(User(Session.email_current,Session.password_current))
        }

        //RecyclerView
        initializeRecyclerView()


        //Crear
        addSeccion()

        //SESSION
        showSession()


    }

    fun showSession(){
        val btnSession=findViewById<ImageView>(R.id.sesion_user)
        btnSession.setOnClickListener {
            val popupMenu = PopupMenu(this, btnSession)

            popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Iniciar Sesión")
            popupMenu.menu.add(Menu.NONE, 2, Menu.NONE, "Cerrar Sesión")

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    1 -> {
                        //Session.email_current="NEW USER"
                        //Toast.makeText(this, "Seleccionado: ${Session.email_current}", Toast.LENGTH_SHORT).show()
                        //Session.email_current="DEFAULT"


                            SessionDialog(
                                onSubmitClickListener = { userCredentials ->
                                    /*if(notExist(nameseccion)){
                                        database.seccionDao.insert(Seccion(nameseccion = nameseccion, email = Session.email_current))
                                        initializeRecyclerView()
                                        Toast.makeText(this, "Añadió $nameseccion", Toast.LENGTH_SHORT).show()
                                        //scheduleNotification()
                                    }else{
                                        Toast.makeText(this, "Sección Existente", Toast.LENGTH_SHORT).show()
                                    }*/
                                    Toast.makeText(this, "${userCredentials.email} && ${userCredentials.password}", Toast.LENGTH_SHORT).show()
                                }
                            ).show(supportFragmentManager, "dialog")

                        true
                    }
                    2 -> {
                        // Acción para la opción 2
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()

        }
    }



    private fun initializeRecyclerView() {
        var recyclerView=findViewById<RecyclerView>(R.id.rv_view_seccions)
        var secciones = database.seccionDao.getAll()

        if(secciones.isEmpty()){
            database.seccionDao.insert(Seccion(nameseccion = "General", email =Session.email_current ))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 2",email = Session.email_current))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 3",email = Session.email_current))
            secciones = database.seccionDao.getAll()
        }

        val manager= LinearLayoutManager(this)
        //val manager= GridLayoutManager(this,2)
        val decoration= DividerItemDecoration(this, manager.orientation)

        adapterSeccion = RcVwAdapterSeccion(this, secciones, {seccion ->onSeccionSelected(seccion)})
        recyclerView.adapter = adapterSeccion
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = manager
        recyclerView.addItemDecoration(decoration)
        adapterSeccion.notifyDataSetChanged()

        registerForContextMenu(recyclerView)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_rv_seccion_ver -> {
                startActivity(Intent(this, ListTareas::class.java).apply {
                    putExtra("seccionSelected",selectedSeccion)
                })
                return true
            }
            R.id.menu_rv_seccion_eliminar ->{
                if(selectedSeccion.nameseccion=="General"){
                    Toast.makeText(this, "No puedes eliminar esta Sección", Toast.LENGTH_SHORT).show()
                }else{
                    database.seccionDao.delete(selectedSeccion)
                    initializeRecyclerView()
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    //Formas de obtener un elemento

    //1ERA: al mantener pulsado y luego soltar
    fun setSelectedSeccionId(seccion: Seccion) {
        selectedSeccion = seccion
        //Toast.makeText(this, "Seleccionado: ${seccion.id}", Toast.LENGTH_SHORT).show()
    }

    //2DA: al hacer clic
    fun onSeccionSelected(seccion: Seccion) {
        //Toast.makeText(this, "Seleccionado: ${seccion.id}", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ListTareas::class.java).apply {
            putExtra("seccionSelected",seccion)
        })
    }

    fun addSeccion(){
        val btnSeccion=findViewById<ImageView>(R.id.iv_add_seccion)
        btnSeccion.setOnClickListener {
            SeccionDialog(
                onSubmitClickListener = { nameseccion ->
                    if(notExist(nameseccion)){
                        database.seccionDao.insert(Seccion(nameseccion = nameseccion, email = Session.email_current))
                        initializeRecyclerView()
                        Toast.makeText(this, "Añadió $nameseccion", Toast.LENGTH_SHORT).show()
                        //scheduleNotification()
                    }else{
                        Toast.makeText(this, "Sección Existente", Toast.LENGTH_SHORT).show()
                    }
                }
            ).show(supportFragmentManager, "dialog")
        }
    }

    fun notExist(name: String):Boolean{
        var seccions_current=database.seccionDao.get(name.trim())
        if(seccions_current.isEmpty()){
            return true
        }
        return false
    }



}