package com.example.h.classattendance;

import   android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.h.classattendance.DBModel.ClassContract;
import com.example.h.classattendance.DBModel.ClassHelper;
import com.example.h.classattendance.DBModel.GetResponse;
import com.example.h.classattendance.DBModel.VolleySingleton;
import com.example.h.classattendance.Retrofit.ApiClient;
import com.example.h.classattendance.Retrofit.IEndpoints;
import com.example.h.classattendance.Retrofit.StaffList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    //variable declaration
    Button loginn;
    ClassHelper getClassHelper;
    EditText staff_id, password;
    RequestQueue requestQueue;

    CheckBox rememberMeCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        staff_id = (EditText) findViewById(R.id.staffId_editText);
        password = (EditText) findViewById(R.id.password_editText);
        loginn = (Button) findViewById(R.id.button_login);
         getClassHelper = new ClassHelper(this);
        requestQueue = Volley.newRequestQueue(login.this);
        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jsonParse();
                checkData();
            }
        });
    }

    /**
     * Method to read data from the staff database through an API
     * and insert into the SQLiteDatabase
     */

    private void jsonParse () {
         String url = "https://campuspay.bowen.edu.ng/api/alpha/secusers";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("res");

                            //Toast.makeText(login.this, "onSuccess", Toast.LENGTH_LONG).show();

                            Log.d("staffid", response.toString());
                            Log.d("password", response.toString());

                            List<GetResponse> dbList = new ArrayList<>();
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                dbList.add(new GetResponse( jsonObject.getString("email"),
                                        jsonObject.getString("mat_no")));
                                if (!(ClassContract.ClassEntry.COLUMN_STAFF_ID.contains(jsonObject.getString("email")))
                                    && (ClassContract.ClassEntry.COLUMN_PASSWORD.contains(jsonObject.getString("password")))) {
                                    getClassHelper.addData(jsonObject.getString("email"), jsonObject.getString("mat_no"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(login.this, "onFailure", Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        });

        //add to RequestQueue
        VolleySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }


    /**
     * Method to check if data exists in the database
     *
     */
    public void checkData () {
                    String uID = staff_id.getText().toString();
                String pass = password.getText().toString();

                List<StaffList> staffLists = getClassHelper.getStaff();

                if (uID.equalsIgnoreCase(staffLists.get(0).getStaffId())
                        && pass.equalsIgnoreCase(staffLists.get(0).getPassword())) {
                    Intent homeIntent = new Intent(login.this, Home.class);
                    startActivity(homeIntent);
                    finish();
                }else {
                    String errMessage = "Wrong StaffId or Password";
                    Toast.makeText(login.this, errMessage, Toast.LENGTH_LONG).show();
                }
    }

}
