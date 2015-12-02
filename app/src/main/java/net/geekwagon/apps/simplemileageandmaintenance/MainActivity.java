package net.geekwagon.apps.simplemileageandmaintenance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    Button enterinfo_gas_btn;
    Button enterinfo_maint_btn;
    Button enterinfo_repair_btn;
    Button view_upcoming_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up elements.
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

