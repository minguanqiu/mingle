package io.github.amings.mingle.svc.handler.model;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Svc logging model
 *
 * @author Ming
 */

@Data
public class SvcEndModel {

    private HttpServletResponse httpServletResponse;

    private String uuid;

    private LocalDateTime endDateTime;

    private String responseBody;

    private String code;

    private String desc;

    private String runTime;

}
