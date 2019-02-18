package com.example.h.classattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.classattendance.DBModel.ClassContract;
import com.example.h.classattendance.DBModel.ClassHelper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Activity to read data from an NDEF Tag
 *
 */

public class Scan extends AppCompatActivity {

    TextView autoComplete_TextView, scan_textView;
    public static String MIME_TEXT_PLAIN = "text/plain";
    public static String TAG = "NFC";
    private NfcAdapter nfcAdapter;
    Home home = new Home();
    ClassHelper helper = new ClassHelper(this);

    public AlertDialog.Builder alertDialog;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sync:
                    Intent homeIntent = new Intent(Scan.this, Sync.class);
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
        setContentView(R.layout.activity_scan);

        //alert dialog
        alertDialog = new AlertDialog.Builder(Scan.this);
        //bottom nav
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        autoComplete_TextView = (TextView) findViewById(R.id.auto_course_edit_text);
        String autoComplete = getIntent().getStringExtra("courseEntered");
        autoComplete_TextView.setText(autoComplete);
        //nfc
        scan_textView = (TextView) findViewById(R.id.text_view_nfc_explanation);
        String res = scan_textView.getText().toString();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //the app will not proceed further, we need NFC
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "NFC is disabled", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "NFC is enabled", Toast.LENGTH_LONG).show();
        }
        handleIntent(getIntent());

    }

    @Override
    protected void onResume () {
        super.onResume();
        /**
         * method to call setupForeGroundDispatch method
         *
         */
        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause () {
        /**
         * method to call stopForeGroundDispatch
         *
         */
        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent (@NonNull Intent intent) {
        /**
         * method to handle intent
         */

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            }else {
                Log.d(TAG,"Wrong mime type: " + type );
            }
        }else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String [] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            //using a foreach loop
            for (String tech: techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(),
                0, intent, 0);

        IntentFilter [] filters = new IntentFilter[1];
        String [][] techList = new String[][] {} ;

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        }catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch (final Activity activity, @NonNull NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }




    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        /**
         * Background task for reading data.
         * NOTE: Do not block the UI thread while reading
         *
         */

        @Override
        protected String doInBackground (Tag...params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF not supported by this card
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord [] ndefRecord = ndefMessage.getRecords();

            for (NdefRecord record : ndefRecord) {
                if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                        Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(record);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding ", e);
                    }

                }
            }
            return null;
        }

        private String readText(NdefRecord records) throws UnsupportedEncodingException {

            byte [] payload = records.getPayload();

            //get the text encoding
            String textEncoding = ((payload[0] & 128)==0) ? "UTF-8" : "UTF-16";

            //get the language code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"
            //get the text
            return new String(payload, languageCodeLength + 1,
                    payload.length-languageCodeLength-1, textEncoding);
        }
        /**
         * method to display the text
         *
         */

        @Override
        protected void onPostExecute (String result) {
            if (result != null) {
                //Toast.makeText(Scan.this, "Success", Toast.LENGTH_LONG).show();
                scan_textView.setText(result);
                addRecords();
            }
        }



        public void addRecords () {
            // get writable database as we want to write data
            SQLiteDatabase db = helper.getWritableDatabase();


            ContentValues values = new ContentValues();
            /**
             *  'id' and 'timestamp' will be inserted automatically
             *  no need to put them
             */
            values.put(ClassContract.ClassEntry.COLUMN_MATRIC, scan_textView.getText().toString());
            values.put(ClassContract.ClassEntry.COLUMN_COURSE, autoComplete_TextView.getText().toString());

            //insert row
            db.insert(ClassContract.ClassEntry.STUD_TABLE_NAME, null, values);
            //close the db
            db.close();
        }


    }


    /**
     * Method to show alert dialog
     */
    public void showAlertDialog () {
        //set title
        alertDialog.setTitle("Logout");
        //set Message
        alertDialog.setMessage("Are you sure you want to logout?");
        //set icon
        alertDialog.setIcon(R.drawable.alert_logout);
        //set positive YES button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent scanIntent = new Intent(Scan.this, Sync.class);
                startActivity(scanIntent);
                finish();
            }
        });
        //set negative button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }
}


