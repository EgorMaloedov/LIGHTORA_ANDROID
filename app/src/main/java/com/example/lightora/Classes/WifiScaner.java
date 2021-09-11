package com.example.lightora.Classes;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiScaner {
    private WifiManager wifiManager;
    private List<ScanResult> wifiPoints;
    private Thread scanThread;
    private boolean status = false;
    public WifiScaner(WifiManager wifiManager){
        this.wifiManager = wifiManager;
    }

    public void wifiScanStart(){
        status = true;
        scanThread = new Thread(scanRunnable);
        scanThread.start();
    }

    public void wifiScanStop(){
        status = false;
    }
    public List<ScanResult> getWifiPoints() {
        return wifiPoints;
    }


    public Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            while (status){
               wifiManager.startScan();
               wifiPoints = wifiManager.getScanResults();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
    };

}
