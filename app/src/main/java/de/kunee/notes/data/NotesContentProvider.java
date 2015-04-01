package de.kunee.notes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.text.TextUtils;

public class NotesContentProvider extends ContentProvider {

    private static final int NOTES = 1;
    private static final int NOTES_WITH_ID = 2;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = NotesContract.CONTENT_AUTHORITY;
        String path = NotesContract.Notes.PATH;
        uriMatcher.addURI(authority, path, NOTES);
        uriMatcher.addURI(authority, path + "/#", NOTES_WITH_ID);
        return uriMatcher;
    }

    private NotesDatabaseHelper db;

    @Override
    public boolean onCreate() {
        db = new NotesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return NotesContract.Notes.CONTENT_TYPE;
            case NOTES_WITH_ID:
                return NotesContract.Notes.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor resultCursor;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case NOTES_WITH_ID:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        resultCursor = db.getReadableDatabase().query(
                NotesContract.Notes.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return resultCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri resultUri;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                long id = db.getWritableDatabase().insert(
                        NotesContract.Notes.TABLE_NAME,
                        null,
                        values
                );
                if (id > 0) {
                    resultUri = ContentUris.withAppendedId(NotesContract.Notes.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                rowsDeleted += db.getWritableDatabase().delete(
                        NotesContract.Notes.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsUpdated = 0;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                rowsUpdated += db.getWritableDatabase().update(
                        NotesContract.Notes.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
