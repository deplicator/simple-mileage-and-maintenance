package net.geekwagon.apps.simplemileageandmaintenance;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewUpcomingActivity extends ActionBarActivity {
    private static final String TAG = ViewUpcomingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewupcoming);


        TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);
        TextView tv;
        TextView qty;


        for (int i = 0; i <5; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            tv = new TextView(this);
            qty = new TextView(this);
            qty.setText("10");
            row.addView(qty);
            ll.addView(row,i);
        }


    }
}