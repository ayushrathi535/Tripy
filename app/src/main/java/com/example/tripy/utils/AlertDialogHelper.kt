package com.example.tripy.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.example.tripy.R

object AlertDialogHelper {

    fun showAlertDialog(
        context: Context,
        title: String,
        icon: Int? = null,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        positiveButtonAction: () -> Unit,
        negativeButtonAction: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            // .setMessage(message)
            .setTitle(title)
            .setIcon(icon ?: 0)
            .setPositiveButton(positiveButtonTitle) { dialog, which ->
                positiveButtonAction.invoke()
            }
            .setNegativeButton(negativeButtonTitle) { dialog, which ->
                negativeButtonAction.invoke()
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    fun showDialog(
        context: Context,
        title: String,
        positiveButtonText: String,
        negativeButtonText: String,
        positiveButtonAction: (String) -> Unit,
        negativeButtonAction: () -> Unit
    ): androidx.appcompat.app.AlertDialog {

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)

        val editText = dialogView.findViewById<EditText>(R.id.editText)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle(title)

        dialogBuilder.setPositiveButton(positiveButtonText) { dialog, which ->
            val text = editText.text.toString()
            positiveButtonAction.invoke(text)

        }

        dialogBuilder.setNegativeButton(negativeButtonText) { dialog, which ->
            negativeButtonAction.invoke()
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)
  alertDialog.show()
        return alertDialog
    }

}
