/**
 * Alibaba.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.example.asus.iotdemo.iot.util;

import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 签名工具类
 *
 * <p>具体说明</p>
 *
 * @version $Id: SignUtil.java,v 0.1 2016年1月19日 下午4:18:09  Exp $
 */
public class SignUtil {

    /** 签名算法 **/
    public static final String signMethod = "MD5"; // HmacMD5（默认） 或 HmacSHA1 或 MD5

    /**
     * 对参数签名  算法 可选 HmacMD5 或  HmacSHA1 或  MD5（不推荐）
     * @param params
     * @param appsecret
     * @param devicesecret
     * @param hmac
     * @return
     */
    public static String sign(Map<String, String> params, String appSecret, String deviceSecret) {
        //将参数Key按字典顺序排序
        String[] sortedKeys = params.keySet().toArray(new String[] {});
        Arrays.sort(sortedKeys);

        //生成规范化请求字符串
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            if ("sign".equalsIgnoreCase(key)) {
                continue;
            }
            canonicalizedQueryString.append(key).append(params.get(key));
        }

        if ("MD5".equalsIgnoreCase(signMethod)) {
            return Md5.getInstance()
                    .md5_32(appSecret + canonicalizedQueryString.toString() + deviceSecret)
                    .toUpperCase();
        } else {
            try {
                String key = appSecret + deviceSecret;
                return encryptHMAC(canonicalizedQueryString.toString(), key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * HMAC加密 
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHMAC(String content, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), signMethod);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] data = mac.doFinal(content.getBytes("utf-8"));
        return CipherUtils.bytesToHexString(data);
    }

}
