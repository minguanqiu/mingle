package io.github.amings.mingle.svc.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Extends to change Svc response template model
 *
 * @author Ming
 */

public abstract class SvcResModelHandler {

    private JsonNode resBody;

    public void setResBody(JsonNode resBody) {
        this.resBody = resBody;
    }

    @JsonProperty("resBody")
    public JsonNode getResBody() {
        return resBody;
    }

    abstract public void setCode(String code);

    abstract public String getCode();

    abstract public void setDesc(String desc);

    abstract public String getDesc();

}
