package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.adapter.RcVwAdapterTareas
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea

class ListTareas : AppCompatActivity() {
    private lateinit var seccionSelected: Seccion
    private lateinit var tareaSelected: Tarea
    private lateinit var database: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tareas)

        database = AppDataBase.getInstance(this)

        seccionSelected= intent.getSerializableExtra("seccionSelected") as Seccion


        var tareas = database.tareaDao.get(seccionSelected.id)

        if(tareas.isEmpty()){

            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Tarea 1", date = 123455333))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Tarea 2", date = 123455333))
            database.tareaDao.insert(Tarea(idseccion = seccionSelected.id, description = "Tarea 3", date = 123455333))
            tareas = database.tareaDao.get(seccionSelected.id)
        }

        //RecyclerView
        val rvTareas=findViewById<RecyclerView>(R.id.rv_view_tareas)
        initializeRecyclerView(tareas, rvTareas)
        registerForContextMenu(rvTareas)

    }

    private fun initializeRecyclerView(
        list: List<Tarea>,
        recyclerView: RecyclerView
    ) {
        val manager= LinearLayoutManager(this)
        //val manager= GridLayoutManager(this,2)
        val decoration= DividerItemDecoration(this, manager.orientation)

        val adapter = RcVwAdapterTareas(this, list, {tarea ->onTareaSelected(tarea)})
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = manager
        recyclerView.addItemDecoration(decoration)
        adapter.notifyDataSetChanged()
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