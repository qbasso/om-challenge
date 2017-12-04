package pl.qbasso.omisechallenge.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_success.view.*
import pl.qbasso.omisechallenge.R

class SuccessDialog : DialogFragment() {

    var successListener: SuccessClickListener? = null

    interface SuccessClickListener {
        fun click()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null, false)
        view.okButton.setOnClickListener { _ ->
            dismiss()
            successListener?.click()
        }
        return AlertDialog.Builder(context).setView(view).create()
    }
}