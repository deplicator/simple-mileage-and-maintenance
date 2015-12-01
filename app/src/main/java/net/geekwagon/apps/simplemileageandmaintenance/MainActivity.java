package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;


public class MainActivity extends Activity {

    Button enterinfo_gas_btn;
    Button enterinfo_maint_btn;
    Button enterinfo_repair_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up elements.
        enterinfo_gas_btn = (Button) findViewById(R.id.enterinfo_gas_btn);
        enterinfo_maint_btn = (Button) findViewById(R.id.enterinfo_maint_btn);
        enterinfo_repair_btn = (Button) findViewById(R.id.enterinfo_repair_btn);

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

        enterinfo_repair_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(view.getContext(), EnterMaintActivity.class));
            }
        });



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

}

