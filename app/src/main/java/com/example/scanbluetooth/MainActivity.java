package com.example.scanbluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;

    Button btnOnOff;
    Button btnScan;
    ListView lScan;

    ArrayList<String> mArrayAdapter;
    ArrayAdapter adapter;
    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOnOff = (Button) findViewById(R.id.btnO);
        btnScan = (Button) findViewById(R.id.buttonScan);
        lScan = (ListView) findViewById(R.id.listScan);

        mArrayAdapter = new ArrayList<>();
        mArrayAdapter.add("Dong 1");
        mArrayAdapter.add("Dong 2");
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mArrayAdapter);
        lScan.setAdapter(adapter);

        //  mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBlueAdapter.isEnabled()){
                    //showToast("Bluetooth is already off");
                    showToast("Turning On Bluetooth....");
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }

                else if (mBlueAdapter.isEnabled()){
                    // showToast("Bluetooth is already on");
                    mBlueAdapter.disable();
                    showToast("Turing Bluetooth off");
                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBluetooth(true);
            }
        });
    }

    private void scanBluetooth(final boolean enable){
        mArrayAdapter.clear();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLE_BT);

        //showToast("Bluetooth scanning...");
        mArrayAdapter.add("abe");
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent1) {
                showToast("Bluetooth scanning...");
                String action = intent1.getAction();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getAddress());
                }
                else{
                    showToast("No device");
                    mArrayAdapter.add("No");
                }
            }
        };
        mArrayAdapter.add("xyz");
        adapter.notifyDataSetChanged();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        mBlueAdapter.startDiscovery();
    }


    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}