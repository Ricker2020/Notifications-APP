package com.example.notifications.clases.adapter

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.notifications.ListTareas
import com.example.notifications.MainActivity
import com.example.notifications.R
import com.example.notifications.clases.component.TareaNotification
import com.example.notifications.clases.entity.Seccion
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
        val divider1: View
        var timer: CountDownTimer? = null


        init {
            descriptionTextView = view.findViewById(R.id.rv_tarea_description)
            dateTextView = view.findViewById(R.id.rv_tarea_fecha)
            hourTextView = view.findViewById(R.id.rv_tarea_hora)
            timerTextView = view.findViewById(R.id.rv_tarea_temporizador)
            divider1= view.findViewById(R.id.tarea_divider1)

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

        val zero:Long=0
        if(tarea.date==zero){
            //holder.dateTextView.visibility = View.INVISIBLE
            holder.dateTextView.visibility = View.GONE
            holder.hourTextView.visibility = View.GONE
            holder.timerTextView.visibility = View.GONE
            holder.divider1.visibility = View.GONE
        }
        else{
            val date = Date(tarea.date)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val timeFormat = SimpleDateFormat("HH:mm:ss")

            holder.dateTextView.text = dateFormat.format(date)
            holder.hourTextView.text = timeFormat.format(date)

            var timer: CountDownTimer? = holder.timer
            val currentDate = Date()
            val timeDiff = tarea.date - currentDate.time
            if (timeDiff < 0) {
                holder.timerTextView.text = "00:00:00"
            } else {
                if (timer == null) {
                    timer = object : CountDownTimer(timeDiff, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                            val mins = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished - TimeUnit.HOURS.toMillis(hours))
                            val secs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(mins))
                            val timerResult = String.format("%02d:%02d:%02d", hours, mins, secs)
                            holder.timerTextView.text = timerResult


                            val (launch, bundle) = checkToLaunch(hours.toInt(), mins.toInt(), secs.toInt())
                            if (launch) {
                                //createChannel
                                val notificationManager =
                                    holder.itemView.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val channel = NotificationChannel(
                                        "MY_CHANNEL_ID",
                                        "Channel Name",
                                        NotificationManager.IMPORTANCE_DEFAULT
                                    )
                                    notificationManager.createNotificationChannel(channel)
                                }
                                //scheduleNotification
                                val intent = Intent(
                                    holder.itemView.context,
                                    TareaNotification::class.java
                                ).apply {
                                    putExtra("channel", "MY_CHANNEL_ID")
                                    putExtras(bundle)

                                }
                                val pendingIntent = PendingIntent.getBroadcast(
                                    holder.itemView.context,
                                    TareaNotification.NOTIFICATION_ID,
                                    intent,
                                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                )
                                val alarmManager = holder.itemView.context.getSystemService(ALARM_SERVICE) as AlarmManager
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis , pendingIntent)

                            }
                        }

                        override fun onFinish() {
                            holder.timerTextView.text = "00:00:00"
                        }
                    }
                    holder.timer = timer
                }
                timer.start()
            }
        }
    }

    private fun checkToLaunch(hour: Int, min: Int, sec:Int): Pair<Boolean, Bundle> {
        var launch = false
        var bundle = Bundle()

        if (hour == 1 && min == 0 && sec == 0) {
            bundle.putString("title", "Tarea")
            bundle.putString("resume", "La Tarea finalizará en ${hour}hr:00min:0seg")
            bundle.putString("bigText", "Tarea super")
            launch = true
        } else if (hour == 0 && sec == 0) {
            if (min == 10 || min == 5 || min == 0) {
                bundle.putString("title", "Tarea")
                bundle.putString("resume", "La Tarea finalizará en ${min}min")
                bundle.putString("bigText", "Tarea super min")
                launch = true
            }
        }
        return Pair(launch, bundle)
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}