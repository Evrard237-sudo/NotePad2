package androidkotlin.training

import android.app.Activity
import android.content.Intent
import android.icu.text.AlphabeticIndex
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidkotlin.training.utils.persistNote
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidkotlin.training.utils.deleteNote
import androidkotlin.training.utils.loadNotes

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notes: MutableList<Note>
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val fab: View = findViewById(R.id.create_note_fab)
        fab.setOnClickListener(this)

        notes = loadNotes(this)
        adapter = NoteAdapter(notes, this)

        // J' ajoute des elements notes par défaut
        notes.add(Note("Note 1", "Je fais un premier test"))
        notes.add(Note("Note 2", "Je fais une deuxieme note"))

        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onClick(view: View) {
        if (view.tag != null){
            showNoteDetails(view.tag as Int)
        } else {
            when(view.id){
                R.id.create_note_fab -> createNewNote()
            }
        }
    }

    private fun createNewNote() {
        showNoteDetails(-1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null){
            return
        }
        when (requestCode){
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
    }


    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when(data.action){
            NoteDetailActivity.ACTION_SAVE_NOTE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                if (note != null) {
                    saveNote(note, noteIndex)
                }
            }
            NoteDetailActivity.ACTION_DELETE_NOTE ->  {
                deleteNote(noteIndex)
            }
        }
    }

    fun saveNote(note: Note, noteIndex: Int){
        persistNote(this, note)
        if (noteIndex < 0){
            notes.add(0, note)
        }else{
            notes[noteIndex]= note
        }
        deleteNote(this, note)
        adapter.notifyDataSetChanged()
    }

    private fun deleteNote(noteIndex: Int) {
        if (noteIndex < 0 ){
            return
        }
        val note = notes.removeAt(noteIndex)
        adapter.notifyDataSetChanged()
    }

    fun showNoteDetails(noteIndex: Int){
        val note = if (noteIndex < 0) Note() else  notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
}