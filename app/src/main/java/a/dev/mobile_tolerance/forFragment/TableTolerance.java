package a.dev.mobile_tolerance.forFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inqbarna.tablefixheaders.TableFixHeaders;

import java.util.ArrayList;

import a.dev.mobile_tolerance.AppConst;
import a.dev.mobile_tolerance.R;
import a.dev.mobile_tolerance.utils.ExternalDbOpenHelper;
import a.dev.mobile_tolerance.utils.UtilsHelper;



public class TableTolerance extends Fragment {
    private FragmentActivity activity;


    public TableTolerance newInstance() {
        return new TableTolerance();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;

    }

    private TableFixHeaders tableFixHeaders;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.table, container, false);


        tableFixHeaders = (TableFixHeaders) view.findViewById(R.id.table);


        new OpenQuery().execute();
        return view;
    }


    private ArrayList<ToleranceItem> arrayList;

    private void query() {
        arrayList = new ArrayList<>();
        String query;
        Cursor cursor;
        query = "select * from tolerance ";
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(activity);
        SQLiteDatabase database = dbOpenHelper.openDataBase();
        cursor = database.rawQuery(query, null);
        String headerColumn;
        String[] values;
        String test;
        if (cursor.moveToFirst()) {
            values = new String[cursor.getCount()];
            for (int i1 = 0; i1 < cursor.getColumnCount(); i1++) {
                headerColumn = cursor.getColumnName(i1);
                cursor.moveToPosition(0);
                for (int i2 = 0; i2 < cursor.getCount(); i2++) {
                    cursor.moveToPosition(i2);
                    if (cursor.getString(i1) == null) {
                        test = "null";
                    } else test = cursor.getString(i1);
                    values[i2] = test;
                }

                ToleranceItem toleranceItem = new ToleranceItem(headerColumn, values);
                arrayList.add(toleranceItem);
                values = new String[cursor.getCount()];


            }
            cursor.close();
            database.close();
            dbOpenHelper.close();
        }


    }


    private class OpenQuery extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... params) {
            query();
           /* UtilsHelper.stopThread(5);*/
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            tableFixHeaders.setAdapter(new ToleranceAdapter(activity, arrayList));


            Log.d(UtilsHelper.getTag(), "X= " + tableFixHeaders.getActualScrollX() + " / Y= " + tableFixHeaders.getActualScrollY());


            if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.HONEYCOMB){
                tableFixHeaders.setScrollX(loadIntFromFile());
            }



        }
    }


    private void saveIntInFile(int value) {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(AppConst.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConst.SETTING_LAST_POSITION, value);
        editor.apply();
    }


    private int loadIntFromFile() {



        SharedPreferences sharedPreferences;
        int i = 0;
        sharedPreferences = activity.getSharedPreferences(AppConst.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(AppConst.SETTING_LAST_POSITION)) {
            i = sharedPreferences.getInt(AppConst.SETTING_LAST_POSITION, i);
        }
        return i;
    }



    @Override
    public void onResume() {
        Log.d("TEXT", "onResume");
        super.onResume();
    }


    @Override
    public void onPause() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){

            if (tableFixHeaders.getActualScrollY()<214748364) {
                saveIntInFile(tableFixHeaders.getActualScrollX());
            }


        }

        super.onPause();
    }





}


