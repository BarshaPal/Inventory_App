package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "booknew45.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SELLER_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SELLER_EMAIL + " TEXT, "
                + BookContract.BookEntry.COLUMN_SELLER_PHONENUMBER + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_STOCK + " INTEGER NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_BOOK_TYPE + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_COST + " INTEGER NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_AUTHOR_NAME + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * This is called when the database needs to be upgraded.
     */

}