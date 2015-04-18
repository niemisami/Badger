package niemisami.badger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sami on 18.4.2015.
 */
public class BadgeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "badges.sqlite";
    //    Version will be checked on upgrade or on downgrade of the db
    private static final int DB_VERSION = 1;

    private static final String TABLE_BADGE = "badges";
    private static final String COLUMN_BADGE_ID = "id";
    private static final String COLUMN_BADGE_NAME = "name";
    private static final String COLUMN_BADGE_DATE = "date";
    private static final String COLUMN_BADGE_EXTRA = "extra_info";
    private static final String COLUMN_BADGE_ATTACHED = "is_attached";

    public BadgeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BADGE  + "("
                        + COLUMN_BADGE_ID + "_ID STRING PRIMARY KEY ON CONFLICT REPLACE, "
                        + COLUMN_BADGE_NAME + " STRING, "
                        + COLUMN_BADGE_DATE + " STRING, "
                        + COLUMN_BADGE_ATTACHED + " BOOLEAN DEFAULT FALSE, "
                        + COLUMN_BADGE_EXTRA + " STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BADGE);
        onCreate(db);
    }
}
