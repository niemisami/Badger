package niemisami.badger;

import android.content.Context;
import android.util.Log;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Sami on 15.4.2015.
 * Badge manager is Singleton class. So it has only one instance at all times
 * when app stays in device's menory
 * <p/>
 * BadgeManager will handle sharing all the data of single badge
 */
public class BadgeManager {

    private static BadgeManager sBadgeManager;
    private static final String TAG = "BadgeManager";

    //    Context allows class to get access activities,
//    resources and storage
    private Context mAppContext;
    private ArrayList<Badge> mBadges;
    private BadgeDatabaseHelper dbHelper;


    private BadgeManager(Context appContext) {
        mAppContext = appContext;
        mBadges = new ArrayList<>();

        mBadges = getBadgesFromDb();

////        Generate few badges for testing
//        for(int i = 0; i < 10; i++){
//            Badge b = new Badge();
//            b.setName("Merkki #" + i);
////           attached odd and and not attached even
//            b.setIsAttached(i % 2 == 0);
//            mBadges.add(b);
//        }
    }

    //    get(Context) will check if BadgeManager has an instance created already
    public static BadgeManager get(Context context) {
        if (sBadgeManager == null) {
            sBadgeManager = new BadgeManager(context.getApplicationContext());
        }
        return sBadgeManager;
    }


//    public boolean saveBadges(){} ;

    public void addBadge(Badge badge) {
        mBadges.add(badge);
    }


    public Badge getBadge(UUID id) {
        for (Badge badge : mBadges) {
            if (badge.getId().equals(id)) {
                return badge;
            }
        }
        return null;
    }

    public ArrayList<Badge> getBadges() {
        mBadges = getBadgesFromDb();
        return mBadges;
    }

    public ArrayList<Badge> getBadgeArray() {
        return mBadges;
    }

    public boolean saveBadges() {
        dbHelper.saveBadgesToDb(mBadges);
        return false;
    }

    private ArrayList<Badge> getBadgesFromDb() {
        dbHelper = new BadgeDatabaseHelper(mAppContext);
        ArrayList<Badge> badges = new ArrayList<>();
        try {
            badges = dbHelper.getBadgesFromDb();
//            Log.d(TAG, Integer.toString(mBadges.size()) + " amount of badges from db");
        } catch (SQLDataException e) {
            Log.e(TAG, "Error loading from database: ", e);
        }
        dbHelper.close();

        return badges;
    }

    public void closeDb() {
        dbHelper.close();
    }
}

