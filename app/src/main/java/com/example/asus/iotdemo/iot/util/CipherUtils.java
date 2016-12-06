package com.example.asus.iotdemo.iot.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Gary on 14-5-10.
 */

public class CipherUtils {

    private static byte toByte(char c) {

        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToByte(String hex) {

        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    public static final String bytesToHexString(byte[] bArray) {

        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    //Base64解码
    public static byte[] base64De(String base64_msg) throws IOException {

        return Base64Util.decode(base64_msg.replace(" ", ""));
    }

    //Base64编码
    public static String base64En(byte[] byteMsg) throws IOException {

        return Base64Util.encode(byteMsg).replace("\r\n", "");
    }

    //DES密文解密
    public static byte[] desDecrypt(byte[] byteMi, String strKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return doDes(byteMi, strKey, Cipher.DECRYPT_MODE);
    }

    //DES密文解密
    public static byte[] desEncrypt(byte[] byteMi, String strKey)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        return doDes(byteMi, strKey, Cipher.ENCRYPT_MODE);
    }

    //DES加解密
    private static byte[] doDes(byte[] byteData, String strKey, int opmode)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = null;
        try {

            DESKeySpec desKeySpec = new DESKeySpec(hexStringToByte(strKey));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key key = keyFactory.generateSecret(desKeySpec);

            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(opmode, key);

            return cipher.doFinal(byteData);
        } finally {
            cipher = null;
        }
    }

    //AES密文解密
    public static byte[] aesDecrypt(byte[] byteMi, String strKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return doAes(byteMi, strKey, Cipher.DECRYPT_MODE);
    }

    //AES密文加密
    public static byte[] aesEncrypt(byte[] byteMi, String strKey) {

        try {
            return doAes(byteMi, strKey, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return null;
    }

    //AES加解密
    private static byte[] doAes(byte[] byteData, String strKey, int opmode)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = null;
        try {

            SecretKeySpec aesKey = new SecretKeySpec(hexStringToByte(strKey), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(opmode, aesKey);

            return cipher.doFinal(byteData);
        } finally {
            cipher = null;
        }
    }

    //RSA密文解密
    public static byte[] rsaDecrypt(byte[] byteMi, Key pKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return doRsa(byteMi, pKey, Cipher.DECRYPT_MODE);
    }

    //RSA密文加密
    public static byte[] rsaEncrypt(byte[] byteMi, Key pKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return doRsa(byteMi, pKey, Cipher.ENCRYPT_MODE);
    }

    //RSA加解密
    //Key对象为PrivateKey或PublicKey
    private static byte[] doRsa(byte[] byteData, Key pKey, int opmode)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = null;
        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(opmode, pKey);

            return cipher.doFinal(byteData);
        } finally {
            cipher = null;
        }
    }

    //加密数据
    public static String encodeStringWithAES(String data, String strKey) throws Exception {
        String strMi = "";

        byte[] byteMing = null;
        byte[] byteMi = null;

        try {

            byteMing = data.getBytes("UTF8");

            //AES加密
            byteMi = aesEncrypt(byteMing, strKey);

            //BASE64编码，最终密文
            strMi = base64En(byteMi);
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    //解密数据
    public static String decodeStringWithAES(String data, String strKey) throws Exception {
        String strMing = "";

        byte[] byteMing = null;
        byte[] byteMi = null;

        try {

            //BASE64解码
            byteMi = base64De(data);

            //AES解密
            byteMing = aesDecrypt(byteMi, strKey);

            strMing = new String(byteMing, "UTF8");
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }


    //RSA加密KEY
    public static String encodeStringWithRSA(String data, PublicKey pKey) throws Exception {
        String strMi = "";

        byte[] byteMing = null;
        byte[] byteMi = null;

        try {

            byteMing = data.getBytes("UTF8");

            //RSA加密
            byteMi = rsaEncrypt(byteMing, pKey);

            //BASE64编码,最终密文
            strMi = base64En(byteMi);
        } finally {

            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }


    //RSA解密KEY
    public static String decodeStringWithRSA(String data, PrivateKey pKey) throws Exception {
        String strMing = "";

        byte[] byteMing = null;
        byte[] byteMi = null;

        try {

            //BASE64解码
            byteMi = base64De(data);

            //RSA解密
            byteMing = rsaDecrypt(byteMi, pKey);

            strMing = new String(byteMing, "UTF8");
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    //加密数据
    public static byte[] encodeDataWithAES(byte[] data, String strKey) throws Exception {

        byte[] result;


        //AES加密
        result = aesEncrypt(data, strKey);

        return result;
    }

    //解密数据
    public static byte[] decodeDataWithAES(byte[] data, String strKey) throws Exception {

        byte[] result;

        //AES解密
        result = aesDecrypt(data, strKey);

        return result;
    }
    
    /** 
     * 公钥加密 
     * @param data 
     * @param publicKey 
     * @return 
     * @throws Exception 
     */
    public static String encryptByPublicKey(String data, String pk) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf
                .generateCertificate(new ByteArrayInputStream(pk.getBytes()));
            PublicKey publicKey = cert.getPublicKey();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            
            System.out.println("证书有效期："+cert.getNotBefore()+","+cert.getNotAfter());

            //1,将seedkey转16进制数组
            byte[] seedkeyb = CipherUtils.hexStringToByte(data);
            //2,公钥加密
            byte[] b1 = cipher.doFinal(seedkeyb);
            //3,base64
            return Base64Util.encode(b1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * 公钥加密 
     * @param data 
     * @param publicKey 
     * @return 
     * @throws Exception 
     */
    public static byte[] encryptByPublicKey(byte[] data, String pk) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf
                .generateCertificate(new ByteArrayInputStream(pk.getBytes()));
            PublicKey publicKey = cert.getPublicKey();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //1,将seedkey转16进制数组
            byte[] seedkeyb = data;
            //2,公钥加密
            byte[] b1 = cipher.doFinal(seedkeyb);
            //3,base64
            return b1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
