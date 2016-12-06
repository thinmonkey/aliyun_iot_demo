package com.example.asus.iotdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.iotdemo.iot.IotManager;
import com.example.asus.iotdemo.iot.IotPublishMessageControl;
import com.example.asus.iotdemo.iot.IotSubscribeMessageControl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        IotManager.getInstance(this).init();
        //发送消息
        IotPublishMessageControl.publishMessage("xxx", "xxx", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        //订阅消息
        IotSubscribeMessageControl.subscribe("xxx", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
