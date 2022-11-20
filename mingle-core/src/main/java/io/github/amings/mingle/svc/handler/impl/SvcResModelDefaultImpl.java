package io.github.amings.mingle.svc.handler.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;

/**
 * {@link SvcResModelHandler} impl
 *
 * @author Ming
 */

@JsonPropertyOrder({"code","desc","resBody"})
public class SvcResModelDefaultImpl extends SvcResModelHandler {

    @JsonProperty("code")
    private String code;
    @JsonProperty("desc")
    private String desc;

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
