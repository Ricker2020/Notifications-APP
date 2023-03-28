package com.example.notifications.clases.component;

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.notifications.R
import com.example.notifications.clases.entity.Tarea
import com.example.notifications.clases.entity.User
import com.example.notifications.databinding.DialogSeccionBinding
import com.example.notifications.databinding.DialogSessionBinding

class SessionDialog(
    private val onSubmitClickListener: (User) -> Unit
): DialogFragment() {
    //La clase viene de dialog_name.xml
    private lateinit var binding : DialogSessionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSessionBinding.inflate(LayoutInflater.from(context))

        //val builder = AlertDialog.Builder(requireActivity(), R.style.AppTheme)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.btnCheckSession.setOnClickListener {
            //onSubmitClickListener.invoke(binding.inputSeccion.text.toString())
            onSubmitClickListener.invoke(
                User(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
            )
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}