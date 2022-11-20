package io.github.amings.mingle.svc.session.handler.impl;

import com.nimbusds.jose.EncryptionMethod;
import io.github.amings.mingle.svc.session.handler.JwtKeyHandler;
import io.github.amings.mingle.svc.session.handler.model.JwtEncryptionData;
import io.github.amings.mingle.utils.AesUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author Ming
 */
public class JwtKeyHandlerDefaultImpl implements JwtKeyHandler {

    @Override
    public JwtEncryptionData getJwtEncryptionData() {
        JwtEncryptionData data = new JwtEncryptionData();
        data.setEncryptionMethod(EncryptionMethod.A256GCM);
        try {
            data.setKey(AesUtils.generate(256).getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
