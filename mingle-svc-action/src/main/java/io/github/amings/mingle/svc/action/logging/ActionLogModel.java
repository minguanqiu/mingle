package io.github.amings.mingle.svc.action.logging;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActionLogModel {

    private String actSerialNum;

    private LocalDateTime startDataTime;

}
