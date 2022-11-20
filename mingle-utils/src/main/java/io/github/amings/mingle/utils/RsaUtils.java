package io.github.amings.mingle.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * publicKey Encryption privateKey Decryption
 *
 * @author Ming
 */

@Slf4j
public class RsaUtils {

    /**
     * RSA encryption
     * @param data - origin data
     * @param key  - encryption key
     * @return byte[]
     */
    public static byte[] encryption(String data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes());
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException e) {
            log.error("", e);
            return null;
        }

    }

    /**
     *
     * @param data - encryption data
     * @param key - decryption key
     * @return byte[]
     */
    public static byte[] decryption(byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * generate key
     * @param keySize - key of size
     * @throws NoSuchAlgorithmException - NoSuchAlgorithmException
     */
    private void generate(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keySize);
        KeyPair keyPair = kpg.generateKeyPair();
        log.info(Base64Utils.encodeToString(keyPair.getPublic().getEncoded()));
        log.info(Base64Utils.encodeToString(keyPair.getPrivate().getEncoded()));
    }

}
