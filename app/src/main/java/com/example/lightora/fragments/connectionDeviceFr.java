package com.example.lightora.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lightora.Classes.ConnectionDevice;
import com.example.lightora.R;

public class connectionDeviceFr extends Fragment {
    ConnectionDevice connectionDevice;
    TextView name;
    TextView ssid;
    ImageView connectionImage;
    View statusLine;
    Context context;
    String status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.device_fr, null);
        context = container.getContext();
        name = view.findViewById(R.id.device_name);
        ssid = view.findViewById(R.id.device_ssid);
        connectionImage = view.findViewById(R.id.connection_img);
        statusLine = view.findViewById(R.id.status_line);

        name.setText(connectionDevice.getName());
        ssid.setText(connectionDevice.getSsid());
        connectionImage.setImageDrawable(connectionDevice.getConnectionImg());

        view.setOnClickListener(selectItem);

        return view;
    }

    private View.OnClickListener selectItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
            if (v.isSelected())
                statusLine.setBackground(ContextCompat.getDrawable(context, R.drawable.device_line_selected));
            else
                statusLine.setBackground(ContextCompat.getDrawable(context, R.drawable.device_line_default));
        }
    };

    public connectionDeviceFr(ConnectionDevice device){
        connectionDevice = device;
    }

}
