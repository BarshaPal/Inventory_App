package data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;

import com.example.myapplication.EditActivity;

import java.util.Scanner;

public class BookProvider extends ContentProvider{

    /**
     * Tag for the log messages
     */
    /**
     * URI matcher code for the content URI for the pets table
     */
    private BookDbHelper mDbHelper;
        private static final int BOOKS=100;
        private static final int BOOKS_ID=101;
        private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS,BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS+ "/#",BOOKS_ID);
    }

    /**
     * Database helper object
     */


    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Create and initialize a PetDbHelper object to gain access to the pets database.
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:


                cursor = db.query(BookContract.BookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case BOOKS_ID:

                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};


                cursor = db.query(BookContract.BookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }


    private Uri insertPet(Uri uri, ContentValues values) {


        String name = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_TITLE);
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(String.format("Insert Exception! Product requires a product name!"));
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SELLER_NAME)) {
            String sname = values.getAsString(BookContract.BookEntry.COLUMN_SELLER_NAME);
            if (sname == null || sname.length() == 0) {
                throw new IllegalArgumentException(String.format("Insert Exception! Product requires a product name!"));
            }
        }
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            long id = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        if (id == -1) {

            return null;
        }

            // Notify all listeners that the data has changed for the pet content URI
            getContext().getContentResolver().notifyChange(uri, null);

            return ContentUris.withAppendedId(uri, id);

    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updatePet(uri, values, selection, selectionArgs);
            case BOOKS_ID:
                if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_TITLE)) {

                    String name = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_TITLE);
                    if (name == null || name.isEmpty()) {
                        throw new IllegalArgumentException(String.format("Insert Exception! Product requires a product name!"));

                    }
                }

                if (values.containsKey(BookContract.BookEntry.COLUMN_SELLER_NAME)) {
                    String sname = values.getAsString(BookContract.BookEntry.COLUMN_SELLER_NAME);
                    if (sname == null || sname.length() == 0) {
                        throw new IllegalArgumentException(String.format("Insert Exception! Product requires a product name!"));
                    }
                }
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rows=database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
        if(rows!=0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    return rows;
    }



    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match=sUriMatcher.match(uri);
        int rows;
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        switch (match) {
            case BOOKS:
             rows=db.delete(BookContract.BookEntry.TABLE_NAME,null,null);
            break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rows;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);


        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}