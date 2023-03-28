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
            database.userDao.insert(User("new","pass"))
        }

        //RecyclerView
        initializeRecyclerView()


        //Crear
        addSeccion()

        //SESSION
        showSession()


    }

    //EXTERNO
    fun showSession(){
        val btnSession=findViewById<ImageView>(R.id.sesion_user)
        btnSession.setOnClickListener {
            val popupMenu = PopupMenu(this, btnSession)
            var optionTitle: String
            var optionAction: Int
            if(Session.email_current=="DEFAULT"){
                optionTitle="Iniciar Sesión"
                optionAction=0
            }else{
                optionTitle="Cerrar Sesión"
                optionAction=1
            }
            popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, optionTitle)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    1 -> {
                        if(optionAction==0) {
                            SessionDialog(
                                onSubmitClickListener = { userCredentials ->
                                    if (notExistUser(userCredentials.email)) {
                                        database.userDao.insert(userCredentials)
                                        Toast.makeText(this, "Nuevo Usuario", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        if (checkUser(userCredentials)) {
                                            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT)
                                                .show()
                                            //LOGIN
                                            Session.email_current = userCredentials.email
                                            Session.password_current = userCredentials.password
                                            initializeRecyclerView()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Contraseña Incorrecta",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ).show(supportFragmentManager, "dialog")
                        }
                        else if(optionAction==1){
                            Session.email_current = "DEFAULT"
                            Session.password_current = "DEFAULT"
                            initializeRecyclerView()
                        }
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
        var secciones = database.seccionDao.getByEmail(Session.email_current)

        if(secciones.isEmpty()){
            database.seccionDao.insert(Seccion(nameseccion = "General", email =Session.email_current ))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 2",email = Session.email_current))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 3",email = Session.email_current))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 4",email = "new"))
            secciones = database.seccionDao.getByEmail(Session.email_current)
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
        }else{
            if(seccions_current.first().email!=Session.email_current){
                return true
            }
        }
        return false
    }


    //EXTERNO
    fun notExistUser(email: String):Boolean{
        var account=database.userDao.get(email)
        if(account.isEmpty()){
            return true
        }
        return false
    }

    fun checkUser(user: User):Boolean{
        var account=database.userDao.get(user.email)
        if(account.first().password==user.password){
            return true
        }
        return false
    }



}