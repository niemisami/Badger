package niemisami.badger;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.hardware.Camera.Size;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("deprecation") // this is so app could still use the old Camera API
public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";

    private Camera mCamera;
    private SurfaceView mViewFinder;
    private ImageButton mPhotoButton;
    private View mProgresSpinner;

    public static final String EXTRA_PHOTO_FILENAME = "niemisami.badger.photo_filename";


    public CameraFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mProgresSpinner = view.findViewById(R.id.camera_progressContainer);
        mProgresSpinner.setVisibility(View.INVISIBLE);

        mPhotoButton = (ImageButton) view.findViewById(R.id.camera_takeImage);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Method needs action that happens when 1. shutter closes, 2. image is wanted in RAW format 3. image is wanted in JPEG format
                mCamera.takePicture(mShutterCallback, null, mJpegCallback);
            }
        });


        mViewFinder = (SurfaceView) view.findViewById(R.id.camera_surfaceView);
        SurfaceHolder holder = mViewFinder.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (mCamera != null)
                        mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    Log.e(TAG, "Error while setting the preview", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mCamera.setDisplayOrientation(90);

                mCamera.setDisplayOrientation(getResources().getConfiguration().getLayoutDirection());
                if (mCamera == null)
                    return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(size.width, size.height);
                size = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(size.width, size.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Could not start preview: ", e);
                    mCamera.release();
                    mCamera = null;
                }
            }


            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null)
                    mCamera.stopPreview();
            }
        });
        return view;
    }


    //    when the image is taken the progress bar comes visible
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgresSpinner.setVisibility(View.VISIBLE);
        }
    };


    //   saving image to JPEG after the image is taken
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String filename = UUID.randomUUID().toString() + ".jpeg";

//      openedd FileOutPutStream for saving image to device
            FileOutputStream os = null;
            boolean success = true;

            try {
//            MODE_PRIVATE so photos can be called only this app
                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e) {
                Log.e(TAG, "Error while writing file: " + filename, e);
            } finally {
                try {
                    if (os != null)
                        os.close();

                } catch (Exception e) {
                    Log.e(TAG, "Error while closing the file: " + filename, e);
                    success = false;
                }
            }

//            CameraFragment is called for result in the BadgeFragment.
//            These results are returned depending on the outcome
            if (success) {
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = width * height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }


    @TargetApi(8)
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }



}
