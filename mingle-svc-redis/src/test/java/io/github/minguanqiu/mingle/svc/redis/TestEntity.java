package io.github.minguanqiu.mingle.svc.redis;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Qiu Gaun Ming
 */
@Getter
@Setter
public class TestEntity implements Serializable{


  public String text = "Hello";

  public Test1 test1 = new Test1();

  @Getter
  @Setter
  public static class Test1 implements Serializable {

    public String text = "he";

  }

}
