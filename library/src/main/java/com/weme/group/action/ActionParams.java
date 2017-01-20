package com.weme.group.action;

import android.content.Context;
import android.text.TextUtils;

import com.weme.group.utils.DidHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于上报数据提交，默认已经提交了did,equipment_id, os_version, device_info
 */
public class ActionParams {
    private Map<String,Object> params;

    ActionParams(Context context){
        params = new HashMap<>();
        params.put("did", DidHelper.getInstance(context).getDid());
        params.put("equipment_id", CommHelper.getEquipmentId());
        params.put("os_version", CommHelper.getOsVersion());
        params.put("device_info", CommHelper.getDeviceInfo(context).toJSON().toString());
        params.put("type",2);
    }

    ActionParams addParams(String key, Object value){
        if(!TextUtils.isEmpty(key) && value != null){
            params.put(key,value);
        }
        return this;
    }

    Map<String,Object> build(){
        return params;
    }


}
