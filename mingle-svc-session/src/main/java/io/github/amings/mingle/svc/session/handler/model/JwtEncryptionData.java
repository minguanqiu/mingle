package io.github.amings.mingle.svc.session.handler.model;

import com.nimbusds.jose.EncryptionMethod;
import lombok.Data;

/**
 * @author Ming
 */

@Data
public class JwtEncryptionData {

    private EncryptionMethod encryptionMethod;

    private byte[] key;

}
