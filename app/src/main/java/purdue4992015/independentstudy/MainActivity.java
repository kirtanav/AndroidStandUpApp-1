package purdue4992015.independentstudy;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class MainActivity extends ActionBarActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor mSensor;

    TextView output; // X, Y and Z axes
    TextView status; // The status
    boolean sitting; // false for standing, true for standing
    Chronometer c;
    Chronometer manager; // Internal chronometer
    long interval; //
    int previous; // 0 for nothing, 1 sitting, 2 standing
    long standUpTime = 15000; // Time to stand up
    long checkInterval = 10000; //Time to check if the user is sitting or standing

    //for testing the interval values
    TextView aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the accelerometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Values of X, Y and Z
        output = (TextView) findViewById(R.id.textView);

        //View objects
        status = (TextView) findViewById(R.id.txtStatus);
        c = (Chronometer) findViewById(R.id.chronometer);

        //Internal chronometer
        manager = (Chronometer) findViewById(R.id.chronometerBG);
        manager.setBase(SystemClock.elapsedRealtime());

        //This variable keeps track of the last state of the app (sitting or standing)
        previous = 0;

        //For testing the interval values
        aa = (TextView)findViewById(R.id.textView2);

        //**********Tasks stuff from this point (imported from Kirtana's file)***********//

      /*  //Read existing tasks from file
        EditText txtBox = (EditText) findViewById(R.id.txtBox);
        String existingtasks = "";
        //System.out.println(txtBox.getText().toString());

        String fileName = "taskFile.txt";
        File file = new File(this.getFilesDir(), fileName);
        BufferedReader br;

        try{
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                existingtasks += line + "\n";
            }
            br.close();
            txtBox.setText((CharSequence)existingtasks);
            //Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            Toast.makeText(MainActivity.this, "Error while reading from file", Toast.LENGTH_SHORT).show();
        }
*/
        //txtBox.setSelection(existingtasks.length()); // What is this???
    }

    public void standUpTime()
    {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    public void manageTimer(boolean verify)
    {
        manager.start();
        interval = SystemClock.elapsedRealtime() - manager.getBase();
        aa.setText(Long.toString(interval));

        if (interval > checkInterval) //to each in intervals of 15s
        {
            if (previous == 0 && verify) //If this is first time the timer is counting and the user is sitting
            {
                previous = 1;
                sitting = true;
                manageStatus();
            }
            else
            {
                if (previous == 0 && !verify) //If this is first time the timer is counting and the user is standing
                {
                    previous = 2;
                    sitting = false;
                    manageStatus();
                }
                else
                {
                    if (previous == 1 && verify) //If the user is siting and he/she was sitting before.
                    {
                        manager.setBase(SystemClock.elapsedRealtime());
                        if (SystemClock.elapsedRealtime() - c.getBase() >= standUpTime)
                        {
                            standUpTime();
                        }
                    }
                    else
                    {
                      if (previous == 1 && !verify) //If the user is standing and he/she was sitting before.
                      {
                          previous = 2;
                          sitting = false;
                          manageStatus();
                          manager.setBase(SystemClock.elapsedRealtime());
                      }
                       else
                      {
                        if (previous == 2 && !verify) //If the user is standing and he/she was standing before
                        {
                          manager.setBase(SystemClock.elapsedRealtime());
                        }
                          else{
                            if (previous == 2 && verify) //If the user is sitting and he/she was standing before
                            {
                                previous = 1;
                                sitting = true;
                                manageStatus();
                                manager.setBase(SystemClock.elapsedRealtime());
                            }
                        }
                      }
                    }
                }
            }
        }

    }

    public void manageStatus()
    {
        if (sitting)
        {
            c.setBase(SystemClock.elapsedRealtime());
            c.start();
            status.setText("SITTING!");
        }

        else
        {
        c.setBase(SystemClock.elapsedRealtime());
        c.start();
        status.setText("STANDING!");
        }
    }


    public void resetTimer(View v)
    {
        c.setBase(SystemClock.elapsedRealtime());
        manager.setBase(SystemClock.elapsedRealtime());
        c.start();
        manager.start();
    }

    //Event handler for SAVE button
   /* public void saveTasks(View view) {
        EditText txtBox = (EditText) findViewById(R.id.txtBox);
        String tasks = txtBox.getText().toString();
        //System.out.println(txtBox.getText().toString());

        String fileName = "taskFile.txt";
        File file = new File(this.getFilesDir(), fileName);
        BufferedWriter bw;

        try{
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(tasks);
            bw.close();
            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            Toast.makeText(MainActivity.this, "Error while saving to file", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void openTasks(View view)
    {
        Intent intent = new Intent(this, Tasks.class);
        startActivity(intent);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        output.setText("X: " + event.values[0] +
         "\n Y: " + event.values[1] +
        "\n Z: " + event.values[2]);

        if ((event.values[1] > 7) || (event.values[1] < -7))
        {
          manageTimer(false);
        }
        else
        {
          manageTimer(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
