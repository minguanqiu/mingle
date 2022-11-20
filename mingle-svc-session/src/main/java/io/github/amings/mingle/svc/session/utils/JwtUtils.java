package io.github.amings.mingle.svc.session.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import io.github.amings.mingle.svc.session.handler.JwtKeyHandler;
import io.github.amings.mingle.svc.session.handler.model.JwtEncryptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;

/**
 * Generate jwt utils
 *
 * @author Ming
 */

@Component
public class JwtUtils {

    @Autowired
    JwtKeyHandler jwtKeyHandler;
    private DirectEncrypter directEncrypter;
    private DirectDecrypter directDecrypter;
    private JWEHeader header;

    public String encryptionJWEToken(String body) throws JOSEException {
        Payload payload = new Payload(body);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(directEncrypter);
        return jweObject.serialize();
    }

    public String decryptJWEToken(String token) throws JOSEException, ParseException {
        JWEObject jweObject = JWEObject.parse(token);
        jweObject.decrypt(directDecrypter);
        return jweObject.getPayload().toString();
    }

    @PostConstruct
    private void init() {
        JwtEncryptionData jwtEncryptionData = jwtKeyHandler.getJwtEncryptionData();
        header = new JWEHeader(JWEAlgorithm.DIR, jwtEncryptionData.getEncryptionMethod());
        try {
            directEncrypter = new DirectEncrypter(jwtEncryptionData.getKey());
            directDecrypter = new DirectDecrypter(jwtEncryptionData.getKey());
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }
    }

}
