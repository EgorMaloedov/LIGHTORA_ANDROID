package com.example.lightora.Classes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import java.util.List;

public class BluetoothScaner {
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> bluetoothPoints;
    private Thread scanThread;
    private BroadcastReceiver broadcastReceiver;
    private boolean status = false;

    BluetoothScaner(BluetoothAdapter adapter, BroadcastReceiver broadcastReceiver){
        bluetoothAdapter = adapter;
        this.broadcastReceiver = broadcastReceiver;
    }

    public void startScan(){
        status = true;
        scanThread = new Thread(scanRunnable);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        scanThread.start();
    }

    public void stopScan(){
        status = false;
    }
    public List<BluetoothDevice> getBluetoothPoints() {
        return bluetoothPoints;
    }


    public Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            while (status){
                bluetoothAdapter.startDiscovery();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
