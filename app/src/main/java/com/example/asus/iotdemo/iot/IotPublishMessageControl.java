package com.example.asus.iotdemo.iot;

import com.example.asus.iotdemo.LogTool;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by zhanghao on 2016/11/12.
 */

public class IotPublishMessageControl {

    /**
     * 发布消息
     *
     * @param topic
     * @param content
     * @param mqttCallback
     */
    public static void publishMessage(String topic, String content, MqttCallback mqttCallback) {
        try {
            MqttClient mqttClient = IotConfig.mqttClient;
            mqttClient.setCallback(mqttCallback);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1);
            mqttClient.publish(topic, message);
            LogTool.d("消息Publish成功");
        } catch (MqttException me) {
            LogTool.d("MQTT连接异常: 异常码 " + me.getReasonCode() + ". 消息:" + me.getMessage()
                    + " . 具体原因:" + me.getCause() + ". 异常:"
                    + me);
        }
    }
}
