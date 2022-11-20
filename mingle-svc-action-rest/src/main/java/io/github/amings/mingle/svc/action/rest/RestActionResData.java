package io.github.amings.mingle.svc.action.rest;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.action.ActionResModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
public class RestActionResData<Res extends ActionResModel> extends ActionResData<Res> {

    private String uri;

    private int httpCode;

    private Map<String, List<String>> responseHeaderValue;

}
