package com.muxi.workbench.commonUtils;


import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.muxi.workbench.MyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

public class Encryption {

    private static final String KEYSTORE_PROVIDER ="AndroidKeyStore";
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String KEYSTORE_ALIAS = "info_alias";

    private static final String SP_ENCRYPTION="encryption";
    private static final String SP_IV="ase_iv";
    private Cipher mEncryptCipher=null;
    private Cipher mDecryptCipher=null;
    private KeyHelperBelowApi23 keyHelper;
    private SPUtils spUtils;
    private KeyStore mKeyStore;


    public Encryption(){
        spUtils=SPUtils.getInstance(SP_ENCRYPTION);


    }



    public synchronized String encryptAES(String content) throws  Exception {
        if (mEncryptCipher==null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mEncryptCipher=Cipher.getInstance(AES_MODE);
                mEncryptCipher.init(Cipher.ENCRYPT_MODE,getSecretAESKeyApi23());
            }else {
                if (keyHelper==null) {
                    keyHelper = new KeyHelperBelowApi23();
                }
                mEncryptCipher=Cipher.getInstance(AES_MODE);
                mEncryptCipher.init(Cipher.ENCRYPT_MODE,keyHelper.getAESKey());

            }
        }
        spUtils.put(SP_IV,Base64.encodeToString(mEncryptCipher.getIV(),Base64.DEFAULT));
        byte[]encryptedBytes=mEncryptCipher.doFinal(content.getBytes());
        return Base64.encodeToString(encryptedBytes,Base64.DEFAULT);

    }


    public synchronized String decryptAES(String content)throws  Exception{
        byte[]decodeBytes=Base64.decode(content,Base64.DEFAULT);
        if (mDecryptCipher==null){
            mDecryptCipher=Cipher.getInstance(AES_MODE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDecryptCipher.init(Cipher.DECRYPT_MODE,getSecretAESKeyApi23(),new IvParameterSpec(getIv()));
            }else {
                if (keyHelper==null){
                    keyHelper=new KeyHelperBelowApi23();
                }
                mDecryptCipher.init(Cipher.DECRYPT_MODE,keyHelper.getAESKey(),new IvParameterSpec(getIv()));
            }

        }

        return new String(mDecryptCipher.doFinal(decodeBytes));
    }


    private byte[] getIv(){
        String ivd=spUtils.getString(SP_IV);
        byte []iv=null;
        if (ivd.length()==0){
            SecureRandom random=new SecureRandom();
            byte[] generate=random.generateSeed(16);
            ivd= Base64.encodeToString(generate,Base64.DEFAULT);
            spUtils.put(SP_IV,ivd);
            Log.i("TAG", "getIv: "+new String(generate));
            return generate;
        }
        iv=Base64.decode(ivd,Base64.DEFAULT);
        return iv;
    }
    /**
     * AES_MODE = "AES/CBC/PKCS7Padding"
     * api大于23时直接获取 aes key
     *
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey getSecretAESKeyApi23() throws Exception {
        mKeyStore=KeyStore.getInstance(KEYSTORE_PROVIDER);
        mKeyStore.load(null);
        if (mKeyStore.containsAlias(KEYSTORE_ALIAS)){
            return ((KeyStore.SecretKeyEntry)mKeyStore.getEntry(KEYSTORE_ALIAS,null)).getSecretKey();
        }
        final KeyGenerator keyGenerator=KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES,KEYSTORE_PROVIDER);

        keyGenerator.init(new KeyGenParameterSpec.Builder(KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

        return keyGenerator.generateKey();

    }





    /**
     * api23以下用rsa+aes配合
     * api23以下无法直接使用对称加密
     * 所以用非对称+对称式加解密流程
     *  1.使用keystore产生一对rsa key
     *  2.用rsa公匙对aes的key加密存入本地
     *  3.使用时用keystore获取私匙解密得到aes的key
     *
     */
    private  class KeyHelperBelowApi23{


        private KeyStore keyStore;
        private static final String SP_AES="sp_aes";
        public KeyHelperBelowApi23() throws Exception {
            keyStore=KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                generateRSAKey();
            }
        }

        private void generateRSAKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(MyApp.getAppContext())
                    .setAlias(KEYSTORE_ALIAS)
                    .setSubject(new X500Principal("CN=" + KEYSTORE_ALIAS))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                           .getInstance("RSA", KEYSTORE_PROVIDER);

            keyPairGenerator.initialize(spec);
            keyPairGenerator.generateKeyPair();

        }

        private SecretKeySpec getAESKey() throws Exception {
            String encryptAESKey=spUtils.getString(SP_AES);
            byte[]aesKey=null;
            if (encryptAESKey.length()==0){
                aesKey=new byte[16];
                SecureRandom random=new SecureRandom();
                random.nextBytes(aesKey);
                encryptAESKey=encryptRSA(aesKey);
                spUtils.put(SP_AES,encryptAESKey);
                return new SecretKeySpec(aesKey,AES_MODE);
            }
            aesKey=decryptRSA(encryptAESKey);
            return new SecretKeySpec(aesKey,AES_MODE);

        }

        private String encryptRSA(byte[] plainText) throws Exception {
            PublicKey publicKey = keyStore.getCertificate(KEYSTORE_ALIAS).getPublicKey();

            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedByte = cipher.doFinal(plainText);
            return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
        }

        private byte[] decryptRSA(String encryptedText) throws Exception {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEYSTORE_ALIAS, null);

            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);

            return cipher.doFinal(encryptedBytes);
        }




    }








}
