package com.cubixedu.dialogfragmentdemo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cubixedu.dialogfragmentdemo.databinding.LayoutDialogBinding

class SelectFruitFragment: DialogFragment(), DialogInterface.OnClickListener {

    interface OptionsFragmentInterface {
        fun onOptionsFragmentResult(fruit: String)
    }
    private var optionsFragmentInterface: OptionsFragmentInterface? = null

    companion object {
        const val TAG = "OptionsFragment"
    }

    private val options = arrayOf("Apple", "Orange", "Lemon")

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            optionsFragmentInterface = context as OptionsFragmentInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OptionsFragmentInterface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireActivity())
            .setTitle("Please select")
            .setItems(options,this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        optionsFragmentInterface?.onOptionsFragmentResult(
            options[which])
    }
}