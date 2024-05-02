package io.github.minguanq.mingle.svc.action;

import io.github.minguanq.mingle.svc.action.configuration.properties.SvcActionProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest
public class ActionTests {

    @Autowired
    private SimpleAction simpleAction;
    @Autowired
    private SvcActionProperties svcActionProperties;

    @Test
    void testAction() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("Hello");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo(svcActionProperties.getCode());
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo(svcActionProperties.getMsg());
        assertThat(simpleActionResActionResponse.getResponseBody().isPresent()).isTrue();
        SimpleActionRes simpleActionRes = simpleActionResActionResponse.getResponseBody().get();
        assertThat(simpleActionRes.getText1()).isEqualTo("Hello");
        assertThat(simpleActionRes.getText2()).isEqualTo("World");
    }

    @Test
    void testActionError() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("");
        simpleActionReq.setText2("");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo("Text1 is empty");
        assertThat(simpleActionResActionResponse.getResponseBody().isPresent()).isFalse();
    }
}
