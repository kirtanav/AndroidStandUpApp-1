package purdue4992015.independentstudy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.MenuItem;
import android.widget.*;
import java.io.*;
import java.lang.*;


public class Tasks extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Intent intent = getIntent();

        EditText txtBox = (EditText) findViewById(R.id.txtBox);
        String existingtasks = "";
        //System.out.println(txtBox.getText().toString());

        String fileName = "taskFile.txt";
        File file = new File(this.getFilesDir(), fileName);
        BufferedReader br;


        if(file.length()>0) {

            try {
                br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    existingtasks += line + "\n";
                }
                br.close();
                txtBox.setText((CharSequence) existingtasks);

            } catch (Exception e) {
                Toast.makeText(Tasks.this, "Error while reading from file", Toast.LENGTH_SHORT).show();
            }
        }
        txtBox.setSelection(existingtasks.length());
    }
    public void saveTasks(View view) {
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
            Toast.makeText(Tasks.this, "Saved!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            Toast.makeText(Tasks.this, "Error while saving to file", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view)
    {
            openActivity2(view);
    }

    public void openActivity2(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
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
