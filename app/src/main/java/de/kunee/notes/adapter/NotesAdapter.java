package de.kunee.notes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
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
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.list_item_textview);
        String text = convertCursorToText(cursor);
        Log.d(LOG_TAG, String.format("Binding text '%s' to list item view", text));
        tv.setText(text);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.list_item_delete);
        deleteButton.setTag(cursor.getString(cursor.getColumnIndex(NotesContract.Notes._ID)));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _id = v.getTag().toString();
                context.getContentResolver().delete(NotesContract.Notes.CONTENT_URI, NotesContract.Notes._ID + " = ?",
                        new String[]{_id});
            }
        });
    }

    public String convertCursorToText(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(NotesContract.Notes.COLUMN_NOTE);
        return cursor.getString(columnIndex);
    }
}
