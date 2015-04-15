package niemisami.badger;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Sami on 15.4.2015.
 */
public class Badge {


//    UUID is Universally Unique Identifier, which will work as a badge identification
    private UUID mId;
    private String mName;
    private Date mDate;
    private Boolean mIsAttached;

//    Not yer sure if Photo will be saved to JSON or it is just a filename
//      private Photo mPhoto;


    public Badge() {
        mId = UUID.randomUUID();
    }


    public Boolean getIsAttached() {
        return mIsAttached;
    }

    public void setIsAttached(Boolean mIsAttached) {
        this.mIsAttached = mIsAttached;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
