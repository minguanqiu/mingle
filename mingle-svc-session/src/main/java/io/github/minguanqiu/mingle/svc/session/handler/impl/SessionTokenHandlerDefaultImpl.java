package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.session.handler.TokenKeyHandler;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * {@inheritDoc}
 * Default impl for {@link SessionTokenHandler}
 *
 * @author Ming
 */
public class SessionTokenHandlerDefaultImpl implements SessionTokenHandler {

    private final TokenKeyHandler tokenKeyHandler;
    private static final SecureRandom secureRandom = new SecureRandom();
    public static final String TRANSFORMATION = "AES/GCM/NoPadding";
    public static final int TAG_LENGTH = 128;
    public static final int IV_LENGTH = 12;

    public SessionTokenHandlerDefaultImpl(TokenKeyHandler tokenKeyHandler) {
        this.tokenKeyHandler = tokenKeyHandler;
    }

    @Override
    public String encryption(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = generateIV();
        cipher.init(Cipher.ENCRYPT_MODE, tokenKeyHandler.getAesSecretKey(), buildParameterSpec(iv));
        byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] tokenBytes = ByteBuffer.allocate(iv.length + cipherBytes.length)
                .put(iv)
                .put(cipherBytes)
                .array();
        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    @Override
    public String decryption(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(cipherText));
        byte[] iv = new byte[IV_LENGTH];
        byteBuffer.get(iv);
        byte[] cipherContent = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherContent);
        cipher.init(Cipher.DECRYPT_MODE, tokenKeyHandler.getAesSecretKey(), buildParameterSpec(iv));
        return new String(cipher.doFinal(cipherContent), StandardCharsets.UTF_8);
    }

    private GCMParameterSpec buildParameterSpec(byte[] ivBytes) {
        return new GCMParameterSpec(TAG_LENGTH, ivBytes);
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        return iv;
    }

}
