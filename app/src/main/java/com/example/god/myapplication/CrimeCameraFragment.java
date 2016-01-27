package com.example.god.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by god on 2016/1/25.
 */
public class CrimeCameraFragment extends Fragment{
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private static final String TAG="CrimeCameraFragment";
    private View mProgressContainer;

    public static final String EXTRA_PHOTO_FILENAME="fuck all !";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_camera, container, false);
        mSurfaceView=(SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);
        mProgressContainer=view.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        final SurfaceHolder surfaceHolder=mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try{
                    if(mCamera!=null)
                {
                    mCamera.setPreviewDisplay(surfaceHolder);
                }
                }
                catch (IOException e)
                {
                    Log.e(TAG, "surfaceCreated setting up error", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera==null)
                {
                    return;
                }
                Camera.Parameters parameters=mCamera.getParameters();
                Camera.Size size=getBestSupportSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(size.width, size.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "surfaceChanged not start preview",e);
                    mCamera.release();
                    mCamera=null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        Button takePickture=(Button)view.findViewById(R.id.crime_camera_takPicture);
        takePickture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(mShtterCallback,null,mJpegCallback);
            }
        });
        return view;
    }

    private Camera.ShutterCallback mShtterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String fileName=UUID.randomUUID()+".jpg";
            FileOutputStream out=null;
            boolean success=true;
            try
            {
                out=getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                out.write(data);
            }
            catch(Exception e)
            {
                Log.e(TAG, "onPictureTaken error writing"+fileName,e);
                success=false;
            }
            finally {
                try
                {
                    if(out !=null)
                    {
                        out.close();
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onPictureTaken closing fail");
                }
            }
            if(success)
            {
                Intent intent=new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME,fileName);
                getActivity().setResult(Activity.RESULT_OK,intent);
                Log.i(TAG, "onPictureTaken succeed in "+fileName);
            }
            else
            {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }


    };


    @Override
    public void onResume() {
        super.onResume();
        mCamera=Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null)
        {
            mCamera.release();
            mCamera=null;
        }
    }
    private Camera.Size getBestSupportSize(List<Camera.Size> sizes,int width,int height)
    {
        Camera.Size bestSize=sizes.get(0);
        int largestArea=bestSize.width*bestSize.height;

        for (Camera.Size s:
             sizes) {
            int area=s.width*s.height;
            if(area>largestArea)
            {
                largestArea=area;
                bestSize=s;
            }
        }
        return bestSize;
    }
}
