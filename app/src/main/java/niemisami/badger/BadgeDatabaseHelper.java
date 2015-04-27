package niemisami.badger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Camera;
import android.nfc.Tag;
import android.util.Log;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Sami on 18.4.2015.
 */
public class BadgeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "BadgeDatabaseHelper";
    private static final String DB_NAME = "badges.sqlite";
    //    Version will be checked on upgrade or on downgrade of the db
    private static final int DB_VERSION = 1;

    private static final String TABLE_BADGE = "badges";
    private static final String COLUMN_BADGE_ID = "_id";
    private static final String COLUMN_BADGE_NAME = "name";
    private static final String COLUMN_BADGE_DATE = "date";
    private static final String COLUMN_BADGE_EXTRA = "extra_info";
    private static final String COLUMN_BADGE_ATTACHED = "is_attached";
    private static final String COLUMN_BADGE_PHOTO_PATH = "photo_filename";

    public BadgeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BADGE  + "("
                        + COLUMN_BADGE_ID + " STRING PRIMARY KEY ON CONFLICT REPLACE, "
                        + COLUMN_BADGE_NAME + " STRING, "
                        + COLUMN_BADGE_DATE + " STRING, "
                        + COLUMN_BADGE_ATTACHED + " BOOLEAN DEFAULT FALSE, "
                        + COLUMN_BADGE_EXTRA + " STRING)");
//                        + COLUMN_BADGE_PHOTO_PATH + " STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BADGE);
        onCreate(db);
        Log.i(TAG, "Database upgraded from version: " + oldVersion + " to: " +  newVersion);
    }


    public void saveBadgesToDb(ArrayList<Badge> badges) {
//        Adding badge data to ContentValues
        SQLiteDatabase db = getWritableDatabase();

//        With transaction some other resource can use the same thread that the sqlite is using.
//        This gathers all the insertions to the database and then commits it in finally block
//        yieldIfContendedSafely allows other resources to use the thread.
        db.beginTransaction();
        try {
            for (Badge badge : badges) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_BADGE_ID, badge.getId().toString());
                cv.put(COLUMN_BADGE_NAME, badge.getName());
                cv.put(COLUMN_BADGE_DATE, badge.getDate().toString());
                cv.put(COLUMN_BADGE_EXTRA, badge.getExtraInfo());
                cv.put(COLUMN_BADGE_ATTACHED, badge.getIsAttached());
//                cv.put(COLUMN_BADGE_PHOTO_PATH, badge.getPhoto());

//          Writing to the database. 1. which table to save 2. cursor 3. ContentValue that holds the value key pairs

                db.insert(TABLE_BADGE, null, cv);
                db.yieldIfContendedSafely();
            }
            db.setTransactionSuccessful();
        } finally{
            db.endTransaction();

        }
        Log.i(TAG, "Badges saved to db");
    }

    public boolean deleteBadge(UUID badgeId) {
        SQLiteDatabase db = getWritableDatabase();
        String[] tmpId = new String[] { badgeId.toString()};

        int result = db.delete(TABLE_BADGE, COLUMN_BADGE_ID + "= ? ", tmpId);
        Log.i(TAG, "Badge: " + badgeId + " succesfully removed from the database");
        return (result > 0);
    }

    public ArrayList<Badge> getBadgesFromDb() throws SQLDataException{
        ArrayList<Badge> badges = new ArrayList<Badge>();
        String selectAllQuery = "SELECT * FROM " + TABLE_BADGE;
        SQLiteDatabase sb = getWritableDatabase();

        Cursor cursor = sb.rawQuery(selectAllQuery, null);

        while(cursor.moveToNext()) {

            Badge badge = new Badge(UUID.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_BADGE_ID))));
            badge.setName(cursor.getString(cursor.getColumnIndex(COLUMN_BADGE_NAME)));

            String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_BADGE_DATE));
            DateFormat formatOfDate = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
//            Log.d(TAG, dateString + "EEE MMM dd hh:mm:ss zzz yyyy");
            try {
                Date date = formatOfDate.parse(dateString);
//                Log.d(TAG, date.toString());
                badge.setDate(date);
            } catch (ParseException e) {
                Log.e(TAG, "Error while parsing date", e);
            }

            badge.setExtraInfo(cursor.getString(cursor.getColumnIndex(COLUMN_BADGE_EXTRA)));
            String extraInfo = cursor.getString(cursor.getColumnIndex(COLUMN_BADGE_NAME));
            badge.setIsAttached(cursor.getInt(cursor.getColumnIndex(COLUMN_BADGE_ATTACHED)) == 1);

            badges.add(badge);
        }
        cursor.close();

        return badges;
    }
}
