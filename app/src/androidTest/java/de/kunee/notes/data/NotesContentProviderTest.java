package de.kunee.notes.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.Map;

public class NotesContentProviderTest extends AndroidTestCase {

    public void testProvider() {

        // insert
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.Notes.COLUMN_NOTE, "Test");

        Uri rowUri = mContext.getContentResolver().insert(
                NotesContract.Notes.CONTENT_URI,
                contentValues
        );
        long rowId = ContentUris.parseId(rowUri);
        assertTrue(rowId != -1);

        // query
        Cursor cursor = mContext.getContentResolver().query(
                NotesContract.Notes.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertTrue(cursor.moveToFirst());
        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            String columnName = entry.getKey();
            String expectedValue = entry.getValue().toString();

            int columnIndex = cursor.getColumnIndex(columnName);
            assertTrue(columnIndex != -1);
            assertEquals(expectedValue, cursor.getString(columnIndex));
        }
        cursor.close();

        //delete
        mContext.getContentResolver().delete(
                NotesContract.Notes.CONTENT_URI,
                null,
                null
        );

        cursor = mContext.getContentResolver().query(
                NotesContract.Notes.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }
}