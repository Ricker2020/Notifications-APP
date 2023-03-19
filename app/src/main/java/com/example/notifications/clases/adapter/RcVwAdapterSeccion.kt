package com.example.notifications.clases.adapter

import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.MainActivity
import com.example.notifications.R
import com.example.notifications.clases.entity.Seccion

class RcVwAdapterSeccion(
    private val parentContext: MainActivity,
    private val list: List<Seccion>,
    private val onClickListener: (seccion: Seccion) -> Unit
): RecyclerView.Adapter<RcVwAdapterSeccion.MyViewHolder>() {
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        val nameTextView: TextView

        init {
            nameTextView = view.findViewById(R.id.rv_seccion_name)
            view.setOnCreateContextMenuListener(this)
            itemView.isClickable = true
            itemView.isLongClickable = true

            val typedValue = TypedValue()
            itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            itemView.setBackgroundResource(typedValue.resourceId)

            itemView.setOnClickListener {
                onClickListener(list[adapterPosition])
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                val inflater = MenuInflater(view?.context)
                inflater.inflate(R.menu.menu_rv_seccion, menu)
                parentContext.setSelectedSeccionId(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.rv_seccion,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val seccion = this.list[position]
        holder.nameTextView.text = seccion.nameseccion
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}