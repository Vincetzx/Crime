package com.example.god.myapplication;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by god on 2016/1/26.
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH="com.zx.image_path";
    public static ImageFragment newIntance(String imagePaht)
    {
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH,imagePaht);
        ImageFragment fragment=new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView mImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView=new ImageView(getActivity());
        String path=(String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image=PictureUtils.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

}
