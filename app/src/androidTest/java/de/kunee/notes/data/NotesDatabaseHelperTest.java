package de.kunee.notes.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Set;

public class NotesDatabaseHelperTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        mContext.deleteDatabase(NotesDatabaseHelper.DATABASE_NAME);
    }

    public void testOnCreate() throws Exception {
        SQLiteDatabase db = new NotesDatabaseHelper(mContext).getWritableDatabase();
        assertTrue(db.isOpen());

        Set<String> tableNames = new HashSet<>();
        tableNames.add(NotesContract.Notes.TABLE_NAME);

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue(cursor.moveToFirst());
        do {
            tableNames.remove(cursor.getString(0));
        } while (cursor.moveToNext());
        assertTrue("Tables missing", tableNames.isEmpty());

        Set<String> columnNames = new HashSet<>();
        columnNames.add(NotesContract.Notes._ID);
        columnNames.add(NotesContract.Notes.COLUMN_NOTE);

        cursor = db.rawQuery(String.format("PRAGMA table_info(%s)", NotesContract.Notes.TABLE_NAME), null);
        assertTrue(cursor.moveToFirst());
        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            columnNames.remove(cursor.getString(columnNameIndex));
        } while (cursor.moveToNext());
        assertTrue("Columns missing", columnNames.isEmpty());

        db.close();
        assertFalse(db.isOpen());
    }
}