package com.example.god.myapplication;

import android.support.v4.app.Fragment;

/**
 * Created by god on 2016/1/19.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
