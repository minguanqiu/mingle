package io.github.amings.mingle.svc.session.handler;

import io.github.amings.mingle.svc.session.handler.model.JwtEncryptionData;

/**
 * @author Ming
 */
public interface JwtKeyHandler {

    JwtEncryptionData getJwtEncryptionData();

}
