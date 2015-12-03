package net.geekwagon.apps.simplemileageandmaintenance;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ViewUpcomingActivity extends ActionBarActivity {
    private static final String TAG = ViewUpcomingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewupcoming);

        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("Oil Change-0");
        arrayList.add("Wiper Replace-0");
        arrayList.add("Check Tire Pressure-0");
        arrayList.add("Tire Rotation-0");
        arrayList.add("Tire Balance-0");
        arrayList.add("Wheel Alignment-0");
        arrayList.add("Check or Fill Engine Coolant-0");
        arrayList.add("Air Filter-0");

        TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);
        TextView description;
        TextView interval;

        FileInputStream is;
        BufferedReader reader;
        final File file = new File(Environment.getExternalStorageDirectory().toString() + "/smm/maint_data.txt");
        if (file.exists()) {
            try {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                line = reader.readLine(); // skip first line
                while (line != null) {
                    String[] separated = line.split(",");
                    float next = Float.parseFloat(separated[5]);
                    // new string with description-next odometer reading for that item
                    String wut = separated[3].trim() + "-" + (int)next;

                    //iterate through array
                    int index = 0;
                    for(String str : arrayList){

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

                    line = reader.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for(String str : arrayList) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            String[] item = str.split("-");

            if(!item[1].equals("0")) {
                description = new TextView(this);
                description.setText(item[0]);
                interval = new TextView(this);
                interval.setText(item[1]);
                row.addView(description);
                row.addView(interval);
                ll.addView(row);
            }
        }
    }
}