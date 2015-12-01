package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class EnterMaintActivity extends ActionBarActivity {

    private static final String TAG = EnterGasActivity.class.getSimpleName();

    /**
     * @param datePicker a data picker elements
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    DatePicker maint_date;
    Spinner maint_items_dropdown;
    EditText maint_odometer_field;
    EditText maint_interval_field;
    Button maint_ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entermaint);

        // Setup elements.
        maint_date = (DatePicker) findViewById(R.id.maint_date_field);
        maint_items_dropdown = (Spinner) findViewById(R.id.maint_items_spinner);
        maint_odometer_field = (EditText) findViewById(R.id.maint_odometer_field);
        maint_interval_field = (EditText) findViewById(R.id.maint_interval_field);
        maint_ok_btn = (Button) findViewById(R.id.maint_ok_btn);

        // Populate maintenance items dropdown.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.maint_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maint_items_dropdown.setAdapter(adapter);

        // Maintenance drop down listener.
        int current_maint_selection = maint_items_dropdown.getSelectedItemPosition();
        maint_items_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String maint_description_string = maint_items_dropdown.getSelectedItem().toString();
                // get last interval for the line that matches this string and put that value in maint_interval_field.

                FileInputStream is;
                BufferedReader reader;
                final File file = new File(Environment.getExternalStorageDirectory().toString() + "/smm/maint_data.txt");
                maint_interval_field.setText("0");
                if (file.exists()) {
                    try {
                        is = new FileInputStream(file);
                        reader = new BufferedReader(new InputStreamReader(is));
                        String line = reader.readLine();
                        while (line != null) {
                            Log.d("TAG", line);
                            String[] separated = line.split(",");
                            Log.d("TAG", separated[3].trim());
                            Log.d("TAG", maint_description_string.trim());
                            if (separated[3].trim().equals(maint_description_string.trim())) {
                                maint_interval_field.setText(separated[4].trim());
                            }
                            line = reader.readLine();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                Context context = getApplicationContext();
                Toast toast;
                toast = Toast.makeText(context, maint_description_string, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Add button listener
        maint_ok_btn.setOnClickListener(new View.OnClickListener() {

            // date variables
            Date maint_entry_date;
            int maint_date_day;
            int maint_date_month;
            int maint_date_year;

            // maint description drop down
            String maint_description_string = maint_items_dropdown.getSelectedItem().toString();

            // interval variables
            String maint_odometer_raw;
            float maint_odometer_number = 0f;

            // odometer variables
            String maint_interval_raw;
            float maint_interval_number = 0f;

            public void onClick(final View v) {

                // Date and time of entry
                final Date rightNow = new Date();

                // Getting date - can't not have it
                // (only significantly different from now time if entering in data later)
                maint_entry_date = getDateFromDatePicket(maint_date);
                maint_date_day = maint_date.getDayOfMonth();
                maint_date_month = maint_date.getMonth() + 1;
                maint_date_year = maint_date.getYear();

                // Maintenance odometer reading - required
                maint_odometer_raw = maint_odometer_field.getText().toString();
                if (maint_odometer_raw.length() > 0) {
                    maint_odometer_number = Float.parseFloat(maint_odometer_raw);
                }

                // Maintenance next interval - required if not already set.
                maint_interval_raw = maint_interval_field.getText().toString();
                if (maint_interval_raw.length() > 0) {
                    maint_interval_number = Float.parseFloat(maint_interval_raw);
                }

                // Validating
                Context context = getApplicationContext();
                Toast toast;
                if (maint_odometer_number == 0.0) {
                    toast = Toast.makeText(context, "Odometer reading is required.", Toast.LENGTH_SHORT);
                    toast.show();

                } else if (maint_interval_number == 0.0) {
                    toast = Toast.makeText(context, "Interval is required.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    // Alert to confirm data being entered.
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setTitle("Enter this maintenance data?");
                    alertDialogBuilder
                        .setMessage(
                            "Confirm this entry:\n" +
                                "    When: " + maint_date_year + "-" + maint_date_month + "-" + maint_date_day + "\n" +
                                "    What: " + maint_description_string + "\n" +
                                "    Odometer: " + maint_odometer_raw + "\n" +
                                "    Next Occurrence in: " + maint_interval_raw + " miles")
                        .setCancelable(false)
                        .setPositiveButton("Looks good!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // The place we save gas data.
                                File direct = new File(Environment.getExternalStorageDirectory() + "/smm/");
                                File maint_file = new File(Environment.getExternalStorageDirectory().toString() + "/smm/maint_data.txt");
                                if (!direct.exists()) {
                                    direct.mkdir();
                                }

                                if (!maint_file.exists()) {
                                    try {
                                        maint_file.createNewFile();
                                        FileOutputStream f = new FileOutputStream(maint_file);
                                        InputStream is = getResources().openRawResource(R.raw.maint_data);
                                        byte buf[] = new byte[1024];
                                        int len;
                                        try {
                                            while ((len = is.read(buf)) != -1) {
                                                f.write(buf, 0, len);
                                            }
                                            f.close();
                                            is.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.d("TAG", maint_file.getPath());

                                String gas_entry = rightNow + ", " + maint_entry_date + ", " + maint_odometer_raw + ", " + maint_description_string + ", " + maint_interval_raw + "\n";

                                FileWriter fw;
                                try {
                                    fw = new FileWriter(maint_file, true);
                                    fw.write(gas_entry);
                                    fw.flush();
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                EnterMaintActivity.this.finish();
                            }
                        }).setNegativeButton("Nope, let me fix this.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });

    }

}



