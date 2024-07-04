# mingle-core
`service` 是核心概念之一，別名`svc`，基於`spring web`功能，透過HTTP協定接收、回應訊息，獨立且不互相依賴。


## 特點
- 統一使用`json`格式接收及回應。
- 固定的回應樣式。
- 使用回傳狀態碼(code)、狀態訊息(msg)決定成功或失敗，相比`http code`的範圍限制，更加的有彈性。
- 自動的註冊`API`接口。
- 使用`Spring Open API`自動註冊`API`文件。
- 使用`jackson`套件實現序列化、反序列化`POJO`物件。
- 固定的開發流程。
- 鏈式日誌記錄。

## Getting Started

### Maven設定

**設定pom.xml**
```xml
<dependencies>
  <dependency>
    <groupId>io.github.minguanqiu</groupId>
    <artifactId>mingle-core</artifactId>
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
```

### java命名規則 (建議)
- {SvcName}.java - service
- {SvcName}Req.java - request
- {SvcName}Res.java - response body


### package規則 (建議)
```text
com.example
├── DemoApplication.java
├── service(svc)
│   │── request
│   │   └──── DemoSvcReq.java
│   │── response
│   │   └──── DemoSvcRes.java
│   └── DemoSvc.java
```

**建立 spring boot application類**

```java
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(Demo1Application.class, args);
  }

}
```

### 建立service

