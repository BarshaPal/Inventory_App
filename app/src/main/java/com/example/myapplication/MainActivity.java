package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import data.BookContract;
import data.BookContract.BookEntry;
import data.BookDbHelper;

public class MainActivity  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 1;
    private int count_int = 0;
    // This is the Adapter being used to display the list's pet
    BookCursorAdapter mcursorAdapter;
    Button inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inc = findViewById(R.id.add_butn);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);

            }
        });

        // Find the ListView which will be populated with the pet data
        ListView displayView = (ListView) findViewById(R.id.list_view);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.emptycart);
        TextView counting = findViewById(R.id.counting);
        displayView.setEmptyView(emptyView);


        mcursorAdapter = new BookCursorAdapter(this, null);
        displayView.setAdapter(mcursorAdapter);

        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
// TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri contenturi = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(contenturi);
                startActivity(intent);

            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, "Toto");
        values.put(BookEntry.COLUMN_SELLER_NAME, "Terrier");
        values.put(BookEntry.COLUMN_SELLER_EMAIL, "PAL02");
        values.put(BookEntry.COLUMN_SELLER_PHONENUMBER, "096857");
        values.put(BookEntry.COLUMN_STOCK, "10");
        values.put(BookEntry.COLUMN_AUTHOR_NAME, "JHGDF");
        values.put(BookEntry.COLUMN_BOOK_TYPE, BookEntry.TYPE_EBOOK);
        values.put(BookEntry.COLUMN_COST, 45);
        values.put(BookEntry.COLUMN_QUANTITY, 45);

        Uri newUri = getContentResolver().insert(
                BookEntry.CONTENT_URI,   // the pet content URI
                values                  // the values to insert
        );


    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void delete_all() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu

        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.insert_dummy:
                insertPet();

                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.delete_all:

                delete_all();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when a new Loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_SELLER_NAME,
                BookEntry.COLUMN_SELLER_EMAIL,
                BookEntry.COLUMN_AUTHOR_NAME,
                BookEntry.COLUMN_SELLER_PHONENUMBER,
                BookEntry.COLUMN_STOCK,
                BookEntry.COLUMN_COST,
                BookEntry.COLUMN_BOOK_TYPE,
                BookEntry.COLUMN_QUANTITY,

        };
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null, null);
    }

    // Called when a previously created loader has finished loading
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {
            mcursorAdapter.swapCursor(data);
        } else if(data != null && data.getCount() == 0) {
            mcursorAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mcursorAdapter.swapCursor(null);
    }
}