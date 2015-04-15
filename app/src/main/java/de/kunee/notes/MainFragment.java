package de.kunee.notes;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import de.kunee.notes.adapter.NotesAdapter;
import de.kunee.notes.data.NotesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final String LOG_TAG = getClass().getSimpleName();

    public static final int NOTES_LOADER_ID = 0;

    private NotesAdapter notesAdapter;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "Creating main fragment");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d(LOG_TAG, "Adding notes adapter to main fragment");
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_main_listview);
        notesAdapter = new NotesAdapter(getActivity(), null, 0);
        listView.setAdapter(notesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Toast.makeText(getActivity(), notesAdapter.convertCursorToText(cursor), Toast.LENGTH_SHORT).show();
                }
            }
        });
        final EditText editText = (EditText) rootView.findViewById(R.id.fragment_main_edittext);
        Button submitButton = (Button) rootView.findViewById(R.id.fragment_main_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable text = editText.getText();
                if (!TextUtils.isEmpty(text)) {
                    ContentValues values = new ContentValues();
                    values.put(NotesContract.Notes.COLUMN_NOTE, text.toString());
                    getActivity().getContentResolver().insert(NotesContract.Notes.CONTENT_URI, values);
                }
                editText.setText(null);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Initializing notes loader after activity was created");
        getLoaderManager().initLoader(NOTES_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "Creating cursor loader");
        return new CursorLoader(
                getActivity(),
                NotesContract.Notes.CONTENT_URI,
                new String[]{
                        NotesContract.Notes._ID,
                        NotesContract.Notes.COLUMN_NOTE
                },
                null,
                null,
                NotesContract.Notes._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Finished loading notes");
        notesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Resetting notes loader");
        notesAdapter.swapCursor(null);
    }
}
