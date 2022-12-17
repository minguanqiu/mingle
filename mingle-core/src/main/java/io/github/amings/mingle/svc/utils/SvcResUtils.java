package io.github.amings.mingle.svc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.filter.SvcInfo;
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
    SvcInfo svcInfo;
    @Autowired
    JacksonUtils jacksonUtils;

    public SvcResModelHandler build(String code, String desc) {
        return build(code, desc, new SvcResModel());
    }

    public SvcResModelHandler build(SvcResModel svcResModel) {
        return build(svcInfo.getCode(), svcInfo.getDesc(), svcResModel);
    }

    public SvcResModelHandler build(String code, String desc, SvcResModel svcResModel) {
        SvcResModelHandler svcResModelHandlerImpl = ReflectionUtils.newInstance(svcResModelHandler.getClass());
        svcResModelHandlerImpl.setCode(code);
        svcResModelHandlerImpl.setDesc(desc);
        if (svcResModel != null) {
            Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(svcResModel);
            jsonNodeOptional.ifPresent(svcResModelHandlerImpl::setResBody);
        }
        return svcResModelHandlerImpl;
    }

}
