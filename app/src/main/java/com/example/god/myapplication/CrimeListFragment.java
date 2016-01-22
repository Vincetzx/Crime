package com.example.god.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by god on 2016/1/19.
 */
public class CrimeListFragment extends ListFragment {
    private  static final String TAG="CrimeFragment";
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCrimes=CrimeLab.getCrimeLab(getActivity()).getCrimes();
        CrimeAdapter adapter=new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime=((CrimeAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, crime.getTitle()+"Holy fuck ! ");
        Intent intent=new Intent(getActivity(),CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getID());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    class CrimeAdapter extends ArrayAdapter<Crime>
    {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(),0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);

            }
            Crime crime=getItem(position);
            TextView crimeTitle=(TextView)convertView.findViewById(R.id.crime_list_title);
            TextView crimeDate=(TextView)convertView.findViewById(R.id.crime_list_date);
            CheckBox crimeIsSolved=(CheckBox)convertView.findViewById(R.id.crime_list_solvedCheckBox);
            crimeTitle.setText(crime.getTitle());
            crimeDate.setText(crime.getDate().toString() + "");
            crimeIsSolved.setChecked(crime.isSolved());
            return  convertView;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int positon=info.position;
        CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
        Crime crime=adapter.getItem(positon);
        switch (item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getActionBar().setSubtitle("something");
        ListView listView=(ListView)view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater=mode.getMenuInflater();
                menuInflater.inflate(R.menu.crime_list_item_context,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
                        CrimeLab crimeLab=CrimeLab.getCrimeLab(getActivity());
                        for(int i=adapter.getCount()-1;i>=0;i--)
                        {
                            if(getListView().isItemChecked(i))
                            {
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Intent intent=new Intent(getActivity(),CrimePagerActivity.class);
                Crime crime=new Crime();
                CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
                startActivityForResult(intent,0);
                return true;
            default:return super.onOptionsItemSelected(item);
        }

    }
}
