package com.example.god.myapplication;

import android.content.Context;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by god on 2016/1/21.
 */
public class CrimeJSONSerializer {
    private Context mContext;
    private String mFile;
    public CrimeJSONSerializer(Context context,String file)
    {
        this.mContext=context;
        this.mFile=file;
    }
    //从文件里面独处Crimes数组到返回的ArrayList<Crime>数组上
    public ArrayList<Crime> loadCrimes() throws JSONException,IOException
    {
        ArrayList<Crime> crimes=new ArrayList<>();
        BufferedReader reader=null;
        try
        {
            InputStream inputStream=mContext.openFileInput(mFile);
            reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            StringBuilder jsonString=new StringBuilder();
            while((line=reader.readLine())!=null)
            {
                jsonString.append(line);
            }
            JSONArray array=(JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();i++)
            {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            if(reader!=null)
            {
                reader.close();
            }
        }
        return crimes;
    }

    //写入Crimes数组到文件
    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException,IOException
    {
        JSONArray array=new JSONArray();
        for (Crime c :
                crimes) {
            array.put(c.toJson());
        }
        Writer writer=null;
        try
        {
            OutputStream out=mContext.openFileOutput(mFile,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            writer.write(array.toString());
        }
        finally {
            if(writer!=null)
            {
                writer.close();
            }
        }

    }
}
