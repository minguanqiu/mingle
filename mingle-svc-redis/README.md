# mingle-svc-redis

搭配[spring data redis](https://spring.io/projects/spring-data-redis)套件，提供`DAO`相關架構。

## 特點
- `DAO`邏輯層
- 統一的開發流程
- `RedisKey`型別保護
- 鏈式日誌記錄

## Getting Started

請先安裝[redis](https://redis.io)並詳閱使用方式。

### Maven設定

```xml
<dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-svc-redis</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.7.3</version>
    </dependency>
</dependency>

</dependencies>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-bom</artifactId>
      <version>2.0.0-SNAPSHOT</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>


<build>
   <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <parameters>true</parameters>
        </configuration>
      </plugin>
    </plugins>
  </build>
```
加入`maven-compiler-plugin`的原因，請參考[Parameter Name Retention](https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x#parameter-name-retention)


### java命名規則 (建議)
- {RedisHashName}Dao.java
- {RedisHashName}Entity.java
- {RedisHashName}Repository.java

### package規則 (建議)
```text
com.example
├── DemoApplication.java
├── service(svc)
│   │── dao
│   │   │── entity
│   │   │   └──── {RedisHashName}Entity.java
│   │   │── repository
│   │   │   └──── {RedisHashName}Repository.java
│   │   └──── {RedisHashName}Dao.java
```

### 建立DAO

**建立Entity**
```java
@Setter
@Getter
@RedisHash("session")
public class SessionEntity extends RedisEntity {

  public SessionEntity(RedisKey redisKey, long timeToLive) {
    super(redisKey, timeToLive);
  }

  private String sessionId;

  private String account;

}
```

**建立Repository**
```java
public interface SessionRepository extends CrudRepository<SessionEntity, RedisKey> {

}
```
**建立DAO**
```java
@Service
public class SessionDao extends RedisCrudRepositoryDao<SessionRepository, SessionEntity> {

  public SessionDao(SessionRepository repository) {
    super(repository);
  }

}
```

**建立Service**

```java
@Getter
@Setter
public class DaoRedisTestSvcReq extends SvcRequest {

  private String account;

}
```

```java
@Setter
@Getter
public class DaoRedisTestSvcRes extends SvcResponseBody {

  private String sessionId;

  private String account;

}
```

```java
@Svc(tags = "svc", summary = "DaoRedisTestSvc", description = "Test redis dao service")
public class DaoRedisTestSvc extends AbstractService<DaoRedisTestSvcReq, DaoRedisTestSvcRes> {

  @Autowired
  SessionDao sessionDao;

  public DaoRedisTestSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public DaoRedisTestSvcRes doService(DaoRedisTestSvcReq request) {
    RedisKey redisKey = RedisKey.builder().addParam(UUID.randomUUID().toString())
        .build();
    sessionDao.save(sessionEntity(request,redisKey));
    Optional<SessionEntity> sessionEntityOptional = sessionDao.findById(redisKey);
    DaoRedisTestSvcRes redisTestSvcRes = new DaoRedisTestSvcRes();
    if(sessionEntityOptional.isPresent()) {
      SessionEntity sessionEntity = sessionEntityOptional.get();
      redisTestSvcRes.setSessionId(sessionEntity.getSessionId());
      redisTestSvcRes.setAccount(sessionEntity.getAccount());
    }
    return redisTestSvcRes;
  }

  private SessionEntity sessionEntity(DaoRedisTestSvcReq daoRedisTestSvcReq,RedisKey redisKey) {
    SessionEntity sessionEntity = new SessionEntity(redisKey,600);
    sessionEntity.setSessionId(UUID.randomUUID().toString());
    sessionEntity.setAccount(daoRedisTestSvcReq.getAccount());
    return sessionEntity;
  }

}
```

上述流程完成後，發送請求：

```text
URL : http://localhost:8080/svc/DaoRedisTestSvc
Method : POST
```
request body :

```json
{
    "account": "mingle"
}
```

response body :

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "sessionId": "2f02f6f2-e3ee-42d2-a706-2b539a518ea7",
        "account": "mingle"
    }
}
```
完成後，成功新增一筆資料。

```text
127.0.0.1:6379> keys *
(1) "session:40ff831e-0f69-457c-b27f-ba5a5f633c04"
```

```text
127.0.0.1:6379> HGETALL "session:40ff831e-0f69-457c-b27f-ba5a5f633c04"
 1) "_class"
 2) "com.example.svc.redis.entity.SessionEntity"
 3) "account"
 4) "mingle"
 5) "redisKey"
 6) "40ff831e-0f69-457c-b27f-ba5a5f633c04"
 7) "sessionId"
 8) "b49e7b1b-e64e-4929-b327-2e9afeac3f1e"
 9) "timeToLive"
