package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

    public WIFIManager(Context context){
       this.context = context;
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
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
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
            android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //Check ssid exists
            if (scanWifi(wifiManager, networkSSID)) {
                //Security type detection
                String SECURE_TYPE = checkSecurity(wifiManager, networkSSID);
                if (SECURE_TYPE.equals(WPA)) {
                    WPA(networkSSID, networkPass, wifiManager);
                } else if (SECURE_TYPE.equals(WEP)) {
                    WEP(networkSSID, networkPass);
                } else
                    OPEN(wifiManager, networkSSID);
            }
        } catch (Exception e) {
            Log.d("Logger", "Error Connecting WIFI " + e);
        }
    }

    private void WPA(String networkSSID, String networkPass, android.net.wifi.WifiManager wifiManager) {
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

    private void OPEN(android.net.wifi.WifiManager wifiManager, String networkSSID) {
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

    private boolean scanWifi(android.net.wifi.WifiManager wifiManager, String networkSSID) {
        List<ScanResult> scanList = wifiManager.getScanResults();
        for (ScanResult i : scanList) {
            if (i.SSID != null && i.SSID.equals(networkSSID))
                return true;
        }
        return false;
    }

    public String getCurrentSSID(android.net.wifi.WifiManager wifiManager) {
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

    private String checkSecurity(android.net.wifi.WifiManager wifiManager, String ssid) {
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
