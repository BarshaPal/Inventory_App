package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.example.myapplication";
    public static final String PATH_BOOKS = "finalbooktablepls";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

       public static final class BookEntry implements BaseColumns {
           public  static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        public final static String TABLE_NAME = "finalbooktablepls";

           public static final String CONTENT_LIST_TYPE =
                   ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


           public static final String CONTENT_ITEM_TYPE =
                   ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_BOOK_TITLE = "book_title";

           public final static String COLUMN_QUANTITY = "book_quantity";
        public final static String COLUMN_SELLER_NAME = "seller_name";


        public final static String COLUMN_SELLER_EMAIL = "seller_email";
           public final static String COLUMN_STOCK = "in_stock";


        public final static String COLUMN_SELLER_PHONENUMBER = "seller_phonenumber";
        public final static String COLUMN_AUTHOR_NAME = "author_name";
        public final static String COLUMN_COST = "cost";
        public final static String COLUMN_BOOK_TYPE = "book_type";
           public static final int TYPE_UNKNOWN = 0;
           public static final int TYPE_EBOOK = 1;
           public static final int TYPE_PDF = 2;
           public static final int TYPE_OFFLINE = 3;
    }
}