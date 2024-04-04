package io.github.amings.mingle.svc.session.handler.impl;

import io.github.amings.mingle.svc.session.handler.TokenKeyHandler;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * {@inheritDoc}
 * Default impl for {@link TokenKeyHandler}
 *
 * @author Ming
 */
public class TokenKeyHandlerImpl implements TokenKeyHandler {

    private final SecretKey key;

    public TokenKeyHandlerImpl() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(256);
        key = keyGenerator.generateKey();
    }

    @Override
    public SecretKey getAesSecretKey() {
        return key;
    }

}
