package com.example.god.myapplication;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by god on 2016/1/19.
 */
public class CrimeLab {
    private Context mContext;
    private ArrayList<Crime> mCrimes;
    private static CrimeLab mCrimeLab;
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CrimeJSONSerializer mCrimeJSONSerializer;

    private CrimeLab(Context context) {
        mContext = context;


        mCrimeJSONSerializer = new CrimeJSONSerializer(mContext, FILENAME);

        try {
            mCrimes = mCrimeJSONSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<>();
            Log.e(TAG, "CrimeLab loading error", e);
        }
    }

    public boolean saveCrimes() {
        try {
            mCrimeJSONSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "saveCrimes saved success");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "saveCrimes is error", e);
            return false;
        }
    }

    public static CrimeLab getCrimeLab(Context context) {
        if (mCrimeLab == null) {
            mCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return mCrimeLab;
    }

    public void deleteCrime(Crime c)
    {
        mCrimes.remove(c);

    }
    public ArrayList<Crime> getCrimes()
    {
        return mCrimes;
    }
    public Crime getCrime(UUID uuid)
    {
        for (Crime crime : mCrimes) {
            if (crime.getID()==uuid)//但是这里好像又可以了，我服了
                return crime;
        }
        return null;
    }
    public void addCrime(Crime crime)
    {
        mCrimes.add(crime);
    }
}
