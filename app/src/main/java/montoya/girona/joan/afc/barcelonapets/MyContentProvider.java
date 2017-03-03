package montoya.girona.joan.afc.barcelonapets;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by joangmontoya on 24/6/15.
 */
public class MyContentProvider extends ContentProvider {

    private DBHelper myDB;

    private static final String AUTHORITY = "montoya.girona.joan.afc.barcelonapets.MyContentProvider";
    public static final String ANIMALS_TABLE = Contract.TABLE_ANIMALS;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ANIMALS_TABLE);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int ANIMALS = 1;
    public static final int ANIMALS_ID = 2;

    static {
        sURIMatcher.addURI(AUTHORITY, ANIMALS_TABLE, ANIMALS);
        sURIMatcher.addURI(AUTHORITY, ANIMALS_TABLE + "/#", ANIMALS_ID);
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHelper(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Contract.TABLE_ANIMALS);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case ANIMALS_ID:
                queryBuilder.appendWhere(Contract.Animal.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case ANIMALS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case ANIMALS:
                id = sqlDB.insert(Contract.TABLE_ANIMALS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(ANIMALS_TABLE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case ANIMALS:
                rowsDeleted = sqlDB.delete(Contract.TABLE_ANIMALS,
                        selection,
                        selectionArgs);
                break;

            case ANIMALS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Contract.TABLE_ANIMALS,
                            Contract.Animal.COLUMN_ID + "=" + id,
                            null);
                }
                else {
                    rowsDeleted = sqlDB.delete(Contract.TABLE_ANIMALS,
                            Contract.Animal.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case ANIMALS:
                rowsUpdated = sqlDB.update(Contract.TABLE_ANIMALS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ANIMALS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(Contract.TABLE_ANIMALS,
                                    values,
                                    Contract.Animal.COLUMN_ID + "=" + id,
                                    null);
                }
                else {
                    rowsUpdated =
                            sqlDB.update(Contract.TABLE_ANIMALS,
                                    values,
                                    Contract.Animal.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}

