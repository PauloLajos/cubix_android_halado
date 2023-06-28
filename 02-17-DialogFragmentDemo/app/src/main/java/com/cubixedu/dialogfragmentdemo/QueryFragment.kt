package com.cubixedu.dialogfragmentdemo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cubixedu.dialogfragmentdemo.databinding.LayoutDialogBinding

class QueryFragment: DialogFragment() {

    private var _binding : LayoutDialogBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //val message = arguments?.getString(MainActivity.KEY_MSG)

        _binding = LayoutDialogBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setTitle("Please read this message")
            //.setMessage(message)
            .setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
                //onQueryFragmentAnswer!!.onPositiveSelected(etName.text.toString())
            })
            .setNegativeButton("Nope", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
                //onQueryFragmentAnswer!!.onNegativeSelected()
            })
            .create()
    }
}