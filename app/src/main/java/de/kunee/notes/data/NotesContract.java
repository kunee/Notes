package de.kunee.notes.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract {

    /* CONTENT_URI = content://de.kunee.notes.provider/notes */
    public static final String CONTENT_AUTHORITY = "de.kunee.notes.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Notes implements BaseColumns {

        // Database
        static final String TABLE_NAME = "note";
        static final String COLUMN_NOTE = "note";

        // Content Provider
        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = getContentUri(PATH);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
    }

    private static Uri getContentUri(String path) {
        return BASE_CONTENT_URI.buildUpon().appendPath(path).build();
    }
}
