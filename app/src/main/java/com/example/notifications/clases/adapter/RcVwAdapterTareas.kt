package com.example.notifications.clases.adapter

import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.ListTareas
import com.example.notifications.R
import com.example.notifications.clases.entity.Tarea
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RcVwAdapterTareas(
    private val parentContext: ListTareas,
    private val list: List<Tarea>,
    private val onClickListener: (tarea: Tarea) -> Unit
): RecyclerView.Adapter<RcVwAdapterTareas.MyViewHolder>() {
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        val descriptionTextView: TextView
        val dateTextView: TextView
        val hourTextView: TextView
        val timerTextView: TextView

        init {
            descriptionTextView = view.findViewById(R.id.rv_tarea_description)
            dateTextView = view.findViewById(R.id.rv_tarea_fecha)
            hourTextView = view.findViewById(R.id.rv_tarea_hora)
            timerTextView = view.findViewById(R.id.rv_tarea_temporizador)

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

        val date = Date(tarea.date)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val currentDate = Date()
        val timeDiff = tarea.date - currentDate.time
        val timerResult = if (timeDiff < 0) {
            "00:00:00"
        } else {
            val hours = TimeUnit.MILLISECONDS.toHours(timeDiff)
            val mins = TimeUnit.MILLISECONDS.toMinutes(timeDiff - TimeUnit.HOURS.toMillis(hours))
            val secs = TimeUnit.MILLISECONDS.toSeconds(timeDiff - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(mins))
            String.format("%02d:%02d:%02d", hours, mins, secs)
        }

        holder.descriptionTextView.text = tarea.description
        holder.dateTextView.text = dateFormat.format(date)
        holder.hourTextView.text = timeFormat.format(date)
        holder.timerTextView.text= timerResult

    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}