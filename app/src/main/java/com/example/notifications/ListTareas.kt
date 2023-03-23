package com.example.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.adapter.RcVwAdapterSeccion
import com.example.notifications.clases.adapter.RcVwAdapterTareas
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea

class ListTareas : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion
    private lateinit var tareaSelected: Tarea
    private lateinit var database: AppDataBase
    private lateinit var adapterTareas: RcVwAdapterTareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tareas)

        database = AppDataBase.getInstance(this)
        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion

        //RecyclerView
        initializeRecyclerView()

        //ADD Tarea
        val addTarea=findViewById<ImageView>(R.id.iv_add_tarea)
        addTarea.setOnClickListener {
            startActivity(Intent(this, CreateTarea::class.java).apply {
                putExtra("seccionSelected",seccionSelected)
            })
        }

    }

    private fun initializeRecyclerView(){
        val recyclerView=findViewById<RecyclerView>(R.id.rv_view_tareas)
        var tareas = database.tareaDao.get(seccionSelected.id)

        if(tareas.isEmpty()){
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Tarea 1", date = 1679442609093))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Tarea 2", date = 1679451609093))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.", date = 123455333))
            tareas = database.tareaDao.get(seccionSelected.id)
        }

        val manager= LinearLayoutManager(this)
        //val manager= GridLayoutManager(this,2)
        val decoration= DividerItemDecoration(this, manager.orientation)

        adapterTareas = RcVwAdapterTareas(this, tareas, {tarea ->onTareaSelected(tarea)})
        recyclerView.adapter = adapterTareas
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = manager
        recyclerView.addItemDecoration(decoration)
        adapterTareas.notifyDataSetChanged()

        registerForContextMenu(recyclerView)
    }




    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_rv_tarea_editar -> {
                /*startActivity(Intent(this, ViewInstrument::class.java).apply {
                    putExtra("instrumentSelected",selectedInstrument)
                })*/
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


    //Formas de obtener un elemento

    //1ERA: al mantener pulsado y luego soltar
    fun setSelectedTareaId(tarea: Tarea) {
        tareaSelected = tarea
        Toast.makeText(this, "Seleccionado: ${tarea.idtarea}", Toast.LENGTH_SHORT).show()
    }

    //2DA: al hacer clic
    fun onTareaSelected(tarea: Tarea) {
        Toast.makeText(this, "Seleccionado: ${tarea.idtarea}", Toast.LENGTH_SHORT).show()
        /*startActivity(Intent(this, ListTareas::class.java).apply {
            putExtra("seccionSelected",tarea)
        })*/
    }
}