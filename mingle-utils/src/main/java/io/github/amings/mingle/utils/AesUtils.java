package io.github.amings.mingle.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author Ming
 * 
 */

public class AesUtils {

    public static final String AES_OFB_PKCS_5_PADDING = "AES/OFB/PKCS5Padding";
    public static final String AES_CBC_PKCS_7_PADDING = "AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SecretKey generate() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        return generator.generateKey();
    }

    public static SecretKey generate(int KeySize) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KeySize);
        return generator.generateKey();
    }

    public static byte[] generateIv(String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(cipherName);
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        return iv;
    }

    public static byte[] encryption(String cipherName, SecretKey secretKey, String msg, IvParameterSpec ivParameterSpec) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherName);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(msg.getBytes());
    }

    public static byte[] decryption(String cipherName, SecretKey secretKey, byte[] cipherText, IvParameterSpec ivParameterSpec) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherName);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(cipherText);
    }

}
