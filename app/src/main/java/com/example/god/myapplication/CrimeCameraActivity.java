package com.example.god.myapplication;

import android.support.v4.app.Fragment;

/**
 * Created by god on 2016/1/25.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
