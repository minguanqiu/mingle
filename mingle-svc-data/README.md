# mingle-svc-data

搭配[spring data jpa](https://spring.io/projects/spring-data-jpa)套件，提供`DAO`相關架構。

## 特點
- `DAO`邏輯層
- 統一的開發流程
- 鏈式日誌記錄

## Getting Started

請先安裝[postgresql](https://www.postgresql.org)並詳閱使用方式。

### Maven設定

```xml
<dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-svc-data</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.7.3</version>
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

啟動後，會拋出錯誤訊息，因為沒有配置`datassource`。

```text
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```

配置屬性：

```yaml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/postgres
    username: ming
    password: postgres
    hikari:
      driver-class-name: org.postgresql.Driver
      minimum-idle: 5
      maximum-pool-size: 10
      auto-commit: true
      idle-timeout: 600000
      max-lifetime: 1800000
      connection-timeout: 60000
```


### java命名規則 (建議)
- {TableName}Dao.java
- {TableName}Entity.java
- {TableName}Repository.java

### package規則 (建議)
```text
com.example
├── DemoApplication.java
├── service(svc)
│   │── dao
│   │   └──── {database name}
│   │         │── entity
│   │             └──── {TableName}Entity.java
│   │         │── repository
│   │             └──── {TableName}Repository.java
│   │         └──── {TableName}Dao.java
```

### 建立DAO

**建立table**
```sql
CREATE TABLE public.test (
    serial character varying(20) NOT NULL,
    text1 character varying(20),
    text2 character varying(20),
    primary key(serial)
);
```

**建立Entity**
```java
@Data
@Entity
@Table(schema = "public", name = "test")
public class TestEntity {

  @Id
  private String serial;

  private String text1;

  private String text2;

}
```

**建立Repository**

```java
public interface TestRepository extends CrudRepository<TestEntity, String> {

}
```

**建立DAO**

```java
@Service
public class TestDao extends JPACrudRepositoryDao<TestRepository, TestEntity, String> {

  public TestDao(TestRepository repository) {
    super(repository);
  }

}
```

**建立Service**

```java
@Getter
@Setter
public class DaoTestSvcReq extends SvcRequest {

  private String serial;

  private String text1;

  private String text2;

}
```

```java
@Setter
@Getter
public class DaoTestSvcRes extends SvcResponseBody {

  private String serial;

  private String text1;

  private String text2;

}
```

```java
@Svc(tags = "svc", summary = "DaoTestSvc", description = "Test dao service")
public class DaoTestSvc extends AbstractService<DaoTestSvcReq, DaoTestSvcRes> {

  @Autowired
  TestDao testDao;

  public DaoTestSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public DaoTestSvcRes doService(DaoTestSvcReq request) {
    testDao.save(testEntity(request));
    Optional<TestEntity> testEntityOptional = testDao.findById("99");
    DaoTestSvcRes daoTestSvcRes = new DaoTestSvcRes();
    if(testEntityOptional.isPresent()) {
      daoTestSvcRes.setSerial(testEntityOptional.get().getSerial());
      daoTestSvcRes.setText1(testEntityOptional.get().getText1());
      daoTestSvcRes.setText2(testEntityOptional.get().getText2());
    }
    return daoTestSvcRes;
  }

  private TestEntity testEntity(DaoTestSvcReq daoTestSvcReq) {
    TestEntity testEntity = new TestEntity();
    testEntity.setSerial(daoTestSvcReq.getSerial());
    testEntity.setText1(daoTestSvcReq.getText1());
    testEntity.setText2(daoTestSvcReq.getText2());
    return testEntity;
  }

}
```

上述流程完成後，發送請求：

```text
URL : http://localhost:8080/svc/DaoTestSvc
Method : POST
```
request body :

```json
{
    "serial": "99",
    "text1": "Hello",
    "text2": "World"
}
```

response body :

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "serial": "99",
        "text1": "Hello",
        "text2": "World"
    }
}
```
完成後，成功新增一筆資料。

```text
postgres=# select * from test;
 serial | text1 | text2 
--------+-------+-------
 99     | Hello | World
(1 筆資料)
```

## 主要組件

### JPARepositoryDao
`DAO`架構，提供隱含變數`repository`方便呼叫。

### JPACrudRepositoryDao
提供`CRUD`基本方法，讓`DAO`無需特別撰寫基本方法。

> [!WARNING]
> 子類必須註冊為`spring bean`。

## 功能

### 日誌記錄
鏈式的紀錄方式，以`service`紀錄為主，如果`service`沒有紀錄`dao`應不該紀錄。
- 序列號
- 類別方法名稱
- 開始、結束時間
- 請求、回應資料
- 執行時間

```text
2024-06-15T16:35:56.581+08:00  INFO 7704 --- [nio-8080-exec-2] g.m.m.s.h.i.SvcLoggingHandlerDefaultImpl : 【Svc Request】: {"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","name":"DaoTestSvc","startDateTime":"2024/06/15 16:35:56","request":{"serial":"99","text1":"Hello","text2":"World"}}
2024-06-15T16:35:56.603+08:00  INFO 7704 --- [nio-8080-exec-2] m.m.s.d.h.i.DaoLoggingHandlerDefaultImpl : 【Dao Request】{"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","actSerialNum":"042d79ec-3e1a-4536-a823-3c3226c873da","name":"TestDao.save","startTime":"2024/06/15 16:35:56","reqBody":{"entity":{"serial":"99","text1":"Hello","text2":"World"}},"type":"dao"}
2024-06-15T16:35:56.634+08:00  INFO 7704 --- [nio-8080-exec-2] m.m.s.d.h.i.DaoLoggingHandlerDefaultImpl : 【Dao Response】{"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","actSerialNum":"042d79ec-3e1a-4536-a823-3c3226c873da","endTime":"2024/06/15 16:35:56","reqBody":{"serial":"99","text1":"Hello","text2":"World"},"runTime":"36 ms"}
2024-06-15T16:35:56.634+08:00  INFO 7704 --- [nio-8080-exec-2] m.m.s.d.h.i.DaoLoggingHandlerDefaultImpl : 【Dao Request】{"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","actSerialNum":"bf486bc7-d0e0-41a7-bafe-2c6792c97674","name":"TestDao.findById","startTime":"2024/06/15 16:35:56","reqBody":{"id":"99"},"type":"dao"}
2024-06-15T16:35:56.637+08:00  INFO 7704 --- [nio-8080-exec-2] m.m.s.d.h.i.DaoLoggingHandlerDefaultImpl : 【Dao Response】{"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","actSerialNum":"bf486bc7-d0e0-41a7-bafe-2c6792c97674","endTime":"2024/06/15 16:35:56","reqBody":{"serial":"99","text1":"Hello","text2":"World"},"runTime":"2 ms"}
2024-06-15T16:35:56.646+08:00  INFO 7704 --- [nio-8080-exec-2] g.m.m.s.h.i.SvcLoggingHandlerDefaultImpl : 【Svc Response】: {"svcSerialNum":"00cefd60-96e7-4c23-b6ba-53551a20acf1","endDateTime":"2024/06/15 16:35:56","response":{"serial":"99","text1":"Hello","text2":"World"},"runTime":"86 ms"}
```

## 配置屬性

下面列出了主要配置屬性及其描述。

| 屬性                                | 預設值         | 描述                                    |
|-------------------------------------|----------------|-----------------------------------------|
| `mingle.svc.dao.logging`            | `false`         | 日誌記錄                             |
