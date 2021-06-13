package com.example.myapplication;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import data.BookContract;



public class



BookCursorAdapter extends CursorAdapter {


    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in list item layout
        final int id = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry._ID));
        TextView BookTitle = view.findViewById(R.id.Book_name_list);
        TextView AuthorName = view.findViewById(R.id.author_name_list);
        TextView showcount = view.findViewById(R.id.counting);
        String book_title = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE));
        String author_name = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR_NAME));
        final int productQuantity = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_STOCK));
        showcount.setText(String.valueOf(productQuantity));
        BookTitle.setText(book_title);
        AuthorName.setText(author_name);


        Button saleButton = (Button) view.findViewById(R.id.sell_butn);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                if (productQuantity > 0) {
                    Uri CurrentProductUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                    int currentAvailableQuantity = productQuantity;
                    currentAvailableQuantity -= 1;
                    values.put(BookContract.BookEntry.COLUMN_STOCK, currentAvailableQuantity);
                    resolver.update(
                            CurrentProductUri,
                            values,
                            null,
                            null
                    );
                    context.getContentResolver().notifyChange(CurrentProductUri, null);
                } else {
                    Toast.makeText(v.getContext(), "out_of_stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}