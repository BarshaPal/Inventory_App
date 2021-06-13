package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import data.BookContract;

public class DetailActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView BookTitle;
    private TextView AuthorName;
    private TextView SellerName;
    private TextView SellerPhone;
    private TextView SellerEmail;
    private TextView InStock;
    private Uri currentbook_Uri;
    private String cost;
    private static final int BOOK_LOADER = 0;
    private boolean BookHasChanged=false;
    private Button add_butn;
    private Button sub_butn;
    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        currentbook_Uri = intent.getData();
         BookTitle=findViewById(R.id.idBookTitle);
         AuthorName=findViewById(R.id.id_Author_name);
        SellerName=findViewById(R.id.seller_name_container);
        SellerEmail=findViewById(R.id.email_display_container);
        SellerPhone=findViewById(R.id.phone_display_container);
        InStock=findViewById(R.id.stock_container);
        add_butn=findViewById(R.id.add_butn);
        sub_butn=findViewById(R.id.sub_butn);
        add_butn.setOnTouchListener(mTouchListener);
        sub_butn.setOnTouchListener(mTouchListener);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
        ImageView call_but=findViewById(R.id.call);
        ImageView mail_but=findViewById(R.id.mail);
        call_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num2=SellerPhone.getText().toString();
                Uri num = Uri.parse("tel:" + num2);
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(num);
                startActivity(dial);
            }
        });
        mail_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=SellerEmail.getText().toString();
                String title=BookTitle.getText().toString();
                String author=AuthorName.getText().toString();
                Toast.makeText(DetailActivity.this,mail,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Book your Order");
                String value="BookTitle: "+title+"\n Author Name: "+author+"\nCost: "+cost+"\nEnter your own Details:\n Name:\nQuantity:";
                intent.putExtra(Intent.EXTRA_TEXT, value);
                startActivity(intent);
            }
        });
        add_butn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
              int instock=Integer.parseInt(InStock.getText().toString());
              instock++;
                InStock.setText(""+instock);
            }
        });
        sub_butn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int instock=Integer.parseInt(InStock.getText().toString());
                instock--;
                InStock.setText(""+instock);
            }
        });

    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            BookHasChanged = true;
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.edit:
                if(BookHasChanged)
                {
                    ContentValues values=new ContentValues();
                    Toast.makeText(this,InStock.getText().toString(),Toast.LENGTH_SHORT).show();
                    values.put(BookContract.BookEntry.COLUMN_STOCK,InStock.getText().toString());
                    getContentResolver().update(currentbook_Uri,values,null,null);
                }
                editBook();
                return true;
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!BookHasChanged) {
                    finish();
                    return true;
                }
                onBackPressed();
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
        NavUtils.navigateUpFromSameTask(DetailActivity.this);
    }

    private void editBook() {
        Intent intent = new Intent(DetailActivity.this, EditActivity.class);
        intent.setData(currentbook_Uri);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!BookHasChanged) {
            super.onBackPressed();
            return;
        }

        ContentValues values=new ContentValues();
        Toast.makeText(this,InStock.getText().toString(),Toast.LENGTH_SHORT).show();
        values.put(BookContract.BookEntry.COLUMN_STOCK,InStock.getText().toString());
        getContentResolver().update(currentbook_Uri,values,null,null);
      finish();
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
                BookContract.BookEntry.COLUMN_BOOK_TYPE,
                BookContract.BookEntry.COLUMN_COST,
                BookContract.BookEntry.COLUMN_QUANTITY,
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
             cost=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_COST));
            BookTitle.setText(book_title);
            AuthorName.setText(author_name);
            SellerName.setText(seller_name);
            SellerEmail.setText(seller_email);
            SellerPhone.setText(seller_phone);
            InStock.setText(inStock);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        BookTitle.setText(" ");
        AuthorName.setText(" ");
        SellerName.setText(" ");
        SellerEmail.setText(" ");
        SellerPhone.setText(" ");
        InStock.setText(" ");
    }
}
