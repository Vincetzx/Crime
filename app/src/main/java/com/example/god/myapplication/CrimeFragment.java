package com.example.god.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;
import java.util.UUID;

/**
 * Created by god on 2016/1/19.
 */
public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID ="vczx";
    public static final int REQUEST_CODE=0;
     Crime mCrime;
     EditText mCrimeTitle;
     Button mCrimeDate;
     CheckBox mCrimeSolved;
    private ImageButton mImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
     UUID crimeId=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime=CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null)
                {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_delete_crime:
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(mCrime);
                NavUtils.navigateUpFromSameTask(getActivity());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static CrimeFragment newInstance(UUID uuid)
    {
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, uuid);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_item_context,menu);
    }



    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).saveCrimes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime,container,false);
        if(NavUtils.getParentActivityName(getActivity())!=null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mImageButton=(ImageButton)view.findViewById(R.id.crime_imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CrimeCameraActivity.class);
                startActivity(intent);
            }
        });

        mCrimeTitle=(EditText)view.findViewById(R.id.crime_title);
        mCrimeDate=(Button)view.findViewById(R.id.crime_date);
        mCrimeSolved=(CheckBox)view.findViewById(R.id.crime_solved);//这里是空指针，为什么呢，难道是因为mCrime没有初始化？但是已经绑定了啊
        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeDate.setText(mCrime.getDate().toString() + "");

        mCrimeSolved.setChecked(mCrime.isSolved());


        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mCrimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                DatePickerFragment dp=DatePickerFragment.newInstance(mCrime.getDate());
                dp.setTargetFragment(CrimeFragment.this,REQUEST_CODE);
                dp.show(fm,"Date");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode==REQUEST_CODE)
        {
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mCrimeDate.setText(mCrime.getDate().toString());
        }
    }
}
