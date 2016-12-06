package com.example.asus.iotdemo.iot;

import com.example.asus.iotdemo.Config;
import com.example.asus.iotdemo.iot.util.Base64Util;
import com.example.asus.iotdemo.iot.util.IotAuthUtil;
import com.example.asus.iotdemo.iot.util.Md5;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by zhanghao on 2016/11/12.
 */

public class IotConfig {

    public static String targetServer;
    public static byte[] pubKeyByteContent;
    public static String deviceId;

    public static MqttClient mqttClient;

    public static void initConfigInfo(String deciceName, String deviceSecret) {

        System.out.println("开始获取配置信息!");
        try {
            Map<String, String> result = IotAuthUtil.auth(deciceName, deviceSecret);

            //1.1 得到公钥的BASE64编码以后的字符串数据
            String pubKey = result.get("pubkey");

            //1.2 得到连接的目的地IP与端口
            String servers = result.get("servers");
            targetServer = servers.substring(0, servers.indexOf("|"));

            //1.3 得到BASE64字符串解码以后的公钥证书文件
            pubKeyByteContent = Base64Util.decode(pubKey);

            deviceId = result.get("deviceId");

            String broker = "ssl://" + targetServer;
            //客户端ID格式: productKey + deviceId.
            String clientId = Config.productKey + ":" + deviceId;

            MemoryPersistence persistence = new MemoryPersistence();

            mqttClient = new MqttClient(broker, clientId, persistence);

            mqttClient.connect(mqttConnectOptions());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions connOpts = null;
        try {
            connOpts = new MqttConnectOptions();
            SSLSocketFactory socketFactory = createSSLSocket(pubKeyByteContent);
            connOpts.setMqttVersion(4);// MQTT 3.1.1
            connOpts.setSocketFactory(socketFactory);
            String signUserName = signUserName(deviceId);
            connOpts.setUserName(signUserName);
            connOpts.setKeepAliveInterval(65);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connOpts;

    }

    /**
     * 用户名设置规则: ToUpperCase(MD5_32(productKey+productSecret+deviceId+deviceSecret))
     *
     * @param deviceId
     * @return
     */
    private static String signUserName(String deviceId) {
        String sign = Config.productKey + Config.productSecret + deviceId + Config.deviceSecret;
        String signUserName = Md5.getInstance().md5_32(sign).toUpperCase();
        return signUserName;
    }

    private static SSLSocketFactory createSSLSocket(byte[] pubKeyByteContent) throws Exception {
        InputStream is = new ByteArrayInputStream(pubKeyByteContent);
        InputStream caInput = new BufferedInputStream(is);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLSV1.2");
        context.init(null, tmf.getTrustManagers(), null);
        SSLSocketFactory socketFactory = context.getSocketFactory();
        return socketFactory;
    }
}
