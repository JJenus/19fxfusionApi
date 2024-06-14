package com.tradeFx.tradeFx.util;

import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

public class Util {
    private static final String password = "mFtex@1234==+";
    private static final String ALGORITHM = "AES";
    private static final String KEY = "+YnPGyVpC7OpiKr3WbZs1A=="; // Replace with your own secret key

    public static long generateToken(int numDigits) {
        SecureRandom secureRandom = new SecureRandom();
        long min = (long) Math.pow(10, numDigits);
        return Math.abs(secureRandom.nextLong() % (1 + min));
    }

    public static CryptoGen generateAddress() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        WalletFile wallet = Wallet.createLight(password, ecKeyPair);

        // Print the address and private key
        String address = "0x" + wallet.getAddress();
        String privateKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());

        return  new CryptoGen(address, privateKey);
    }

    private static SecretKey generateAESKey() {
        try {
            // Create a KeyGenerator instance for AES
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);

            // Generate a random AES key of 128 bits (16 bytes)
            keyGen.init(128);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert a SecretKey to a Base64 encoded string
    private static SecretKey decodeKey(String encodedKey) {
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    private static String encodeKey(SecretKey secretKey) {
        byte[] keyBytes = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static String encrypt(String data) {
        SecretKey key = decodeKey(KEY);
        System.out.println("Key: "+encodeKey(key));
        return  encrypt(data, key);
    }
    // Method to encrypt data using AES
    public static String encrypt(String data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decrypt data using AES
    public static String decrypt(String encryptedData) {
        SecretKey secretKey = decodeKey(KEY);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
