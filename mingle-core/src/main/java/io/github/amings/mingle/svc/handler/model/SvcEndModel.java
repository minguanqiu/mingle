package io.github.amings.mingle.svc.handler.model;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
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
