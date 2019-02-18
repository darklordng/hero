package com.example.h.classattendance.DBModel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class utils {

    public static boolean isNetworkAvailable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
}
