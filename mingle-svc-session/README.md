# mingle-svc-session

透過`redis`提供`session`持久管理，並使用`jwt token`實現`stateless`，達到`authentication`、`authority`功能

## Building

### Install Redis

https://redis.io/  

add pom.xml  

```xml
<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-svc-session</artifactId>
</dependency>
```

### Create Session

使用 `SessionUtils` 產生`jwt token` 

```java
@Svc(desc = "test create session")
public class Test extends AbstractSvcLogic<TestReq, SvcNoRes> {

    @Autowired
    SessionUtils sessionUtils;

    @Override
    public SvcNoRes doService(TestReq reqModel, SvcNoRes resModel) {
        ArrayList<String> keyPrefix = new ArrayList<>();
        keyPrefix.add("user123456");
        String jwt = sessionUtils.createSession(keyPrefix, "Login", Duration.ofMinutes(10));
        svcInfo.getHttpServletResponse().setHeader("session-header", jwt);
        return resModel;
    }

}
```

### Authentication Session

`@Session` 會去驗證`jwt token`是否正確及有效時間

```java
@Session("Login")
@Svc(desc = "test valid session")
public class Test1 extends AbstractSvcLogic<SvcNoReq, SvcNoRes> {

    @Autowired
    SessionUtils sessionUtils;

    @Override
    public SvcNoRes doService(SvcNoReq reqModel, SvcNoRes resModel) {
        sessionUtils.setSessionValue("test1", "1"); // add new data
        return resModel;
    }

}
```

透過 `SessionUtils` 取得及儲存`session value`

### Authority Session

設置`authority` 為`ture`後，代表`session`必須要有`Test1`的`authority`，則可以呼叫此`Svc`

```java
@Session(value = "Login", authority = ture)
@Svc(desc = "test valid session")
public class Test1 extends AbstractSvcLogic<SvcNoReq, SvcNoRes> {

    @Autowired
    SessionUtils sessionUtils;

    @Override
    public SvcNoRes doService(SvcNoReq reqModel, SvcNoRes resModel) {
        Optional<String> userNameOptional = sessionUtils.getSessionValue("userName"); // get session value
        sessionUtils.setSessionValue("UserName1","Mingle1"); // add session value
        return resModel;
    }

}
```

建立`authority`並且加入`Test1`

```java
@Svc(desc = "test create session with authority")
public class Test extends AbstractSvcLogic<TestReq, SvcNoRes> {

    @Autowired
    SessionUtils sessionUtils;

    @Override
    public SvcNoRes doService(TestReq reqModel, SvcNoRes resModel) {
        ArrayList<String> keyParams = new ArrayList<>();
        keyParams.add("user account"); // user id
        ArrayList<String> authorities = new ArrayList<>();
        authorities.add("Test1"); // security authority
        Map<String, Object> sessionValue = new HashMap<>();
        sessionValue.put("UserName", "Mingle"); // set session value
        String token = sessionUtils.createSession(keyParams, "Login", Duration.ofMinutes(10), sessionValue, authorities);
        svcInfo.getHttpServletResponse().setHeader("session-header", token);
        return resModel;
    }

}
```

## Handler

* JwtKeyHandler - 產生`jwt`加密方法

#### JwtKeyHandler

當`serverProperties`啟動，預設將會產生一組新的`aes-256 key`，如要更改請覆蓋此`handler`

## Provide Svc

- RefreshSession - 刷新`session`存活時間

## Exception

Svc scope，請參考[mingle-core](#mingle-core) 實作Exception Handler

- `JwtDecryptionFailException`

- `JwtHeaderMissingException`

- `SessionAccessDeniedException`

- `SessionInfoDeserializeFailException`

- `SessionKickException`

- `SessionNotFoundException`

- `SessionTypeIncorrectException`

## Properties

please watch .A.5. Data Properties `spring.redis` properties  

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#appendix.application-properties.data  