#### SvcRequest範例
繼承[SvcRequest](#SvcRequest)，並建立接收欄位。

```java
@Setter
@Getter
public class DemoSvcReq extends SvcRequest {

  @NotEmpty
  private String text1;

  @NotEmpty
  private String text2;

}

```

#### SvcResponseBody範例

繼承[SvcResponseBody](#SvcResponseBody)，並建立回應欄位。

```java
@Setter
public class DemoSvcRes extends SvcResponseBody {

  private String text;

}
```

#### Service範例

繼承`AbstractService`並帶入泛型，並且加入`@Svc`去完成[service](#service)註冊，發送請求後，會主動觸發`doService`方法。

```java
@Svc(tags = "demo", summary = "Demo service", description = "Demo service")
public class DemoSvc extends AbstractService<DemoReq, DemoRes> {

  public DemoSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public DemoRes doService(DemoReq demoReq) {
    DemoRes demoRes = new DemoRes();
    demoRes.setText(demoReq.getText1() + " " + demoReq.getText2());
    return demoRes;
  }
}

```

發送請求取得回應：
```text
URL : http://localhost:8080/svc/DemoSvc
Method : POST
```
request body內容：

```json
{
  "text1": "Hello",
  "text2": "World"
}
```
response body：
```json
{
  "code": "0000",
  "msg": "successful",
  "body": {
    "text": "Hello World"
  }
}
```

恭喜您，這樣就簡單完成了。

## 主要組件

介紹`SvcRequest`、`SvcResponseBody`、`Service`組件功能：

### SvcRequest

負責`request body`的處理，`POJO`物件，透過反序列化生成。

- 搭配`jackson`套件使用。
- jakarta.validation進行內容驗證。
- 搭配`springDoc`針對欄位說明。
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位。

### SvcResponseBody

負責`response body`的處理，`POJO`物件，透過序列化轉變為JSON格式。

- 搭配`jackson`套件使用。
- 搭配`springDoc`針對欄位說明。
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位。

### Service

同時擁有`Controller`的功能，透過`@Svc`自動註冊API入口，接收到請求後觸發`doService`方法。

- 搭配`springDoc`針對`API`說明。
- API URL為: `http://localhost:8080/{contextPath}/svc/{SvcName}`
- HTTP Method為`POST`。
- `produces`、`consumes`為`application/json`。
- 使用`@Svc`註冊`API`。
- `doService`方法執行商業邏輯。

> [!WARNING]
> 不允許存在`service`裡設定額外的`API`入口。

## 功能

### @Svc
此註釋為註冊`service`為bean組件，也可以簡單設定`service`說明。

### Spring Open API
搭配`Spring Open API`註冊相關`service`規格，路徑如下：

- springdoc.swagger-ui.path : /swagger-ui.html
- springdoc.api-docs.path : /v3/api-docs

### service功能
提供現成的基本功能，並且快速方便啟用。
- `logging` - 日誌記錄。
- `body-process` - request body的前置處理處理。
- `ip-secure` - IP白名單保護。

以下有幾種設定的方式：

spring env properties全域設定。
```yaml
    mingle:
      svc:
        properties:
         feature:
            logging: false
        body-process: false
        ip-secure:
          - 127.0.0.1
```
@SvcFeature設定，定義在指定`service`類上。
```java
@SvcFeature(logging = true, body_process = false, ip_secure = "127.0.0.1")
```
handler方式設定，實作`SvcFeatureHandler`後，可以單獨設定`service`的各項功能，可以搭配`@Profile`依照環境彈性設定。
```java
@Component
public class SvcFeatureImpl implements SvcFeatureHandler {

  @Override
  public Map<Class<? extends Service<?, ?>>, SvcFeature> getSvcFeature() {
    return Map.of(DemoSvc.class,
        new SvcFeature(false, true, new String[]{"172.0.0.1"}));
  }

}
```

最後，針對DemoSvc功能的設定結果會是 ：

- `logging` - false
- `body_process` - true
- `ip_secure` - 172.0.0.1

覆蓋的順序為上述的 1 < 2 < 3，多種設定方式。

### 日誌記錄
`service`重要的功能之一，將日誌紀錄串連，紀錄相關資訊。
- 序列號
- 類別名稱
- 開始、結束時間
- 請求、回應資料
- 執行時間
```text
【Svc Request】: {"svcSerialNum":"f81b3602-c577-4456-ac94-771d2bc9af53","name":"DemoSvc","startDateTime":"2024/06/10 13:24:51","request":{"text1":"Hello","text2":"World"}}

【Svc Response】: {"svcSerialNum":"f81b3602-c577-4456-ac94-771d2bc9af53","endDateTime":"2024/06/10 13:24:51","response":{"text":"Hello World"},"runTime":"29 ms"}

```


### 邏輯功能
`doService`方法是請求進入點，也是撰寫邏輯的地方，以下說明相關功能。

#### 中斷邏輯
提供中斷邏輯的方法，讓開發人員可以簡單選擇合適的斷點。
- `throwLogic` - 拋出`BreakSvcProcessException`方式中斷。
```java
throwLogic(SvcResponseHeader.builder(“X001").convertMap(hashMap).build(),
        simpleSvcRes);
```
- `returnLogic` - 一般方法return方式中斷。
```java
returnLogic(SvcResponseHeader.builder(“X001").convertMap(hashMap).build(),
        simpleSvcRes);
```

#### 狀態碼、訊息
`service`重要的功能之一，透過狀態碼去決定是否成功，例如：`0000`成功、`X001`失敗。

```json
{
  "code": "0000",
  "msg": "successful",
  "body": {
    "text": "Hello World"
  }
}
```


參考上述範例，`code`、`msg`都是`header`的範圍，可以透過`SvcResponseHeader`決定狀態碼、訊息，如果`msg`為`null`就會透過[CodeMessageListHandler](#CodeMessageListHandler)取得，還可以透過樣板方式轉換變數訊息。

```java
HashMap<String,String> map = new HashMap<>();
map.put("var","test");
SvcResponseHeader.builder("X001")
                .msg("x001-fail {var}")
                .convertMap(hashMap)
                .build()
```
將會把訊息的變數轉換

```json
{
  "code": "X001",
  "msg": "x001-fail test",
  "body": {
    "text": "Hello World"
  }
}
```

### Handler
spring bean，提供架構的組件，允許取代預設邏輯，例如：
#### SvcPathHandler
提供彈性設置`service`的路徑。
```java
public class SvcPathHandlerDefaultImpl implements SvcPathHandler {

  @Override
  public String getPath(Class<?> serviceClass) {
    return "/svc" + "/" + serviceClass.getSimpleName();
  }

}
```
預設是`/svc/{svcName}`，可以修改為
```java
@Component
public class SvcPathHandlerImpl implements SvcPathHandler {

  @Override
  public String getPath(Class<?> serviceClass) {
    return "/testSvc" + "/" + serviceClass.getSimpleName();
  }
}
```
就會變成`/testSvc/{svcName}`，彈性的去針對需求調整。

#### CodeMessageListHandler
提供狀態碼對應的訊息內容，透過回應自動對應並轉換。
```java
@Component
public class CodeMessageListHandlerImpl implements CodeMessageListHandler {

  private final SvcProperties svcProperties;

  public CodeMessageListHandlerImpl(SvcProperties svcProperties) {
    this.svcProperties = svcProperties;
  }

  @Override
  public List<CodeMessage> getMsgList() {
    ArrayList<CodeMessage> codeMessages = new ArrayList<>();
    codeMessages.add(
            svcMsgModel(svcProperties.getMsgType(), "X001", "X001錯誤 {var}"));
    codeMessages.add(
            svcMsgModel(svcProperties.getMsgType(), "X002", "X002錯誤 {var}"));
    return codeMessages;
  }

  public CodeMessage svcMsgModel(String msgType, String code, String msg) {
    return new CodeMessage(msgType, code, msg);
  }

}
```
透過覆蓋`handler`，只要接收到`X001`、`X002`的狀態碼，將會對應`X001錯誤 {var}`、`X002錯誤 {var}`，前提是`SvcResponseHeader`的`msg`必須為`null`才會處理，否則將以塞入的值為主。例如：

```java
throwLogic(SvcResponseHeader.builder("X001")
                .msg("x001-fail {var}")
                .convertMap(hashMap)
                .build(), simpleSvcRes);
```
已經設定`msg`，所以並不會對應，結果會是`x001-fail {var}`而不是`X001錯誤 {var}`。

### Exception Resolver
提供全局的例外捕捉，當`service`或`filter`拋出`MyException`，則將會被捕捉並且回傳`SvcResponseBody`防止非正常回應樣式，註冊範例：

```java
public class MyExceptionHandler extends AbstractExceptionHandler<MyException> {
  @Override
  public ResponseEntity<SvcResponseBody> handle(MyException ex) {
    // Custom exception handling logic
  }
}
```

## 配置屬性

下面列出了主要配置屬性及其描述。

| 屬性                                             | 預設值           | 描述                                    |
|-------------------------------------------------|----------------|-----------------------------------------|
| `mingle.svc.properties.code`                    | `0000`         | 狀態碼，表示成功或失敗                      |
| `mingle.svc.properties.msg`                     | `successful`   | 狀態訊息                                  |
| `mingle.svc.properties.msg-type`                | `svc`          | 狀態訊息類型                               |
| `mingle.svc.properties.feature.logging`         | `false`        | 日誌記錄功能                               |
| `mingle.svc.properties.feature.body-process`    | `false`        | `request body`的前置處理         |
| `mingle.svc.properties.feature.ip-secure`       | `{}`           | IP白名單保護                               |



