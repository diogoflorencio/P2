package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by diogo on 21/05/17.
 */

public class WIFIManager {

    private Context context;
    private static final String WPA = "WPA";
    private static final String WEP = "WEP";
    private static final String OPEN = "Open";
    private android.net.wifi.WifiManager wifiManager;

    public WIFIManager(Context context){
       this.context = context;
        this.wifiManager = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);;
    }

    public boolean isConnected() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else
            return false;
    }

    public void enableWifi() {
        android.net.wifi.WifiManager wifiManager =
                (android.net.wifi.WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public void disableWifi() {
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void requestWIFIConnection(String networkSSID, String networkPass) {
        try {
            //Check ssid exists
            if (scanWifi(networkSSID)) {
                //Security type detection
                String SECURE_TYPE = checkSecurity(networkSSID);
                if (SECURE_TYPE.equals(WPA)) {
                    WPA(networkSSID, networkPass);
                } else if (SECURE_TYPE.equals(WEP)) {
                    WEP(networkSSID, networkPass);
                } else
                    OPEN(networkSSID);
            }
        } catch (Exception e) {
            Log.d("Logger", "Error Connecting WIFI " + e);
        }
    }

    public void WPA(String networkSSID, String networkPass) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + networkSSID + "\"";
        wc.preSharedKey = "\"" + networkPass + "\"";
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        int id = wifiManager.addNetwork(wc);
        wifiManager.disconnect();
        wifiManager.enableNetwork(id, true);
        wifiManager.reconnect();
    }

    private void WEP(String networkSSID, String networkPass) {
        //
    }

    public void OPEN(String networkSSID) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + networkSSID + "\"";
        wc.hiddenSSID = true;
        wc.priority = 0xBADBAD;
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int id = wifiManager.addNetwork(wc);
        wifiManager.disconnect();
        wifiManager.enableNetwork(id, true);
        wifiManager.reconnect();
    }

    public boolean scanWifi(String networkSSID) {
        List<ScanResult> scanList = wifiManager.getScanResults();
        Log.d("Logger", "redes: "+ scanList.size());
        for (ScanResult i : scanList) {
            Log.d("Logger", i.SSID);
            if (i.SSID != null && i.SSID.equals(networkSSID))
                return true;
        }
        return false;
    }

    public String getCurrentSSID() {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private String checkSecurity(String ssid) {
        List<ScanResult> networkList = wifiManager.getScanResults();
        for (ScanResult network : networkList) {
            if (network.SSID.equals(ssid)) {
                String Capabilities = network.capabilities;
                if (Capabilities.contains("WPA")) {
                    return WPA;
                } else if (Capabilities.contains("WEP")) {
                    return WEP;
                } else
                    return OPEN;
            }
        }
        return null;
    }
}
