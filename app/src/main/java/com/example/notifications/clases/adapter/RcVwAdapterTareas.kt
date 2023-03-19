package com.example.notifications.clases.adapter

import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.ListTareas
import com.example.notifications.R
import com.example.notifications.clases.entity.Tarea

class RcVwAdapterTareas(
    private val parentContext: ListTareas,
    private val list: List<Tarea>,
    private val onClickListener: (tarea: Tarea) -> Unit
): RecyclerView.Adapter<RcVwAdapterTareas.MyViewHolder>() {
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        val descriptionTextView: TextView
        val dateTextView: TextView

        init {
            descriptionTextView = view.findViewById(R.id.rv_tarea_description)
            dateTextView = view.findViewById(R.id.rv_tarea_fecha)

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
                inflater.inflate(R.menu.menu_rv_tarea, menu)
                parentContext.setSelectedTareaId(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.rv_tarea,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tarea = this.list[position]
        holder.descriptionTextView.text = tarea.description
        holder.dateTextView.text = tarea.date.toString()
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}