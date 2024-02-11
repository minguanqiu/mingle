package io.github.amings.mingle.svc.action.rest;

import io.github.amings.mingle.svc.action.ActionResData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter(AccessLevel.PACKAGE)
public class RestActionResData extends ActionResData {

    private String uri;

    private String httpCode;

    private Map<String, List<String>> responseHeaderValue;

}
