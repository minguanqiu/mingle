package io.github.amings.mingle.svc.action.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * For rest action response body common header model determine this action success or fail
 *
 * @author Ming
 */

@Getter
@Setter
public class RestActionResHeaderModel {

    private String successCode;

    private String code;

    private String desc;

}
