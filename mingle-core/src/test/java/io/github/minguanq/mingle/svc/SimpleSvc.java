package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.request.SimpleSvcReq;
import io.github.minguanq.mingle.svc.response.SimpleSvcRes;

import java.util.HashMap;

/**
 * @author Ming
 */
@Svc
public class SimpleSvc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvc(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SimpleSvcRes doService(SimpleSvcReq request) {
        SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
        simpleSvcRes.setText1(request.getText1());
        switch (request.getAction()) {
            case "throw" -> {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("var", "test var");
                throwLogic(SvcResponseHeader.builder(TestUtils.X001).msg("x001-fail {var}").convertMap(hashMap).build(), simpleSvcRes);
            }
            case "throwWithoutMsg" -> {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("var", "test var");
                throwLogic(SvcResponseHeader.builder(TestUtils.X001).convertMap(hashMap).build(), simpleSvcRes);
            }
            case "return" -> {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("var", "test var");
                return returnLogic(SvcResponseHeader.builder(TestUtils.X002).msg("x002-fail {var}").convertMap(hashMap).build(), simpleSvcRes);
            }
            case "returnWithoutMsg" -> {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("var", "test var");
                return returnLogic(SvcResponseHeader.builder(TestUtils.X002).convertMap(hashMap).build());
            }
            case "throwNullException" -> simpleSvcRes.getText2().substring(9);
        }
        simpleSvcRes.setText2(request.getText2());
        return simpleSvcRes;
    }

}
