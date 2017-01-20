package com.weme.group.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

final class DeviceInfo {
    String versionName;
    int netType;
    String channel;
    String imei;
    String gameVersion;
    String appVersion;
    String pixel;

    String type;
    String tags;
    String serial;
    String product;
    String manufacture;
    String hardware;
    String fingerprint;
    String display;
    String device;
    String board;
    String model;
    String brand;
    int version;
    int orientation;

    String wlanMacAddress;
    String ethMacAddress;
    String ipAddressIPV4;
    String ipAddressIPV6;
    String mobile;

    String appId;
    String packageName;
    String did;

    JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("os_version", versionName);
            json.put("netType", netType);
            json.put("channel", channel);
            json.put("pixel", pixel);
            json.put("mobile", mobile);
            json.put("imei", imei);
            json.put("gameVersion", gameVersion);
            json.put("appVersion", appVersion);

            json.put("type", type);
            json.put("tags", tags);
            json.put("serial", serial);
            json.put("product", product);
            json.put("manufacture", manufacture);
            json.put("hardware", hardware);
            json.put("fingerprint", fingerprint);
            json.put("display", display);
            json.put("device", device);
            json.put("board", board);
            json.put("model", model);
            json.put("brand", brand);
            json.put("version", version);
            json.put("orientation", orientation);

            json.put("wlanMacAddress", wlanMacAddress);
            json.put("ethMacAddress", ethMacAddress);
            json.put("ipAddressIPV4", ipAddressIPV4);
            json.put("ipAddressIPV6", ipAddressIPV6);

            json.put("appId", appId);
            json.put("packageName", packageName);
            json.put("did", did);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase(Locale.getDefault()) : sAddr.substring(0, delim).toUpperCase(Locale.getDefault());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }
}