package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


public class EnterGasActivity extends Activity {
    private static final String TAG = EnterGasActivity.class.getSimpleName();

    /**
     * @param datePicker
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

    Button gas_ok_btn;
    DatePicker gas_date;
    EditText gas_gallons_field;
    EditText gas_odometer_field;
    EditText gas_cost_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entergas);

        // Setup elements.
        gas_ok_btn = (Button) findViewById(R.id.gas_ok_btn);

        // Add listeners
        gas_ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                // Date and time of entry
                final Date rightNow = new Date();

                // Getting date - can't not have it
                // (only significantly different from now time if entering in data later)
                gas_date = (DatePicker) findViewById(R.id.gas_date_field);
                final Date gas_entry_date = getDateFromDatePicket(gas_date);
                final int gas_date_day = gas_date.getDayOfMonth();
                final int gas_date_month = gas_date.getMonth() + 1;
                final int gas_date_year = gas_date.getYear();

                // Gas gallons - required
                gas_gallons_field = (EditText) findViewById(R.id.gas_gallons_field) ;
                final String gas_gallons_raw = gas_gallons_field.getText().toString();
                float gas_gallons_number = (float)0.0;
                if(gas_gallons_raw.length() > 0) {
                    gas_gallons_number = Float.parseFloat(gas_gallons_raw);
                }

                // Gas odometer - required
                gas_odometer_field = (EditText) findViewById(R.id.gas_odometer_field);
                final String gas_odometer_raw = gas_odometer_field.getText().toString();
                float gas_odometer_number = (float)0.0;
                if(gas_odometer_raw.length() > 0) {
                    gas_odometer_number = Float.parseFloat(gas_odometer_raw);
                }

                // Gas cost - optional
                gas_cost_field = (EditText) findViewById(R.id.gas_cost_field);
                String gas_cost_raw = "optional";
                if(gas_cost_field.getText().toString().length() != 0) {
                    gas_cost_raw = gas_cost_field.getText().toString();
                }
                float gas_cost_number = (float)0.0;
                if(!gas_cost_raw.equals("optional")) {
                    gas_cost_number = Float.parseFloat(gas_cost_raw);
                }

                if(gas_gallons_number == 0.0) {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Gallons purchased is required.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (gas_odometer_number == 0.0) {
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, "Odometer reading is required.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (gas_cost_number == 0.0) {
                            Context context = getApplicationContext();
                            Toast toast = Toast.makeText(context, "Cost is not required, but nice to have.", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        // Alert to confirm data being entered.
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                        alertDialogBuilder.setTitle("Enter this data?");
                        final String finalGas_cost_raw = gas_cost_raw;
                        AlertDialog.Builder tag = alertDialogBuilder
                            .setMessage(
                                "Confirm this entry:\n" +
                                    "    When: " + gas_date_year + " - " + gas_date_month + "-" + gas_date_day + "\n" +
                                    "    Odometer: " + gas_odometer_raw + "\n" +
                                    "    Gallons: " + gas_gallons_raw + "\n" +
                                    "    Cost/Gallon: " + gas_cost_raw)
                            .setCancelable(false)
                            .setPositiveButton("Looks good!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // The place we save gas data.
                                    File direct = new File(Environment.getExternalStorageDirectory().toString() + "/smm");
                                    File gas_file = new File(Environment.getExternalStorageDirectory().toString() + "/smm/gas_data.txt");

                                    if (!direct.exists()) {
                                        boolean success = direct.mkdir();
                                        if(success)
                                            Log.d("TAG", "sure");
                                        else
                                            Log.d("TAG", "try again");
                                    }

                                    if (!gas_file.exists()) {
                                        try {
                                            gas_file.createNewFile();
                                            FileOutputStream f = new FileOutputStream(gas_file);
                                            InputStream is = getResources().openRawResource(R.raw.gas_data);
                                            byte buf[] = new byte[1024];
                                            int len;
                                            try {
                                                while ((len = is.read(buf)) != -1) {
                                                    f.write(buf, 0, len);
                                                }
                                                f.close();
                                                is.close();
                                            } catch (IOException e) {

                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Log.d("TAG", gas_file.getPath());

                                    String gas_entry = rightNow + ", " + gas_entry_date + ", " + gas_odometer_raw + ", " + gas_gallons_raw + ", " + finalGas_cost_raw + "\n";

                                    FileWriter fw;
                                    try {
                                        fw = new FileWriter(gas_file, true);
                                        fw.write(gas_entry);
                                        fw.flush();
                                        fw.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    EnterGasActivity.this.finish();
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
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gotgas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