10) "600"
```

## 主要組件

### RedisDao
提供`DAO`層邏輯處理
#### RedisTemplateDao
提供`RedisTemplate`功能，存取更為彈性。
- RedisKeyTemplateDao - 提供`RedisKey`的`RedisTemplate`
#### RedisRepositoryDao
提供`Repository`功能，統一實作`CRUD`介面。
- RedisCrudRepositoryDao - 提供預設`CRUD`方法實作。

### RedisEntity
提供`entity`相關功能。
- redisKey - `redis`的`key`物件，統一格式
- timeToLive - 存活時間

### RedisKey
提供組成`redis`的`key`值的物件。

### 功能

### 日誌記錄
鏈式的紀錄方式，以`service`紀錄為主，如果`service`沒有紀錄`dao`應不該紀錄。
- 序列號
- 類別方法名稱
- 開始、結束時間
- 請求、回應資料
- 執行時間

```text
2024-06-15T17:37:51.958+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.h.i.SvcLoggingHandlerDefaultImpl : 【Svc Request】: {"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","name":"DaoRedisTestSvc","startDateTime":"2024/06/15 17:37:51","request":{"account":"mingle"}}
2024-06-15T17:37:51.997+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.r.h.i.RedisLogHandlerDefaultImpl : 【Redis Request】{"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","actSerialNum":"5b244792-e70e-4b13-ab28-5c0861945d90","name":"SessionDao.save","startTime":"2024/06/15 17:37:51","reqBody":{"entity":{"redisKey":{},"timeToLive":600,"sessionId":"b49e7b1b-e64e-4929-b327-2e9afeac3f1e","account":"mingle"}},"type":"redis"}
2024-06-15T17:37:52.199+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.r.h.i.RedisLogHandlerDefaultImpl : 【Redis Response】{"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","actSerialNum":"5b244792-e70e-4b13-ab28-5c0861945d90","endTime":"2024/06/15 17:37:52","reqBody":{"redisKey":{},"timeToLive":600,"sessionId":"b49e7b1b-e64e-4929-b327-2e9afeac3f1e","account":"mingle"},"runTime":"217 ms"}
2024-06-15T17:37:52.200+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.r.h.i.RedisLogHandlerDefaultImpl : 【Redis Request】{"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","actSerialNum":"7751b797-9820-4ba2-b5fa-1a4bc4c1e9a1","name":"SessionDao.findById","startTime":"2024/06/15 17:37:52","reqBody":{"id":{}},"type":"redis"}
2024-06-15T17:37:52.205+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.r.h.i.RedisLogHandlerDefaultImpl : 【Redis Response】{"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","actSerialNum":"7751b797-9820-4ba2-b5fa-1a4bc4c1e9a1","endTime":"2024/06/15 17:37:52","reqBody":{"redisKey":{},"timeToLive":600,"sessionId":"b49e7b1b-e64e-4929-b327-2e9afeac3f1e","account":"mingle"},"runTime":"4 ms"}
2024-06-15T17:37:52.213+08:00  INFO 8329 --- [nio-8080-exec-2] g.m.m.s.h.i.SvcLoggingHandlerDefaultImpl : 【Svc Response】: {"svcSerialNum":"a26537a0-29d3-4f6c-b745-9d4cc5cd17b2","endDateTime":"2024/06/15 17:37:52","response":{"sessionId":"b49e7b1b-e64e-4929-b327-2e9afeac3f1e","account":"mingle"},"runTime":"291 ms"}
```

## 配置屬性

下面列出了主要配置屬性及其描述。

| 屬性                                | 預設值         | 描述                                    |
|-------------------------------------|----------------|-----------------------------------------|
| `mingle.svc.redis.logging`          | `false`         | 日誌記錄                             |
