package niemisami.badger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by Sami on 3.5.2015.
 */

@SuppressWarnings("deprecation")
public class PictureManager {


//    Checks dimensions of the photo stored on the device and scales it to fit the screen
    public static BitmapDrawable scaleDrawableForDisplay(Activity activity, String path) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float displayWidth = display.getWidth();
        float displayHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();


//      Setting the inJustDecodeBounds property to true while decoding avoids memory allocation,
//      returning null for the bitmap object but setting outWidth, outHeight
//      This technique allows you to read the dimensions and type of the image data prior to construction
//      (and memory allocation) of the bitmap.
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcHeight > displayHeight || srcWidth > displayWidth) {
            if(srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / displayHeight);
            } else {
                inSampleSize = Math.round(srcWidth / displayWidth);
            }
        }

        options = new BitmapFactory.Options();

//  inSampleSize will make the photo saved on device x times smaller depending on the inSampleSize determined before
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }


//    method cleanImageView is called from the BadgeFragment for removing image from ImageView
    public static void cleanImageView(ImageView imageView) {
        if(!(imageView.getDrawable() instanceof BitmapDrawable))
            return;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if(drawable.getBitmap() != null)
//        recyle allows garbage collector to remove photos instance so it is not visible in ImageView anymore
//        The actual removal from the device is done in the BadgeFragment
            drawable.getBitmap().recycle();

        imageView.setImageDrawable(null);
    }
}
