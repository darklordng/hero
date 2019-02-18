package com.example.h.classattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.h.classattendance.DBModel.ClassContract;
import com.example.h.classattendance.DBModel.ClassHelper;
import com.example.h.classattendance.DBModel.PostRes;
import com.example.h.classattendance.DBModel.Students;
import com.example.h.classattendance.DBModel.utils;
import com.example.h.classattendance.Retrofit.ApiClient;
import com.example.h.classattendance.Retrofit.IEndpoints;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Sync extends AppCompatActivity {

    public AlertDialog.Builder alertDialog;
    String urlAPI = "https://campuspay.bowen.edu.ng/api/alpha/classattend";
    Button syncButton;
    private IEndpoints iEndpoints;
    ClassHelper helper = new ClassHelper(Sync.this);
    SQLiteDatabase db ;
    private static String TAG = Sync.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sync:
                    break;
                case R.id.navigation_settings:

                    return true;
                case R.id.navigation_logout:
                    showAlertDialog();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        //jsonArray
        JSONArray jsonArray = new JSONArray();
        //alertDialog
        alertDialog = new AlertDialog.Builder(Sync.this);
        //sync button
        syncButton = findViewById(R.id.button_sync);
        db = helper.getReadableDatabase();
        iEndpoints = ApiClient.getClient().create(IEndpoints.class);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassHelper classHelper = new ClassHelper(Sync.this);
                ArrayList<Students> students = new ArrayList<>();
                students = classHelper.getStudentsData();
                Students stud = new Students();

                for (int i=0; i<students.size(); i++) {
                    stud = students.get(i);
                    if (!utils.isNetworkAvailable(Sync.this)) {
                        Toast.makeText(Sync.this, "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                    }else {
                        makeRequestCall(stud);
                    }
                }

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /**
     * Method to show alert dialog
     */
    public void showAlertDialog () {
        //set title
        alertDialog.setTitle("Logout");
        //set message
        alertDialog.setMessage("Are you sure you want to logout?");
        //set icon
        alertDialog.setIcon(R.drawable.alert_logout);
        //set positive button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent logoutIntent = new Intent(Sync.this, login.class);
                startActivity(logoutIntent);
                finish();
            }
        });
        //set negative NO button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    /**
     * Method to makeAPIRequestcall
     */
    private void makeRequestCall(Students s) {
        String course = s.getCourse();
        String time = s.getTime();
        String matricno = s.getMatricno();

        iEndpoints.postDetails(course, time, matricno).enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, retrofit2.Response<PostRes> response) {
                Log.d("response", response.toString());
                Log.d("response", response.message());

                Toast.makeText(Sync.this, "Sync Successful!", Toast.LENGTH_LONG).show();

                if (response.body() != null) {
                    Log.d ("response", response.body().getRes().get(1).getTime());
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Toast.makeText(Sync.this, "Sync Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
