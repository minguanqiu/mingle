# mingle-svc-session
搭配[spring security](https://spring.io/projects/spring-security)及[mingle-svc-redis](../mingle-svc-redis/README.md)，提供`service`安全性。

## 特點
- 簡單設定驗證、授權功能，保護`service`安全。
- 統一的開發流程

## Getting Started

### Maven設定

```xml
<dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-svc-session</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-bom</artifactId>
      <version>2.0.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### 建立Service

**建立登入Service**

```java
@Svc(tags = "svc", summary = "LoginTestSvc", description = "Test login service")
public class LoginTestSvc extends AbstractService<LoginTestSvcReq, LoginTestSvcRes> {

  @Autowired
  SessionUtils sessionUtils;

  public LoginTestSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public LoginTestSvcRes doService(LoginTestSvcReq request) {
    String sessionId = UUID.randomUUID().toString();
    RedisKey redisKey = RedisKey.builder().addParam(sessionId).build();
    String sessionToken = sessionUtils.createSessionToken(redisKey,
        svcSessionEntity(request, redisKey));
    LoginTestSvcRes loginTestSvcRes = new LoginTestSvcRes();
    loginTestSvcRes.setAccount(request.getAccount());
    loginTestSvcRes.setToken(sessionToken);
    return loginTestSvcRes;
  }

  private SvcSessionEntity svcSessionEntity(LoginTestSvcReq request, RedisKey redisKey) {
    SvcSessionEntity svcSessionEntity = new SvcSessionEntity(redisKey, 600, "login",
        request.getAccount());
    HashMap<String, Object> sessionValue = new HashMap<>();
    sessionValue.put("account", request.getAccount());
    sessionValue.put("text", request.getText());
    svcSessionEntity.setSessionValue(sessionValue);
    svcSessionEntity.setAuthorities(List.of("CheckLoginSvc"));
    return svcSessionEntity;
  }

}
```

完成後發送請求。

```text
URL : http://localhost:8080/svc/LoginTestSvc
Method : POST
```
request body :

```json
{
    "account": "mingle",
    "text": "Hello"
}
```

response body :

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "token": "IoeigcojRGo9ug6pr/vrWyBK0uWr5N9MV8McCLdzR9iQc0rnkKwsgKfJkSlaBQTL0KXJLy/xfFtCLPWd49XMhGckKZs3jdSp+iKrpztm7Qu7SKcM31KhxOhcaJCfwkHMVtw=",
        "account": "mingle"
    }
}
```

**建立驗證登入Service**

加入`@SvcSession`讓`service`啟用驗證授權功能。

```java
@SvcSession(types = "login", authority = true)
@Svc(tags = "svc", summary = "CheckLoginSvc", description = "Test check login service")
public class CheckLoginSvc extends AbstractService<SvcRequest, CheckLoginSvcRes> {

  @Autowired
  SessionUtils sessionUtils;

  public CheckLoginSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public CheckLoginSvcRes doService(SvcRequest request) {
    Map<String, Object> sessionValue = sessionUtils.getCurrentSession().getSessionValue();
    CheckLoginSvcRes checkLoginSvcRes = new CheckLoginSvcRes();
    checkLoginSvcRes.setAccount((String) sessionValue.get("account"));
    checkLoginSvcRes.setText((String) sessionValue.get("text"));
    return checkLoginSvcRes;
  }

}
```

完成後登入回覆的`token`帶入`header`並發送請求，。

```text
URL : http://localhost:8080/svc/CheckLoginSvc
Method : POST
Header :
    token : IoeigcojRGo9ug6pr/vrWyBK0uWr5N9MV8McCLdzR9iQc0rnkKwsgKfJkSlaBQTL0KXJLy/xfFtCLPWd49XMhGckKZs3jdSp+iKrpztm7Qu7SKcM31KhxOhcaJCfwkHMVtw=
```
request body :

```json
{
    "account": "mingle",
    "text": "Hello"
}
```

response body :

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "account": "mingle",
        "text": "Hello"
    }
}
```

## 主要組件

### @SvcSession
提供`service`驗證授權功能。
- types - 驗證種類。
- authority - 是否啟用授權。

### SessionUtils
提供管理`session`的工具。

### SvcSessionEntity
`session`物件，儲存相關資料。
- type - 驗證種類。
- id - 帳號。
- authorities - 授權列表，加入`serivce`名稱。
- sessionValue - `session`暫存資料。

## 配置屬性

下面列出了主要配置屬性及其描述。

| 屬性                                | 預設值         | 描述                                    |
|-------------------------------------|----------------|-----------------------------------------|
| `mingle.svc.session.header`          | `token`         |  session header key                  |


