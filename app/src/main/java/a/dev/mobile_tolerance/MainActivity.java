package a.dev.mobile_tolerance;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import a.dev.mobile_tolerance.forFragment.TableTolerance;
import a.dev.mobile_tolerance.utils.AssetDatabaseOpenHelper;
import a.dev.mobile_tolerance.utils.ExternalDbOpenHelper;

public class MainActivity extends AppCompatActivity {

    private AssetDatabaseOpenHelper adb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



       //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        adb = new AssetDatabaseOpenHelper(this);
        adb.openDatabase();


        setFragmentTolerance();
    }



    private void setFragmentTolerance() {
        Fragment fragment;
        fragment = new TableTolerance().newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();


    }


}
