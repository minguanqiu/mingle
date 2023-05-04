package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Svc logging model
 *
 * @author Ming
 */

@Data
public class SvcBeginModel {

    private HttpServletRequest httpServletRequest;

    private String uuid;

    private String name;

    private LocalDateTime startDateTime;

    private String modelBody;

    private String payloadBody;

    private String ip;

    private boolean valid;

    private boolean back;

}
