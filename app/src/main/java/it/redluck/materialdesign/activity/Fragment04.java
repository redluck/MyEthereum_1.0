package it.redluck.materialdesign.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import it.redluck.materialdesign.R;


public class Fragment04 extends Fragment {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver wifiReceiver;
    List<ScanResult> wifiList;
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_04, container, false);

        mainText = (TextView) rootView.findViewById(R.id.mainText);
        mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        //Controlliamo se il wifi è disabilitato
        if (mainWifi.isWifiEnabled() == false) {
            //E in questo caso abilitiamolo
            Toast.makeText(getActivity(), "Wifi is disabled... making it enabled", Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }

        //Istanziamo il BroadcastReceiver
        wifiReceiver = new WifiReceiver();
        //E registriamolo
        getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        mainText.setText("Starting scan...");

        return rootView;
    }



    public void onPause() {
        getActivity().unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    public void onResume() {
        getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    //Il BroadcastReceiver chiama il metodo onReceive() quando il numero delle connessioni wifi cambia
    class WifiReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            stringBuilder = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            stringBuilder.append("\nNumber of wifi connections: " + wifiList.size()+"\n\n");
            for(int i = 0; i < wifiList.size(); i++){
                stringBuilder.append(new Integer(i + 1).toString() + ". ");
                stringBuilder.append((wifiList.get(i)).toString());
                stringBuilder.append("\n\n");
            }
            Log.d("MyLog", wifiList.size() + "");
            mainText.setText(stringBuilder);
        }
    }
}