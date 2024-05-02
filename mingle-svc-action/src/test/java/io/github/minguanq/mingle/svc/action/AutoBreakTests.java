package io.github.minguanq.mingle.svc.action;

import io.github.minguanq.mingle.svc.action.enums.AutoBreak;
import io.github.minguanq.mingle.svc.action.exception.ActionAutoBreakException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest(properties = "mingle.svc.action.auto-break=true")
public class AutoBreakTests {

    @Autowired
    SimpleAction simpleAction;

    @Test
    void testAutoBreak() {
        try {
            SimpleActionReq simpleActionReq = new SimpleActionReq();
            simpleActionReq.setText1("");
            simpleActionReq.setText2("");
            ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
            assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
        } catch (ActionAutoBreakException e) {
            assertThat(e).isNotNull();
            assertThat(e.getActionResponse().getCode()).isEqualTo("X001");
        }
    }

    @Test
    void testAutoBreakReqFalse() {
        try {
            SimpleActionReq simpleActionReq = new SimpleActionReq();
            simpleActionReq.setAutoBreak(AutoBreak.FALSE);
            simpleActionReq.setText1("");
            simpleActionReq.setText2("");
            ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
            assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
        } catch (ActionAutoBreakException e) {
            assertThat(e).isNull();
        }
    }

    @Test
    void testAutoBreakReqTrue() {
        try {
            SimpleActionReq simpleActionReq = new SimpleActionReq();
            simpleActionReq.setAutoBreak(AutoBreak.TRUE);
            simpleActionReq.setText1("");
            simpleActionReq.setText2("");
            ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
            assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
        } catch (ActionAutoBreakException e) {
            assertThat(e).isNotNull();
            assertThat(e.getActionResponse().getCode()).isEqualTo("X001");
        }
    }

}
