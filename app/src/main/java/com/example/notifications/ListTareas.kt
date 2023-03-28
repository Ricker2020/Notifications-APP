package com.example.notifications

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.adapter.RcVwAdapterTareas
import com.example.notifications.clases.component.SessionDialog
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.entity.User
import com.example.notifications.clases.session.Session

class ListTareas : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion
    private lateinit var tareaSelected: Tarea
    private lateinit var database: AppDataBase
    private lateinit var adapterTareas: RcVwAdapterTareas
    var descendant=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tareas)

        database = AppDataBase.getInstance(this)
        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion

        //RecyclerView
        initializeRecyclerView()

        //ADD Tarea
        val addTarea=findViewById<ImageView>(R.id.iv_add_tarea)
        if(seccionSelected.nameseccion=="General"){
            addTarea.visibility = View.INVISIBLE
        }
        addTarea.setOnClickListener {
            startActivity(Intent(this, CreateTarea::class.java).apply {
                putExtra("seccionSelected",seccionSelected)
            })
        }

        //DESCENDANT
        sortIcon()

        //SESSION
        val btnSession=findViewById<ImageView>(R.id.sesion_user)
        btnSession.visibility = View.INVISIBLE
    }
    override fun onResume() {
        super.onResume()
        initializeRecyclerView()
    }

    private fun listGeneral(): List<Tarea>{
        var secciones = database.seccionDao.getByEmail(Session.email_current)
        var tareasList = mutableListOf<Tarea>()
        for (seccion in secciones){
            tareasList.addAll(database.tareaDao.get(seccion.idseccion))
        }
        return tareasList
    }

    private fun initializeRecyclerView(){
        val recyclerView=findViewById<RecyclerView>(R.id.rv_view_tareas)
        var tareas = database.tareaDao.get(seccionSelected.idseccion)

        /*if(tareas.isEmpty()){
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.idseccion, description = "Tarea 1", date = 1679442609093))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.idseccion, description = "Tarea 2", date = 1679451609093))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.idseccion, description = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.", date = 123455333))
            tareas = database.tareaDao.get(seccionSelected.idseccion)
        }*/

        val manager= LinearLayoutManager(this)
        //val manager= GridLayoutManager(this,2)
        val decoration= DividerItemDecoration(this, manager.orientation)

        if(seccionSelected.nameseccion=="General"){
            //tareas=database.tareaDao.getAll()
            tareas=listGeneral()
        }

        tareas=sortList(descendant,tareas)

        adapterTareas = RcVwAdapterTareas(this, tareas, {tarea ->onTareaSelected(tarea)})
        recyclerView.adapter = adapterTareas
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = manager
        recyclerView.addItemDecoration(decoration)
        adapterTareas.notifyDataSetChanged()

        registerForContextMenu(recyclerView)
    }

    fun sortIcon(){
        //val sortImageView=findViewById<ImageView>(R.id.iv_sort_tarea)
        val sortUpImageView=findViewById<ImageView>(R.id.iv_sort_up)
        val sortDownImageView=findViewById<ImageView>(R.id.iv_sort_down)

        sortUpImageView.setImageResource(R.drawable.arrowupselectedrm)
        sortDownImageView.setImageResource(R.drawable.arrowdownrm)

        val sortImageView=findViewById<LinearLayout>(R.id.image_sort_tarea)
        sortImageView.setOnClickListener {
            descendant=!descendant
            if(descendant){
                sortUpImageView.setImageResource(R.drawable.arrowupselectedrm)
                sortDownImageView.setImageResource(R.drawable.arrowdownrm)
            }else{
                sortUpImageView.setImageResource(R.drawable.arrowuprm)
                sortDownImageView.setImageResource(R.drawable.arrowdownselectedrm)
            }
            initializeRecyclerView()
        }
    }
    fun sortList(opcion:Boolean, list: List<Tarea>):List<Tarea>{
        if(opcion){
            return list.sortedByDescending { tarea -> tarea.date }
        }else{
            return list.sortedBy { tarea -> tarea.date }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_rv_tarea_editar -> {
                startActivity(Intent(this, EditTarea::class.java).apply {
                    putExtra("tareaSelected",tareaSelected)
                })
                return true
            }
            R.id.menu_rv_tarea_eliminar ->{
                database.tareaDao.delete(tareaSelected)
                initializeRecyclerView()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    fun setSelectedTareaId(tarea: Tarea) {
        tareaSelected = tarea
    }


    fun onTareaSelected(tarea: Tarea) {
        startActivity(Intent(this, EditTarea::class.java).apply {
            putExtra("tareaSelected",tarea)
        })
    }


}