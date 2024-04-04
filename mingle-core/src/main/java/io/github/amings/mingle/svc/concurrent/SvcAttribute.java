package io.github.amings.mingle.svc.concurrent;

import lombok.Getter;

import java.util.HashMap;

/**
 * This class provide map to set attributes
 *
 * @author Ming
 */
@Getter
public class SvcAttribute {

    private final HashMap<String,Object> values = new HashMap<>();

    public void setAttributes(String name,Object value) {
        this.values.put(name, value);
    }

    public Object getAttributes(String name) {
        return values.get(name);
    }

    public static class Name {

        public static final String SVC_SERIAL_NUMBER = "SVC_SERIAL_NUMBER";

    }

}
