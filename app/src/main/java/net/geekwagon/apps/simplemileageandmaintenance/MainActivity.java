package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private boolean resumeHasRun = false;

    Button enterinfo_gas_btn;
    Button enterinfo_maint_btn;
    Button enterinfo_repair_btn;
    Button view_upcoming_btn;

    TextView last_odometer_reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up elements.
        last_odometer_reading = (TextView) findViewById(R.id.display_odometer);
        enterinfo_gas_btn = (Button) findViewById(R.id.enterinfo_gas_btn);
        enterinfo_maint_btn = (Button) findViewById(R.id.enterinfo_maint_btn);
        enterinfo_repair_btn = (Button) findViewById(R.id.enterinfo_repair_btn);
        view_upcoming_btn = (Button) findViewById(R.id.view_upcoming_btn);

        // Add listeners.
        enterinfo_gas_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), EnterGasActivity.class));
            }
        });

        enterinfo_maint_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), EnterMaintActivity.class));
            }
        });

        enterinfo_repair_btn.setEnabled(false);
        enterinfo_repair_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(view.getContext(), EnterRepairActivity.class));
            }
        });

        view_upcoming_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ViewUpcomingActivity.class));
            }
        });


        // Add the place we save maint data if doesn't exist.
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


        CheckStuff();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        }
        CheckStuff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void CheckStuff() {
        TableLayout pastdue = (TableLayout) findViewById(R.id.display_past_due);
        TableLayout soondue = (TableLayout) findViewById(R.id.display_soon_due);
        pastdue.removeAllViews();
        soondue.removeAllViews();
        TextView description;
        TextView interval;

        // check for upcoming maintenance
        FileInputStream isgas;
        BufferedReader readergas;
        final File filegas = new File(Environment.getExternalStorageDirectory().toString() + "/smm/gas_data.txt");

        FileInputStream ismaint;
        BufferedReader readermaint;
        final File filemaint = new File(Environment.getExternalStorageDirectory().toString() + "/smm/maint_data.txt");

        if (filegas.exists() && filemaint.exists()) {
            try {
                // read gas file
                isgas = new FileInputStream(filegas);
                readergas = new BufferedReader(new InputStreamReader(isgas));
                String linegas = readergas.readLine();
                linegas = readergas.readLine(); // skip first line

                // read maint file
                ismaint = new FileInputStream(filemaint);
                readermaint = new BufferedReader(new InputStreamReader(ismaint));
                String linemaint = readermaint.readLine();
                linemaint = readermaint.readLine(); // skip first line

                // make an array of highest odometer reading for each maintenance type
                ArrayList<String> arrayList = new ArrayList();
                arrayList.add("Oil Change-0");
                arrayList.add("Wiper Replace-0");
                arrayList.add("Check Tire Pressure-0");
                arrayList.add("Tire Rotation-0");
                arrayList.add("Tire Balance-0");
                arrayList.add("Wheel Alignment-0");
                arrayList.add("Check or Fill Engine Coolant-0");
                arrayList.add("Air Filter-0");

                while (linemaint != null) {
                    String[] separated = linemaint.split(",");
                    float next = Float.parseFloat(separated[5]);
                    // new string with description-next odometer reading for that item
                    String wut = separated[3].trim() + "-" + (int)next;

                    //iterate through array
                    int index = 0;
                    for(String str : arrayList) {

                        //check if word contains the key
                        String[] check = str.toString().split("-");

                        if(check[0].contains(separated[3].trim())){
                            float curr = Float.parseFloat(check[1]);
                            if(next > curr) {
                                arrayList.set(index, wut);
                            }
                        }

                        index++;
                    }

                    linemaint = readermaint.readLine();
                }


                // Get the last gas entry
                float current_odometer = 0.0f;
                while (linegas != null) {
                    String[] separated = linegas.split(",");
                    float next_odometer = Float.parseFloat(separated[2]);
                    if(next_odometer > current_odometer) {
                        current_odometer = next_odometer;
                    }
                    linegas = readergas.readLine();
                }


                for(String str : arrayList) {
                    String[] check = str.toString().split("-");
                    float maintdue = Float.parseFloat(check[1]);
                    if(maintdue > 0 && maintdue <= current_odometer) {
                        // this item is past due
                        Log.d("TAG", check[0] + " is past due");
                        TableRow row = new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);

                        description = new TextView(this);
                        description.setText(check[0]);
                        interval = new TextView(this);
                        interval.setText(check[1]);

                        description.setWidth(500);

                        row.addView(description);
                        row.addView(interval);
                        pastdue.addView(row);

                    } else if(maintdue > 0 && maintdue < current_odometer + 400) {
                        // this item is past due before next time you get gas
                        Log.d("TAG", check[0] + " needs to be done soon.");
                        TableRow row = new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);

                        description = new TextView(this);
                        description.setText(check[0]);
                        interval = new TextView(this);
                        interval.setText(check[1]);

                        description.setWidth(500);

                        row.addView(description);
                        row.addView(interval);
                        soondue.addView(row);
                    }
                }
                last_odometer_reading.setText("Last Odometer Reading: " + (int) current_odometer);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

