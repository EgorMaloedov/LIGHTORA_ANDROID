package com.example.lightora.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.example.lightora.Classes.ConnectionDevice;
import com.example.lightora.Classes.WifiScaner;
import com.example.lightora.R;
import com.example.lightora.fragments.UnsuccesfulConnectionFr;
import com.example.lightora.fragments.connectionDeviceFr;
import com.example.lightora.fragments.devicesFr;

import java.util.ArrayList;
import java.util.List;

public class ConnectionAct extends AppCompatActivity {

    //Views
    View decorView = null;
    FrameLayout connectionFr;
    LottieAnimationView wifiChk, bluetoothChk;

    //Lists
    List<ScanResult> wifiPoints = new ArrayList<ScanResult>();
    ArrayList<ConnectionDevice> devices = new ArrayList<>();

    //AnotherVars
    boolean clearLayout = false;
    Context context;
    FragmentManager fragmentManager;
    devicesFr devicesFr = new devicesFr();
    FragmentTransaction fTrans;
    UnsuccesfulConnectionFr frUnsuccess;
    Runnable updateListRunnable;
    WifiManager wifiManager;
    WifiScaner wifiScaner;
    Bundle savedInstance;


    SimpleLottieValueCallback<ColorFilter> lottieValueCallbackDeleteColor = new SimpleLottieValueCallback<ColorFilter>() {
        @Override
        public ColorFilter getValue(LottieFrameInfo frameInfo) {
            return null;
        }
    };

    SimpleLottieValueCallback<ColorFilter> lottieValueCallbackGrayColor = new SimpleLottieValueCallback<ColorFilter>() {
        @Override
        public ColorFilter getValue(LottieFrameInfo frameInfo) {
            return new PorterDuffColorFilter(ContextCompat.getColor(getApplication().getApplicationContext(), R.color.gray), PorterDuff.Mode.SRC_ATOP);
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstance = savedInstanceState;
        context = getApplication().getApplicationContext();
        decorView = getWindow().getDecorView();
        frUnsuccess = new UnsuccesfulConnectionFr();

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        int UiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(UiOptions);

        setContentView(R.layout.connection_act);

        connectionFr = findViewById(R.id.fr_cont);

        wifiChk = findViewById(R.id.wifi_chk);
        bluetoothChk = findViewById(R.id.bluetooth_chk);

        wifiChk.addValueCallback(new KeyPath("front","**"), LottieProperty.COLOR_FILTER, lottieValueCallbackGrayColor);
        bluetoothChk.addValueCallback(new KeyPath("front","**"), LottieProperty.COLOR_FILTER, lottieValueCallbackGrayColor);

        wifiChk.setOnClickListener(onStateChkListener);
        bluetoothChk.setOnClickListener(onStateChkListener);

        wifiScaner = new WifiScaner(wifiManager);
    }

    public final View.OnClickListener onStateChkListener = new View.OnClickListener() {                            //Listener нажатия на значки
        @Override
        public void onClick(View v) {
            clearLayout = false;
            v.setSelected(!v.isSelected());

            Runnable AnimationRunnable = new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override

                public void run() {
                    if (v.isSelected())
                        switch (v.getId()){
                            case R.id.bluetooth_chk:                                                               //Когда нажалт на Bluetooth поиск
                                ActivityCompat.requestPermissions(ConnectionAct.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 1);
                                Log.i("Статус", "Поиск Bluetooth");

                                break;
                            case R.id.wifi_chk:                                                                    //Когда нажали на WiFi поиск
                                ActivityCompat.requestPermissions(ConnectionAct.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 1);
                                wifiScaner.wifiScanStart();

                                Log.i("Статус", "Поиск Wi-Fi");
                        }
                }
            };                                                     //Runnable для нажатия на значки поиска
            updateListRunnable = new Runnable() {
                @Override
                public void run() {
                    while (v.isSelected()){
                        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        wifiPoints = wifiScaner.getWifiPoints();

                        devices.removeAll(devices);

                        for (ScanResult point: wifiPoints){
                            devices.add(new ConnectionDevice("Lightora", point.SSID, point.SSID,ContextCompat.getDrawable(context, R.drawable.wifi_ic)));
                        }

                        if (wifiPoints.isEmpty() && devices.isEmpty()){
                            fTrans.replace(R.id.fr_cont, frUnsuccess);
                            clearLayout = true;
                            wifiScaner.wifiScanStop();
                            v.setSelected(false);
                        }
                        else {
                            devicesFr.setWifiPoints(devices);
                            if (devicesFr.isVisible())
                                devicesFr.updateList();
                            fTrans.replace(R.id.fr_cont, devicesFr);
                        }

                        fTrans.commit();

                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };                                                             //Runnable для оновления списка устройств

            Thread connectDevices = new Thread(AnimationRunnable);
            Thread updateList = new Thread(updateListRunnable);

            if (v.isSelected()){
                connectDevices.start();
                updateList.start();
                if (!((LottieAnimationView)v).isAnimating()){
                    ((LottieAnimationView)v).setSpeed(1f);
                    ((LottieAnimationView) v).addValueCallback(new KeyPath("front","**"), LottieProperty.COLOR_FILTER, lottieValueCallbackDeleteColor);
                    ((LottieAnimationView)v).playAnimation();
                }
            }                                                                               //Запуск поиска и обновления списка + Запуск анимации

            else{
                switch (v.getId()){
                    case R.id.bluetooth_chk:
                        Log.i("Статус", "Поиск Bluetooth остановлен");
                        break;
                    case R.id.wifi_chk:
                        wifiScaner.wifiScanStop();
                        Log.i("Статус", "Поиск Wi-Fi остановлен");
                }
                getLayoutInflater().inflate(R.layout.layout_fr, null);
            }                                                                                              //Остановка поиска и обновления списка
            ((LottieAnimationView) v).addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float progress = (Float) animation.getAnimatedValue() * 100;
                                int intProgress = (int) progress;
                                if (intProgress < 99 && !v.isSelected())
                                    ((LottieAnimationView)v).setSpeed(3f);
                                if (intProgress == 99 && !v.isSelected() || clearLayout) {
                                    ((LottieAnimationView) v).setSpeed(1f);
                                    ((LottieAnimationView) v).setProgress(0f);
                                    ((LottieAnimationView) v).pauseAnimation();
                                    ((LottieAnimationView) v).removeAllAnimatorListeners();
                                    ((LottieAnimationView) v).addValueCallback(new KeyPath("front", "**"), LottieProperty.COLOR_FILTER, lottieValueCallbackGrayColor);
                                    if (devicesFr.isAdded()) {
                                        LinearLayout devicesLayout = devicesFr.getDevicesListLayout();
                                        FragmentTransaction ftrans = devicesFr.getChildFragmentTransaction();
                                        for (int i = 0; i < devicesLayout.getChildCount(); i++) {
                                            connectionDeviceFr connectionDeviceFr = FragmentManager.findFragment(devicesLayout.getChildAt(i));
                                            ftrans.remove(connectionDeviceFr);
                                        }
                                        ftrans.commit();
                                    }
                                }
                            }
                        }); //Listener для анимации (здесь остановка анимации)
        }
    };

    public void onSkipClick(View view) {
        Intent intent = new Intent(ConnectionAct.this, HomeAct.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }
}


