package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.handler.SvcRequestBodyProcessHandler;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author Ming
 */
public class SvcRequestBodyProcessImpl implements SvcRequestBodyProcessHandler {
    @Override
    public String processBody(String body) {
        return new String(Base64.decodeBase64(body));
    }

}
