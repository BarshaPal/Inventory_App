package com.example.myapplication;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import data.BookContract;


public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Spinner mBookSpinnner;
    private int mbooktype = BookContract.BookEntry.TYPE_UNKNOWN;
    private EditText mBookTitle;
    private EditText mSellerName;
    private EditText mInStock;
    private Uri currentbook_Uri;
    private EditText mSellerEmail;
    private EditText mSellerphone;
    private EditText mAuthorName;
    private EditText mCost;
    private boolean mBookHasChanged = false;
    private static final int BOOK_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        mBookSpinnner = (Spinner) findViewById(R.id.spinner_gender);
        mBookTitle = (EditText) findViewById(R.id.Book_name);
        mSellerName = (EditText) findViewById(R.id.edit_seller_name);
        mSellerEmail = (EditText) findViewById(R.id.edit_seller_email);
        mSellerphone = (EditText) findViewById(R.id.edit_seller_phone);
        mCost = (EditText) findViewById(R.id.edit_book_price);
        mInStock= (EditText) findViewById(R.id.edit_in_stock);
        mAuthorName = (EditText) findViewById(R.id.author_name);
        mBookSpinnner.setOnTouchListener(mTouchListener);
        mBookTitle.setOnTouchListener(mTouchListener);
        mSellerName.setOnTouchListener(mTouchListener);
        mSellerEmail.setOnTouchListener(mTouchListener);
        mSellerphone.setOnTouchListener(mTouchListener);
        mCost.setOnTouchListener(mTouchListener);
        mInStock.setOnTouchListener(mTouchListener);
        setupSpinner();
        Intent intent = getIntent();
        currentbook_Uri = intent.getData();
        if(currentbook_Uri==null)
        {
            setTitle("Add A Book");
            invalidateOptionsMenu();
        }
        else
        {
            setTitle("Edit Book");
            getLoaderManager().initLoader(BOOK_LOADER, null, this);
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.  unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
       
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.  unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mBookSpinnner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mBookSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_Ebook))) {
                        mbooktype = BookContract.BookEntry.TYPE_EBOOK;
                    } else if (selection.equals(getString(R.string.type_PDF))) {
                        mbooktype = BookContract.BookEntry.TYPE_PDF;

                    }
                    else if (selection.equals(getString(R.string.type_offline))) {
                        mbooktype = BookContract.BookEntry.TYPE_OFFLINE;
                    }
                        else {
                        mbooktype = BookContract.BookEntry.TYPE_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mbooktype = BookContract.BookEntry.TYPE_UNKNOWN;
            }
        });
    }


            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentbook_Uri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                showDeleteConfirmationDialog();

                return true;
            case R.id.save:
                save_books();
                finish();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged) {
                   finish();
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                               finish();
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        if (currentbook_Uri != null) {
            getContentResolver().delete(currentbook_Uri, null, null);
        }
        finish();
        NavUtils.navigateUpFromSameTask(EditActivity.this);
    }

    private void save_books() {

            String BookTitle = mBookTitle.getText().toString().trim();
            String SellerName = mSellerName.getText().toString().trim();
            String SellerEmail = mSellerEmail.getText().toString().trim();
            String SellerPhone = mSellerphone.getText().toString().trim();
            String AuthorName = mAuthorName.getText().toString().trim();
            String InStock = mInStock.getText().toString().trim();
            String Cost = mCost.getText().toString().trim();
            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_BOOK_TITLE, BookTitle);
            values.put(BookContract.BookEntry.COLUMN_SELLER_NAME, SellerName);
            values.put(BookContract.BookEntry.COLUMN_SELLER_EMAIL, SellerEmail);
            values.put(BookContract.BookEntry.COLUMN_SELLER_PHONENUMBER, SellerPhone);
            values.put(BookContract.BookEntry.COLUMN_STOCK, InStock);
            values.put(BookContract.BookEntry.COLUMN_AUTHOR_NAME, AuthorName);
            values.put(BookContract.BookEntry.COLUMN_COST, Cost);
            values.put(BookContract.BookEntry.COLUMN_BOOK_TYPE, mbooktype);
        if (currentbook_Uri == null &&
                TextUtils.isEmpty(BookTitle) && TextUtils.isEmpty(SellerName) &&TextUtils.isEmpty(BookTitle) &&
                TextUtils.isEmpty(SellerName)&&
                TextUtils.isEmpty(SellerEmail) && mbooktype == BookContract.BookEntry.TYPE_UNKNOWN)
        {return;}
        if(currentbook_Uri==null) {
            Uri returnedUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
            if (returnedUri != null) {
                Toast.makeText(this, "successfull", Toast.LENGTH_LONG).show();
            }
        }
            else
            {
                int rowsAffected = getContentResolver().update(currentbook_Uri, values, null, null);
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
              finish();
            }
        }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_TITLE,
                BookContract.BookEntry.COLUMN_SELLER_NAME,
                BookContract.BookEntry.COLUMN_SELLER_EMAIL,
                BookContract.BookEntry.COLUMN_AUTHOR_NAME,
                BookContract.BookEntry.COLUMN_SELLER_PHONENUMBER,
                BookContract.BookEntry.COLUMN_STOCK,
                BookContract.BookEntry.COLUMN_COST,
                BookContract.BookEntry.COLUMN_BOOK_TYPE

        };
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this,
                currentbook_Uri,
                projection,
                null,
                null,null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String book_title=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE));
            String author_name=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR_NAME));
            String seller_name=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_NAME));
            String seller_email=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_EMAIL));
            String seller_phone=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_PHONENUMBER));
            String inStock=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_STOCK));
            String cost=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_COST));
            int gender=cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TYPE));

            mBookTitle.setText(book_title);
            mAuthorName.setText(author_name);
            mSellerName.setText(seller_name);
            mSellerEmail.setText(seller_email);
            mSellerphone.setText(seller_phone);
            mInStock.setText(inStock);
            mCost.setText(cost);

            switch (gender) {
                case BookContract.BookEntry.TYPE_EBOOK:
                    mBookSpinnner.setSelection(1);
                    break;
                case BookContract.BookEntry.TYPE_PDF:
                    mBookSpinnner.setSelection(2);
                    break;
                case BookContract.BookEntry.TYPE_OFFLINE:
                    mBookSpinnner.setSelection(3);
                    break;
                default:
                    mBookSpinnner.setSelection(0);
                    break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBookTitle.setText(" ");
        mAuthorName.setText(" ");
        mSellerName.setText(" ");
        mSellerEmail.setText(" ");
        mSellerphone.setText(" ");
        mInStock.setText(" ");
        mCost.setText(" ");
        mBookSpinnner.setSelection(0);
    }
}
