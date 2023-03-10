package androidkotlin.training

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView

@Suppress("DEPRECATION")
class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_EDIT_NOTE = 1
        const val EXTRA_NOTE = "note"
        const val EXTRA_NOTE_INDEX = "noteIndex"
        const val ACTION_SAVE_NOTE = "androidkotlin.training.actions.ACTION_SAVE_NOTE"
        const val ACTION_DELETE_NOTE = "androidkotlin.training.actions.ACTION_DELETE_NOTE"
    }

    lateinit var note: Note
    var noteIndex: Int = -1

    lateinit var titleView: TextView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        setSupportActionBar(findViewById(R.id.toolbar))

        note = intent.getParcelableExtra(EXTRA_NOTE)!!
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById(R.id.title)
        textView = findViewById(R.id.text)

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super .onCreateOptionsMenu(menu)
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                return true
            }
             R.id.action_delete -> {
                 showConfirmDeleteNoteDialog()
                 return true
             }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteNoteDialog() {
        val confirmFragment = ConfimDeleteDialogNoteFragment("${note.title}")
        confirmFragment.listener = object: ConfimDeleteDialogNoteFragment.confirmDeleteDialogListener{
            override fun onDialogPositiveClick() {
                deleteNote()
            }

            override fun onDialogNegativeClick() { }
        }
        confirmFragment.show(supportFragmentManager, "confirmDeleteDialog")
    }

    fun saveNote(){
        note.title = titleView.text.toString()
        note.text = textView.text.toString()

        intent = Intent(ACTION_SAVE_NOTE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun deleteNote(){
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}