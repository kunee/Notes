package de.kunee.notes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import de.kunee.notes.R;
import de.kunee.notes.data.NotesContract;

public class NotesAdapter extends CursorAdapter {

    public final String LOG_TAG = getClass().getSimpleName();

    public NotesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(LOG_TAG, "Inflating new list item view");
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view;
        String text = convertCursorToText(cursor);
        Log.d(LOG_TAG, String.format("Binding text '%s' to list item view", text));
        tv.setText(text);
    }

    private String convertCursorToText(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(NotesContract.Notes.COLUMN_NOTE);
        return cursor.getString(columnIndex);
    }
}
