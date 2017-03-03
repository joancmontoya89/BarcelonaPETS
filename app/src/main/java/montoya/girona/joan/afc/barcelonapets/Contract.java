package montoya.girona.joan.afc.barcelonapets;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by joangmontoya on 24/6/15.
 */
public class Contract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "montoya.girona.joan.afc.barcelonapets.app";

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://montoya.girona.joan.afc.barcelonapets.app/month/ is a valid path for
    // looking at animals information related to an specific month
    public static final String PATH_MONTH = "month";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MONTH).build();

    public static final String TABLE_ANIMALS = "animals";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_ANIMALS + " (" +
                    Animal.COLUMN_ID + " INTEGER PRIMARY KEY," +    //num of month: 0..11
                    Animal.COLUMN_RECDOGS + TEXT_TYPE + COMMA_SEP + //num of recollected dogs
                    Animal.COLUMN_RESDOGS + TEXT_TYPE + COMMA_SEP + //num of rescued dogs
                    Animal.COLUMN_ADODOGS + TEXT_TYPE + COMMA_SEP + //num of adopted dogs
                    Animal.COLUMN_RECCATS + TEXT_TYPE + COMMA_SEP + //num of recollected cats
                    Animal.COLUMN_RESCATS + TEXT_TYPE + COMMA_SEP + //num of rescued cats
                    Animal.COLUMN_ADOCATS + TEXT_TYPE + COMMA_SEP + " )";//and adopted cats

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_ANIMALS;

    public Contract() {}

    public static abstract class Animal implements BaseColumns {

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECDOGS = "recollecteddogs";
        public static final String COLUMN_RESDOGS = "rescueddogs";
        public static final String COLUMN_ADODOGS = "adopteddogs";
        public static final String COLUMN_RECCATS = "recollectedcats";
        public static final String COLUMN_RESCATS = "rescuedcats";
        public static final String COLUMN_ADOCATS = "adoptedcats";

        //build uri related to an specific month
        public static Uri buildMonthUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
