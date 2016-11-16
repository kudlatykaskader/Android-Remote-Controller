package edu.remotecontroller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.MainThread;
import android.util.Log;
import android.util.MutableDouble;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoteController extends Activity {
    private static final String TAG = "debug";
    ArrayList<String> savedHostArray = new ArrayList<String>();
    ArrayAdapter hostListAdapter;

    public EditText hostName;
    public EditText hostAddr;
    public EditText hostPort;

    ListView savedHostsList;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_controller);
        Log.d(TAG, "On create");

        hostName = (EditText) findViewById(R.id.hostName);
        hostAddr = (EditText) findViewById(R.id.hostAddr);
        hostPort = (EditText) findViewById(R.id.hostPort);

        TextView savedHostsLabel = (TextView) findViewById(R.id.savedHostsLabel);
        final ListView savedHostsList = (ListView) findViewById(R.id.savedHostsList);

        savedHostsLabel.setText("SavedHosts");
        hostListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedHostArray);

        dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<Host> hostArray = dbHandler.getDatabase();
        savedHostArray.clear();
        for(int i = 0; i < hostArray.size(); i++)
        {
            savedHostArray.add(hostArray.get(i).toString());
        }

        hostListAdapter.notifyDataSetChanged();


        //create an Adapter for savedHostArray data

        savedHostsList.setAdapter(hostListAdapter);

        savedHostsList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i(TAG, "Reauesting row number: " + position);
                 ArrayList<Host> hosts = dbHandler.getDatabase();

                 Host host = hosts.get(position);
                 Log.i(TAG, host.toString());
                 hostName.setText(host.getHostName());
                 hostAddr.setText(host.getIpAddress());
                 hostPort.setText(Integer.toString(host.getPort()));
            }
        });
    }

    public void addItemToList(View v) {
        if (hostName.getText().toString().isEmpty() || hostAddr.getText().toString().isEmpty() || hostPort.getText().toString().isEmpty() )
        {
            return;
        }
        Host host = new Host(0, hostName.getText().toString(), hostAddr.getText().toString(), Integer.valueOf( hostPort.getText().toString() ));

        dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<Host> hostArray = dbHandler.getDatabase();
        for(int i = 0; i < savedHostArray.size(); i++)
        {
            Log.i(TAG, "Comparing " + host.getHostName().toString() + " and " +  hostArray.get(i).getHostName());
            if( host.getHostName().toString().equals( hostArray.get(i).getHostName())) {
                Toast.makeText(RemoteController.this, "Desired host name is already taken, please choose another", Toast.LENGTH_LONG).show();
                return;
            }
        }
        hostArray.clear();
        dbHandler.addHost(host);
        Log.i(TAG, "New entry succesfully added");
        Toast.makeText(RemoteController.this, host.toString(), Toast.LENGTH_LONG).show();

        hostArray = dbHandler.getDatabase();

        Log.i(TAG, Integer.toString(hostArray.size()));
        savedHostArray.clear();
        for(int i = 0; i < hostArray.size(); i++)
        {
            savedHostArray.add(hostArray.get(i).toString());
        }


        hostName.getText().clear();
        hostAddr.getText().clear();
        hostPort.getText().clear();

        hostListAdapter.notifyDataSetChanged();
    }

    public void delAllItems(View v) {
        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.dropTable();
        ArrayList<Host> hostArray = dbHandler.getDatabase();
        savedHostArray.clear();
        for(int i = 0; i < hostArray.size(); i++)
        {
            savedHostArray.add(hostArray.get(i).toString());
        }
        hostListAdapter.notifyDataSetChanged();

    }

    public void deleteHost(View v) {
        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.deleteHostByName(hostName.getText().toString());
        savedHostArray.clear();
        ArrayList<Host> hostArray = dbHandler.getDatabase();
        for(int i = 0; i < hostArray.size(); i++)
            savedHostArray.add(hostArray.get(i).toString());
        hostListAdapter.notifyDataSetChanged();
    }

    public void initializeController(View v) {
        if (hostName.getText().toString().isEmpty() || hostAddr.getText().toString().isEmpty() || hostPort.getText().toString().isEmpty() )
        {
            Toast.makeText(RemoteController.this, "No host selected, choose or add one!", Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(getApplicationContext(), ControllerActivity.class);
        i.putExtra("hostName", hostName.getText().toString());
        i.putExtra("hostAddr", hostAddr.getText().toString());
        i.putExtra("hostPort", hostPort.getText().toString());
        startActivity(i);
    }



}


