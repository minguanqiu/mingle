package io.github.minguanqiu.mingle.svc;

import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import java.util.HashMap;

/**
 * @author Qiu Guan Ming
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
        throwLogic(
            SvcResponseHeader.builder(SvcTestUtils.X001).msg("x001-fail {var}").convertMap(hashMap)
                .build(), simpleSvcRes);
      }
      case "throwWithoutMsg" -> {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("var", "test var");
        throwLogic(SvcResponseHeader.builder(SvcTestUtils.X001).convertMap(hashMap).build(),
            simpleSvcRes);
      }
      case "throwWithoutBody" -> {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("var", "test var");
        throwLogic(SvcResponseHeader.builder(SvcTestUtils.X001).convertMap(hashMap).build());
      }
      case "return" -> {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("var", "test var");
        return returnLogic(
            SvcResponseHeader.builder(SvcTestUtils.X002).msg("x002-fail {var}").convertMap(hashMap)
                .build(), simpleSvcRes);
      }
      case "returnWithoutMsg" -> {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("var", "test var");
        return returnLogic(
            SvcResponseHeader.builder(SvcTestUtils.X002).convertMap(hashMap).build());
      }
      case "throwNullException" -> simpleSvcRes.getText2().substring(9);
      case "unknownCode" ->
          throwLogic(SvcResponseHeader.builder(SvcTestUtils.X003).build(), simpleSvcRes);
    }
    simpleSvcRes.setText2(request.getText2());
    return simpleSvcRes;
  }

}
