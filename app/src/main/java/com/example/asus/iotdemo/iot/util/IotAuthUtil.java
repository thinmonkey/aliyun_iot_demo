/**
 * Alibaba.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.example.asus.iotdemo.iot.util;

import com.example.asus.iotdemo.Config;

import java.util.HashMap;
import java.util.Map;


/**
 * 总体说明
 *
 * <p>具体说明</p>
 *
 * @version $Id: IotAuthUtil.java,v 0.1 2016年3月30日 下午8:14:38  Exp $
 */
public class IotAuthUtil {
    
    /**
     * (1)调用设备配置接口初始化设备，服务器返回公钥以及数据网关服务器地址
     * @param
     * @param
     * @throws Exception
     */
    public static Map<String, String> auth(String deviceName, String deviceSecret) throws Exception {
        //1,填充参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceName", deviceName);
        params.put("productKey", Config.productKey);
        params.put("signMethod", SignUtil.signMethod);
        params.put("protocol", "mqtt");
        //2,生成参数签名
        params.put("sign", SignUtil.sign(params, Config.productSecret, deviceSecret));

        //3,调用配置认证接口
        String result = AliyunWebUtils.doGet(Config.authUrl + "/iot/auth", params);
        System.out.println("调用设备授权认证接口：" + result);

        //解析结果
        Map<String, String> json = result2map(result);

        System.out.println("调用设备授权认证接口-servers：" + json.get("servers"));
        if (json.containsKey("pubkey")) {
            System.out.println("认证成功");
        } else {
            System.out.println("认证失败");
            return null;
        }

        return json;

    }
    
    /**
     * 简单的json字符串解析
     * @param result
     * @return
     */
    public static Map<String, String> result2map(String result) {
        String[] jsonarray = result.replace("{", "").replace("}", "").replaceAll("[\\n]", "")
            .split(",");
        Map<String, String> json = new HashMap<String, String>();
        for (String item : jsonarray) {
            String[] keyvalue = item.split("\":");
            json.put(keyvalue[0].replaceAll("\"", "").trim(), keyvalue[1].replaceAll("\"", "")
                .trim());
        }
        return json;
    }

}
