package niemisami.badger;


import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */

//ImageFragment shows photo clicked on BadgeFragment as a Dialog on top of BadgeActivity
public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH = "niemisami.badger.image_path";
    private ImageView mImageView;


    public ImageFragment() {}


    public static ImageFragment newInstance(String path){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, path);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureManager.scaleDrawableForDisplay(getActivity(), path);

        mImageView.setImageDrawable(image);
        return mImageView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureManager.cleanImageView(mImageView);
    }
}
