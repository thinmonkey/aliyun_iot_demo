/**
 * Alibaba.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.example.asus.iotdemo;

/**
 * 以下参数为设备互联所需信息，可以在控制台查看
 *
 * @version $Id: Config.java,v 0.1 2016年1月21日 下午12:42:38  Exp $
 */
public class Config {

    /**
     * 产品证书的Key，可以在控制台页面上查看
     **/
    public static final String productKey = "xxxxxxxxx";

    /**
     * 产品证书的Secret，可以在控制台页面上查看
     **/
    public static final String productSecret = "xxxxxxxx";

    /**
     * 设备1的名称，可以在设备管理页面上查看
     **/
    public static final String deviceName = "xxxx";

    public static final String deviceId = "xxxxxx";
    
    /**
     * 设备2的名称，可以在设备管理页面上查看
     **/
    public static final String deviceName2 = "O5lWy2ueI7cZQ20r";

    /**
     * 设备1的密钥，可以在设备管理页面上查看
     **/
    public static final String deviceSecret = "DubcjcsvJDwY7sSo";
    
    /**
     * 设备2的密钥，可以在设备管理页面上查看
     **/
    public static final String deviceSecret2 = "llYj7BAmiI9odSX5";

    /**
     * 设备使用的消息topic 可以在设备管理 - 授权页面创建
     **/
    public static final String topic = "/1000098646/action";

    /**
     * 当前证书版本
     **/
    public static final String pkVersion = "2";

    /**
     * 认证服务器地址
     **/
    public static final String authUrl = "http://iot.channel.aliyun.com";

}
