package com.cubixedu.dialogfragmentdemo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class QueryFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //val message = arguments?.getString(MainActivity.KEY_MSG)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.layout_dialog, null)
        val etName = dialogLayout.findViewById<EditText>(R.id.etName)
        alertDialogBuilder.setView(dialogLayout)


        alertDialogBuilder.setTitle("Please read this message")
        //alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.dismiss()
            //onQueryFragmentAnswer!!.onPositiveSelected(etName.text.toString())
        })
        alertDialogBuilder.setNegativeButton("Nope", DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.dismiss()
            //onQueryFragmentAnswer!!.onNegativeSelected()
        })

        return alertDialogBuilder.create()
    }
}