package com.example.asus.iotdemo.iot;

import com.example.asus.iotdemo.LogTool;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by zhanghao on 2016/11/12.
 */

public class IotSubscribeMessageControl {

    public static void subscribe(String topic, MqttCallback mqttCallback){
        try {
            MqttClient mqttClient = IotConfig.mqttClient;
            mqttClient.setCallback(mqttCallback);
            mqttClient.subscribe(topic);
            LogTool.d("订阅消息成功!");
        } catch (MqttException me) {
            LogTool.d("MQTT连接异常: 异常码 " + me.getReasonCode() + ". 消息:" + me.getMessage()
                    + " . 具体原因:" + me.getCause() + ". 异常:"
                    + me);
        }
    }
}
