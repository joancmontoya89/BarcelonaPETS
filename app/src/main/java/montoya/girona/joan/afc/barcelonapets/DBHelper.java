package montoya.girona.joan.afc.barcelonapets;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joangmontoya on 24/6/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "animalDB.db";

    private ContentResolver myCR;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TABLE_ANIMALS);
    }

   /* public void addAnimal(String id, String name, String qqt) {

        ContentValues values = new ContentValues();
        values.put(Contract.Product.COLUMN_PRODUCTNAME, name);
        values.put(Contract.Product.COLUMN_QUANTITY, qqt);

        myCR.insert(MyContentProvider.CONTENT_URI, values);
    }

    public Cursor findAnimal(String productname) {
        String[] projection = {Contract.Product.COLUMN_ID, Contract.Product.COLUMN_PRODUCTNAME, Contract.Product.COLUMN_QUANTITY };

        String selection = "productname = \"" + productname + "\"";

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                projection, selection, null,
                null);

        return cursor;
    }

    public int deleteAnimal(String productname) {

        boolean result = false;

        String selection = "productname = \"" + productname + "\"";

        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI, selection, null);

        return rowsDeleted;
    }*/


    /*
     + Operation to get the instance corresponding to a month
     * pre: onth between "0" and "11", both included
     */
    public Cursor findAnimal(String month) {
        String query = "Select * FROM " + Contract.TABLE_ANIMALS + " WHERE " + Contract.Animal.COLUMN_ID + " =  \"" + month + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

}
