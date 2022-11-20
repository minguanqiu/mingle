package io.github.amings.mingle.svc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Utils for Svc response template process
 *
 * @author Ming
 */

@Service
public class SvcResUtils {
    @Autowired
    SvcResModelHandler svcResModelHandler;

    @Autowired
    JacksonUtils jacksonUtils;

    public SvcResModelHandler build(String code, String desc) {
        return build(code, desc, new SvcResModel());
    }

    public SvcResModelHandler build(String code, String desc, SvcResModel svcResModel) {
        SvcResModelHandler svcResModelHandlerImpl = ReflectionUtils.newInstance(svcResModelHandler.getClass());
        svcResModelHandlerImpl.setCode(code);
        svcResModelHandlerImpl.setDesc(desc);
        Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(svcResModel);
        if(jsonNodeOptional.isPresent()) {
            svcResModelHandlerImpl.setResBody(jsonNodeOptional.get());
        } else {
            svcResModelHandlerImpl.setResBody(jacksonUtils.getObjectNode());
        }
        return svcResModelHandlerImpl;
    }

}
