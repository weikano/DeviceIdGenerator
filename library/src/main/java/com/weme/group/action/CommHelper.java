package com.weme.group.action;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.weme.group.utils.DidHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

final class CommHelper {

    static String getEquipmentId() {
        String model = Build.MODEL; // 手机型号
        model = model.replaceAll(" ", "");
        return model.toLowerCase(Locale.getDefault());
    }

    /**
     * 系统版本号
     */
    static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备imei号 山寨机获取不到 返回 na
     *
     * @return
     */
    static String getPhoneImei(Context context) {
        @SuppressLint("HardwareIds") String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            imei = "na";
        }
        return imei;
    }

    /**
     * 获取设备mac地址 没有获取到的话返回 na
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    static String getPhoneMacAddress(Context context) {
        String mac = "";

        InputStreamReader inputStreamReader = null;
        LineNumberReader lineNumberReader = null;
        try {
            @SuppressLint("WifiManagerPotentialLeak") WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            mac = info.getMacAddress();

            if (TextUtils.isEmpty(mac)) {
                Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
                inputStreamReader = new InputStreamReader(process.getInputStream());
                lineNumberReader = new LineNumberReader(inputStreamReader);
                String line = lineNumberReader.readLine();
                if (line != null) {
                    mac = line.trim();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lineNumberReader != null) {
                try {
                    lineNumberReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (TextUtils.isEmpty(mac)) {
            mac = "na";
        }

        return mac;
    }

    private static DeviceInfo info;

    @SuppressLint("HardwareIds")
    static DeviceInfo getDeviceInfo(Context context) {
        if (info == null) {
            synchronized (CommHelper.class) {
                if (info == null) {
                    info = new DeviceInfo();
                    info.type = Build.TYPE;
                    info.version = Build.VERSION.SDK_INT;
                    info.versionName = Build.VERSION.RELEASE;
                    info.orientation = context.getResources().getConfiguration().orientation;

                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    info.mobile = tm.getLine1Number();
                    info.imei = tm.getDeviceId();

                    info.ipAddressIPV4 = DeviceInfo.getIPAddress(true);
                    info.ipAddressIPV6 = DeviceInfo.getIPAddress(false);
                    info.wlanMacAddress = DeviceInfo.getMACAddress("wlan0");
                    info.ethMacAddress = DeviceInfo.getMACAddress("eth0");

                    info.packageName = context.getPackageName();
//                    info.appVersion = AppDefine.APP_VERSION_NAME;
//                    info.channel = AppDefine.CHANNEL;

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    info.netType = 0;
                    if (networkInfo != null) {
                        int type = networkInfo.getType();
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            info.netType = 1;
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            info.netType = 2;
                        }
                    } else {
                        info.netType = 3;
                    }
                    try {
                        info.gameVersion = context.getPackageManager().getPackageInfo(info.packageName, 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    info.pixel = String.format(Locale.getDefault(), "%d*%d", display.getWidth(), display.getHeight());

                    info.did = DidHelper.getInstance(context).getDid();

                }
            }
        }
        return info;
    }

}
