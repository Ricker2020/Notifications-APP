package com.example.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.clases.AppDataBase
import com.example.notifications.clases.adapter.RcVwAdapterSeccion
import com.example.notifications.clases.component.SeccionDialog
import com.example.notifications.clases.entity.Seccion

class MainActivity : AppCompatActivity() {
    private lateinit var selectedSeccion: Seccion
    private lateinit var database: AppDataBase
    private lateinit var adapterSeccion: RcVwAdapterSeccion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database=AppDataBase.getInstance(this)

        //RecyclerView
        initializeRecyclerView()


        //Crear
        addSeccion()

    }

    private fun initializeRecyclerView() {
        var recyclerView=findViewById<RecyclerView>(R.id.rv_view_seccions)
        var secciones = database.seccionDao.getAll()

        if(secciones.isEmpty()){
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 1"))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 2"))
            database.seccionDao.insert(Seccion(nameseccion = "Elementos 3"))
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
            R.id.menu_rv_seccion_mover ->{
                return true
            }
            R.id.menu_rv_seccion_eliminar ->{
                database.seccionDao.delete(selectedSeccion)
                initializeRecyclerView()
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
        val btnSeccion=findViewById<Button>(R.id.btn_seccion)
        btnSeccion.setOnClickListener {
            SeccionDialog(
                onSubmitClickListener = { nameseccion ->
                    if(notExist(nameseccion)){
                        database.seccionDao.insert(Seccion(nameseccion = nameseccion))
                        initializeRecyclerView()
                        Toast.makeText(this, "Añadió $nameseccion", Toast.LENGTH_SHORT).show()
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