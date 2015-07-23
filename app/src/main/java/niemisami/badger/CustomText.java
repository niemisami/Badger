package niemisami.badger;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Matti on 28.5.2015.
 */
public class CustomText extends TextView {

    public CustomText(Context context,AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Woodstamp.otf"));
    }
}
