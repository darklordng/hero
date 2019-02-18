package com.example.h.classattendance;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;

public class Home extends AppCompatActivity {


    public Button searchButton;
    EditText courseET;
    String courseET_contents = null;
    String put_Course = "courseEntered";


    public AlertDialog.Builder alertDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sync:
                    Intent homeIntent = new Intent(Home.this, Sync.class);
                    startActivity(homeIntent);
                    break;
                case R.id.navigation_settings:
                    break;
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
        setContentView(R.layout.activity_home);
        alertDialog = new AlertDialog.Builder(Home.this );
        courseET = (EditText) findViewById(R.id.enter_course_edit_text);
        //courseET_contents = courseET.getText().toString();
        Log.d("CourseEt", courseET.getText().toString());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCourse();
                Intent scanIntent = new Intent(Home.this, Scan.class);
                scanIntent.putExtra(put_Course, courseET.getText().toString());
                startActivity(scanIntent);
                //clear the edit text
                courseET.setText("");
            }
        });
    }

    private boolean validateCourse () {
        if (courseET.getText().toString() == " ") {
            String message = "Course field cannot be empty";
            Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void showAlertDialog() {
        //set dialog title
        alertDialog.setTitle("Logout");
        //set dialog message
        alertDialog.setMessage("Are you sure you want to logout?");
        //set icon to dialog
        alertDialog.setIcon(R.drawable.alert_logout);
        //set positive YES button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent logoutIntent = new Intent(Home.this, login.class);
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
        alertDialog.show();
    }

}
