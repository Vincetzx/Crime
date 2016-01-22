package com.example.god.myapplication;

import android.content.Context;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by god on 2016/1/21.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";
    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressBar;


    private android.hardware.Camera.ShutterCallback mShutterCallback=new android.hardware.Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    };

    private android.hardware.Camera.PictureCallback mJepgCallBack=new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            String fileName= UUID.randomUUID().toString() + ".jpg";
            boolean success=true;
            FileOutputStream outputStream=null;
            try
            {
                outputStream=getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.write(data);
            }
            catch(Exception e)
            {
                Log.e(TAG, "onPictureTaken error writing to file "+fileName,e);
                success=false;
            }
            finally {
                try
                {
                    if(outputStream!=null)
                    {
                        outputStream.close();
                    }
                }
                catch(Exception e)
                {
                    Log.e(TAG, "onPictureTaken error on closing"+fileName,e);
                }
            }
            if(success)
            {
                Log.i(TAG, "onPictureTaken saved at"+fileName);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        Button takePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
        mProgressBar=view.findViewById(R.id.crime_camera_progressContainer);
        mProgressBar.setVisibility(View.INVISIBLE);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mCamera!=null)
               {
                   mCamera.takePicture(mShutterCallback,null, mJepgCallBack);
               }
            }
        });
        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "surfaceCreated error setting", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if (mCamera == null) {
                    return;
                }
                android.hardware.Camera.Parameters parameters = mCamera.getParameters();
                android.hardware.Camera.Size size = getBestSupporedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(size.width, size.height);
                size=getBestSupporedSize(parameters.getSupportedPictureSizes(),width,height);
                parameters.setPictureSize(size.width,size.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });

        return view;
    }


    private android.hardware.Camera.Size getBestSupporedSize(List<android.hardware.Camera.Size> sizes, int widht, int height) {
        android.hardware.Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (android.hardware.Camera.Size size : sizes) {
            int area = size.width * size.height;
            if (area > largestArea) {
                bestSize = size;
                largestArea = area;
            }
        }
        return bestSize;
    }


    @Override
    public void onResume() {
        super.onResume();
        mCamera = mCamera.open();
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