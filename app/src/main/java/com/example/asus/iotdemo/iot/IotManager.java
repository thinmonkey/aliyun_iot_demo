package com.example.asus.iotdemo.iot;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.asus.iotdemo.Config;
import com.example.asus.iotdemo.LogTool;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * Created by zhanghao on 2016/11/9.
 */

public class IotManager{

    private HandlerThread handlerThread = new HandlerThread("IotManager");
    private Handler handler;

    private static volatile IotManager sInstance;

    private Context context;

    private static byte[] sLock = new byte[0];

    public static IotManager getInstance(Context appCtx) {
        if (sInstance == null) {
            byte[] var1 = sLock;
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new IotManager(appCtx);
                }
            }
        }

        return sInstance;
    }

    private IotManager(Context context) {
        this.context = context;
    }

    public void init() {

        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        //初始化设备客户端和连接
        handler.post(authTask);

        //测试发送消息，这里handle可以暴露给其他模块发送消息
        /*handler.post(new PublishMessageTask(Config.topic, "wo are test message", new MqttCallback
                () {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        }));*/

        //订阅消息
        handler.post(new SubscribeMessageTask(Config.topic, new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                LogTool.d("消息接受失败 =" + throwable.getMessage());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                LogTool.d("接受消息来自topic [" + Config.topic + "] ，content =" + mqttMessage
                        .getPayload());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                LogTool.d("消息接受完成！");
            }
        }));
    }

    public Handler getHandler() {
        return handler;
    }

    private Runnable authTask =
            new Runnable() {
        @Override
        public void run() {
            IotConfig.initConfigInfo(Config.deviceName, Config.deviceSecret);
        }
    };

    /**
     * 发布消息的任务
     */
    public static class PublishMessageTask implements Runnable {

        private String message;
        private MqttCallback mqttCallback;
        private String topic;

        public PublishMessageTask(String topic, String message, MqttCallback mqttCallback) {
            this.topic = topic;
            this.message = message;
            this.mqttCallback = mqttCallback;
        }

        @Override
        public void run() {
            IotPublishMessageControl.publishMessage(topic, message, mqttCallback);
        }
    }

    /**
     * 订阅消息任务
     */
    public static class SubscribeMessageTask implements Runnable{

        private MqttCallback mqttCallback;
        private String topic;

        public SubscribeMessageTask(String topic, MqttCallback mqttCallback) {
            this.topic = topic;
            this.mqttCallback = mqttCallback;
        }

        @Override
        public void run() {
            IotSubscribeMessageControl.subscribe(topic,mqttCallback);
        }

    }


}
