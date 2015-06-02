package niemisami.badger;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Matti on 29.5.2015.
 */
public class CustomText2 extends TextView {
    public CustomText2(Context context,AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/foo.ttf"));
    }
}
