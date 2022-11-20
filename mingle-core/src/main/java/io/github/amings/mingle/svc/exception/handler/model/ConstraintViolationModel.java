package io.github.amings.mingle.svc.exception.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.amings.mingle.svc.SvcResModel;
import lombok.Data;

import java.util.ArrayList;

/**
 * Model for ConstraintViolation data
 *
 * @author Ming
 */

@Data
public class ConstraintViolationModel extends SvcResModel {

    @JsonProperty("Fields")
    private ArrayList<ConstraintViolationField> constraintViolationFields = new ArrayList<>();

    @Data
    public static class ConstraintViolationField {
        @JsonProperty("Field")
        private String Field;
        @JsonProperty("Reason")
        private String Reason;
        @JsonProperty("Value")
        private String Value;
    }

}
