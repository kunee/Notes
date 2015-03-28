package de.kunee.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "notes.db";
    private static final String NOTE_TABLE_CREATE = String.format("CREATE TABLE %s (%s TEXT)", NotesContract.NoteEntry.TABLE_NAME, NotesContract.NoteEntry.COLUMN_NOTE);
    private static final String NOTE_TABLE_DROP = String.format("DROP TABLE IF EXISTS %s", NotesContract.NoteEntry.TABLE_NAME);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NOTE_TABLE_DROP);
        onCreate(db);
    }
}
