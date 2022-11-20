# mingle-svc-session

Provides session feature and JWT authentication security,and using redis to keep session info

## Dependency

* `mingle-svc-redis`

## Building

#### 1. install redis

https://redis.io/

#### 2. setting pom.xml

add pom.xml

```xml

<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-svc-session</artifactId>
</dependency>
```

#### 3. create session

using `SessionUtils` generate JWT token and save custom session value

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

this example save JWT to response header

#### 4. valid session

```java

@Session("login")
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

`@Session` will auto valid session

using `SessionUtils` can get custom session value and save session value

if authority is true,you must create authority session like :

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

using `SessionUtils` get and set session value object
```java
@Session(value = "login", authority = ture)
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

## Handler

* JwtKeyHandler - generate JWT key

#### JwtKeyHandler

default by server startup,always generate new AES-256 key,if you want change this mode,please override it


## Feature

#### `@Session` - must to configuration will get valid feature

```java

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Session {

    /**
     * valid Session type
     **/
    String value();

    /**
     * valid authority
     **/
    boolean authority() default false;

}
```

#### Provide Svc

- RefreshSession - you can call this Svc to refresh session 

## Properties

please watch .A.5. Data Properties `spring.redis` properties

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#appendix.application-properties.data


## Default System Code Keyword

you can override system code description by `SvcMsgListHandler`

|  Code   |                Description                 |
|:-------:|:------------------------------------------:|
| `MGS20` |               Access denied                |
| `MGS21` |        Missing Authorization Header        |
| `MGS22` |            Decryption JWT fail             |
| `MGS23` |        SessionInfo read value fail         |
| `MGS24` |           Session type incorrect           |
| `MGS25` |             Session not found              |
| `MGS26` | Session has been logout by another session |

