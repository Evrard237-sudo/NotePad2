package androidkotlin.training

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfimDeleteDialogNoteFragment (val noteTitle: String = "") : DialogFragment() {

    interface confirmDeleteDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    var listener: confirmDeleteDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage("Etes-vous sures de supprimer la note \"$noteTitle\" ?")
            .setPositiveButton("Supprimer",
                DialogInterface.OnClickListener{ dialog, id -> listener?.onDialogPositiveClick() })
            .setNegativeButton("Annuler",
                DialogInterface.OnClickListener{ dialog, id -> listener?.onDialogNegativeClick() })

        return builder.create()
    }
}