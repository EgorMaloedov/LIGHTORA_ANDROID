package com.example.lightora.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lightora.Classes.ConnectionDevice;
import com.example.lightora.R;

import java.util.ArrayList;
import java.util.List;

public class devicesFr extends Fragment {
    private LinearLayout devicesListLayout;
    private FragmentTransaction childFragmentTransaction;
    private List<Fragment> devicesInList = new ArrayList<>();
    private ArrayList<ConnectionDevice> wifiPoints;
    private FragmentManager chFragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        devicesListLayout = (LinearLayout) inflater.inflate(R.layout.layout_fr, null);
        return devicesListLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chFragmentManager = getChildFragmentManager();
        childFragmentTransaction = chFragmentManager.beginTransaction();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (devicesListLayout.getChildCount() == 0){
            Log.i("Список", "Список пуст");
            for (ConnectionDevice device: wifiPoints) {
                childFragmentTransaction.add(devicesListLayout.getId(), new connectionDeviceFr(device), device.getBssid());
            }
        }
        childFragmentTransaction.commitNow();
        Log.i("Список", String.valueOf(devicesInList.size()));
    }

    public void setWifiPoints(ArrayList<ConnectionDevice> list) {
        wifiPoints = list;
    }

    public void updateList(){
        Log.i("Статус", "Обновляю");
        chFragmentManager = getChildFragmentManager();
        childFragmentTransaction = chFragmentManager.beginTransaction();

        for (ConnectionDevice wifiPoint: wifiPoints) {
            boolean wifiPointStat = false;

            for (int i = 0; i < devicesListLayout.getChildCount(); i++) {
                View deviceInListView = devicesListLayout.getChildAt(i);
                connectionDeviceFr deviceInListFr = FragmentManager.findFragment(deviceInListView);
                if (deviceInListFr.getTag().equals(wifiPoint.getBssid())) {
                    wifiPointStat = true;
                    break;
                } else
                    wifiPointStat = false;
            }

            if (!wifiPointStat) {
                connectionDeviceFr fr = new connectionDeviceFr(wifiPoint);
                childFragmentTransaction.add(devicesListLayout.getId(), fr, wifiPoint.getBssid());
            }

        }

            for (int i = 0; i < devicesListLayout.getChildCount(); i++){
                View deviceInListView = devicesListLayout.getChildAt(i);
                connectionDeviceFr deviceInListFr = FragmentManager.findFragment(deviceInListView);
                boolean devicesInListStat = false;
                for (ConnectionDevice wifiPoint: wifiPoints){
                    if (deviceInListFr.getTag().equals(wifiPoint.getBssid())) {
                        devicesInListStat = true;
                        break;
                    }
                    else
                        devicesInListStat = false;
                }

                if (!devicesInListStat)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            childFragmentTransaction.remove(deviceInListFr);
                        }
                    });
        }

        childFragmentTransaction.commit();
    }

    public FragmentTransaction getChildFragmentTransaction() {
        return getChildFragmentManager().beginTransaction();
    }

    public LinearLayout getDevicesListLayout() {
        return devicesListLayout;
    }
}

