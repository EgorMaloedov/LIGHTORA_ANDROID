package com.example.lightora.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lightora.R;

public class UnsuccesfulConnectionFr extends Fragment {
    TextView goShop = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unseccesful_connection_fr, null);
        goShop = view.findViewById(R.id.go_shop_btn);
        goShop.setOnTouchListener(goShopListener);
        return view;
    }

    public View.OnTouchListener goShopListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Context context = getActivity().getApplication().getApplicationContext();
            TextView tv = (TextView)v;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    tv.setTextColor(ContextCompat.getColor(context, R.color.maincolor_dark));
                    break;
                case MotionEvent.ACTION_UP:
                    tv.setTextColor(ContextCompat.getColor(context, R.color.maincolor));
            }
            return true;
        }
    };


}