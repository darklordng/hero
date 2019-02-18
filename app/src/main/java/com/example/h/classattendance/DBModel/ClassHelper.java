package com.example.h.classattendance.DBModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.constraint.ConstraintLayout;
import android.widget.Toast;

import com.example.h.classattendance.Retrofit.StaffList;

import java.util.ArrayList;
import java.util.List;

import static com.example.h.classattendance.DBModel.ClassContract.ClassEntry.COLUMN_COURSE;
import static com.example.h.classattendance.DBModel.ClassContract.ClassEntry.COLUMN_LOGIN_TIMESTAMP;
import static com.example.h.classattendance.DBModel.ClassContract.ClassEntry.COLUMN_MATRIC;
import static com.example.h.classattendance.DBModel.ClassContract.ClassEntry.LOGIN_TABLE_NAME;
import static com.example.h.classattendance.DBModel.ClassContract.ClassEntry.STUD_TABLE_NAME;

public class ClassHelper extends SQLiteOpenHelper {
    //declare database version and name
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "ClassDatabase.db";

    public ClassHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_STAFF_ENTRIES =
            "CREATE TABLE " + LOGIN_TABLE_NAME + " (" +
                    ClassContract.ClassEntry._ID + " INTEGER PRIMARY KEY ," +
                    ClassContract.ClassEntry.COLUMN_STAFF_ID + " TEXT , " +
                    ClassContract.ClassEntry.COLUMN_PASSWORD + " TEXT , " +
                    ClassContract.ClassEntry.COLUMN_LOGIN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP )";


    private static final String SQL_DELETE_STAFF_ENTRIES =
            "DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME;

    private static final String SQL_CREATE_STUD_ENRIES =
            "CREATE TABLE " + ClassContract.ClassEntry.STUD_TABLE_NAME + " (" +
                    ClassContract.ClassEntry._ID + " INTEGER PRIMARY KEY ," +
                    ClassContract.ClassEntry.COLUMN_MATRIC + " TEXT , " +
                    ClassContract.ClassEntry.COLUMN_COURSE + " TEXT , " +
                    ClassContract.ClassEntry.COLUMN_SCAN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP )";

    private static final String SQL_DELETE_STUD_ENTRIES =
            "DROP TABLE IF EXISTS " + ClassContract.ClassEntry.STUD_TABLE_NAME;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STAFF_ENTRIES);
        db.execSQL(SQL_CREATE_STUD_ENRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_STAFF_ENTRIES);
        db.execSQL(SQL_DELETE_STUD_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addData (String staffId, String password) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        /**
         *  'id' and 'timestamp' will be inserted automatically
         *  no need to put them
         */
        values.put(ClassContract.ClassEntry.COLUMN_STAFF_ID, staffId);
        values.put(ClassContract.ClassEntry.COLUMN_PASSWORD, password);

        //insert row
        db.insert(LOGIN_TABLE_NAME, null, values);
        //close the db
        db.close();
    }

    public List<StaffList> getStaff() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " +LOGIN_TABLE_NAME, null);
        List<StaffList> lists = new ArrayList<>();

        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    StaffList staffList = new StaffList();
                    staffList.setStaffid(cursor.getString(cursor.getColumnIndex("staffId")));
                    staffList.setPassword(cursor.getString(cursor.getColumnIndex("password")));

                    lists.add(staffList);

                }while (cursor.moveToNext());
            }
        }
        return lists;
    }

    ArrayList<Students> students = new ArrayList<>();
    public ArrayList<Students> getStudentsData () {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STUD_TABLE_NAME, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Students student = new Students();
                    student.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
                    student.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN_TIMESTAMP)));
                    student.setMatricno(cursor.getString(cursor.getColumnIndex(COLUMN_MATRIC)));

                    students.add(student);
                }while (cursor.moveToNext());
            }
        }
        return students;
    }
}
