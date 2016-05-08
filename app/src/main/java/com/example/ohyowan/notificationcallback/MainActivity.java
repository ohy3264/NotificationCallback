package com.example.ohyowan.notificationcallback;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText edtKok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edtKok = (EditText)findViewById(R.id.edtkok);

        Button btnTest = (Button)findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kokIntent = new Intent(MainActivity.this, ForegroundService.class);
                kokIntent.putExtra("kok", edtKok.getText().toString());
                kokIntent.setAction(Constants.ACTION.KOK_SAVE_ACTION);
                startService(kokIntent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("hwoh", "fab click");
                Intent service = new Intent(MainActivity.this, ForegroundService.class);
                if (!ForegroundService.IS_SERVICE_RUNNING) {
                    service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    Snackbar.make(view, "Stop Service", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                    Snackbar.make(view, "Start Service", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                startService(service);

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
