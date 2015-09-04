package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EnterGasActivity extends AppCompatActivity {
    private static final String TAG = EnterGasActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entergas);

        Button yourButton = (Button) findViewById(R.id.gas_ok_btn);



        yourButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Getting date
                DatePicker gas_date = (DatePicker) findViewById(R.id.gas_date_field);
                int day = gas_date.getDayOfMonth();
                int month = gas_date.getMonth() + 1;
                int year = gas_date.getYear();

                // Gas cost
                EditText gas_cost_field = (EditText) findViewById(R.id.gas_cost_field) ;
                float gas_cost = Float.parseFloat(gas_cost_field.getText().toString());

                // Gas gallons
                EditText gas_gallons_field = (EditText) findViewById(R.id.gas_gallons_field) ;
                float gas_gallons = Float.parseFloat(gas_gallons_field.getText().toString());

                // Gas odometer
                EditText gas_odometer_field = (EditText) findViewById(R.id.gas_odometer_field) ;
                float gas_odometer = Float.parseFloat(gas_odometer_field.getText().toString());


                Log.d(TAG, "clicked on add! cost:" + gas_cost + ", gallons: " + gas_gallons + "odo: " + gas_odometer);
                AlertDialog.Builder alertdlg = new AlertDialog.Builder(v.getContext());
                alertdlg.setMessage("are you sure?");
                alertdlg.setPositiveButton("Looks Good", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "yes");
                    }
                });
                alertdlg.setNegativeButton("Let me change that", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "no");
                    }
                });
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
