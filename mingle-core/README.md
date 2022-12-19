# mingle-core

mingle核心，使用`Service`和`Action`設計概念，簡單的去打造自己的商業邏輯

## Service

別名`Svc`，是Restful API的入口，也是編寫商業邏輯的地方，每個`Service`都是獨立並且不互相依賴

##### Feature

* 使用回傳代碼決定成功或失敗，比起http code判斷範圍更彈性

* 自動註冊Restful API入口

* 自動註冊Spring open API Spec

* Request和Response皆使用pojo model設計

* 透過`@Svc`簡單設定Service狀態

* 日誌紀錄

## Action

抽象類，可以當作模組或零件，所有類型的事物都可以變成`Action`

參考mingle-svc-action-rest模組 - 提供Http client功能，實現`Action`概念之一

##### Feature

* 使用回傳代碼決定成功或失敗

* Request和Response皆使用pojo model設計

* AOP日誌紀錄

* 線程安全

## Getting Started

**設定 pom.xml :**

```xml
<parent>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle</artifactId>
    <version>${latest.version}</version>
</parent>

<dependencies>
<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-core</artifactId>
</dependency>
</dependencies>
```

**spring boot 打包 :**

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

**建立spring boot application**

```java
@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

}
```

### Build Svc

#### 目錄規則 (建議)

```tex
|-svc <- put logic java
|----request <- put request model java
|----response  <- put response model java
```

#### Java File 命名規則 (建議)

* {SvcName}.java

* {SvcName}Req.java

* {SvcName}Res.java

##### 規則

* 預設路徑為/svc  例: http://localhost/{contextPath}/svc/{SvcName}

* 預設Http Method為POST

* 預設produces和consumes為application/json

* Singleton bean

* Generic type 必須設置

##### 實作

###### 實作 1 - Request & Response Body為空

**Logic**

```java
@Svc(desc = "Simple for Svc")
public class Simple extends AbstractSvcLogic<SvcReqModel, SvcResModel> {

    @Override
    public SvcResModel doService(SvcReqModel reqModel, SvcResModel resModel) {
        return resModel;
    }

}
```

執行 spring application，並且發送請求，你將會得到以下回應 :

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {}
}
```

###### 實作 2 - Request & Response Body不為空

**Logic**

```java
@Svc(desc = "Simple for Svc")
public class Simple extends AbstractSvcLogic<SimpleReq, SimpleRes> {

    @Override
    public SimpleRes doService(SimpleReq reqModel, SimpleRes resModel) {
        resModel.setMessage("Hello : " + reqModel.getName());
        return resModel;
    }

}
```

**Request Model**

建議定義以下註釋 : 

* `@JsonProperty`定義欄位反序列化鍵名

* 使用java validation功能

* `@Schema`定義欄位說明

```java
@Getter
public class SimpleReq extends SvcReqModel {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]{1,10}$")
    @Schema(description = "name")
    @JsonProperty("name")
    private String name;

}
```

**Response Model**

- @JsonProperty`定義欄位反序列化鍵名

- `@Schema`定義欄位說明

```java
@Setter
@Getter
public class SimpleRes extends SvcResModel {

    @Schema(description = "message")
    @JsonProperty("message")
    private String message;

}
```

你將會得到以下回應 : 

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "message": "Hello : mingle"
  }
}
```

**`@Svc` 屬性**

* `tags()`- `Svc` tag

* `summry()`- 概述

* `desc()`- 說明

* `path()`- 自定義路徑 (僅影響SvcName以前路徑)

* `log()`- 啟用後會呼叫`SvcLogHandler`邏輯

* `encryption()`- 啟用後會呼叫`PayLoadDecryptionHandler`解密邏輯

* `ipAddressSecure()`- IP白名單，設定後只允許此IP

**ipAddressSecure**

必須在spring properties or yaml設定`mingle.svc.security.ip.{SvcName}`屬性

```properties
mingle.svc.security.ip.TestSimple=127.0.0.1,127.0.0.2
```

多個IP使用`,`分隔

##### Custom Svc

如果預設`Svc`無法滿足你的需求，透過`@RequestMapping`相關註釋等，你可以自訂`Svc`的`request`、`response` body的格式

###### 規則

* `Svc`只允許存在一個API入口

* `doService`方法不得使用`@RequestMapping`

* `@RequestMapping` `path` & `method` 不允許多個

###### 實作

只需要新增自訂方法，並且使用`@RequestMapping`相關註釋，就會自動啟用自訂

```java
@Svc(desc = "Simple for Svc")
public class Simple extends AbstractSvcLogic<SimpleReq, SimpleRes> {

    @PostMapping("customService")
    public byte[] customService() {
        return "mingle".getBytes();
    }

    @Override
    public SimpleRes doService(SimpleReq reqModel, SimpleRes resModel) {
        resModel.setMessage("Hello : " + reqModel.getName());
        return resModel;
    }

}
```

**自訂`Svc`會導致一些功能失效**

* 自動註冊Restful API入口 - 會被自訂方法取代

* 日誌紀錄 - 不會自動寫入日誌，但是可以透過`svcInfo` 的`setBackReqModel`或 `setSvcResModelHandler4Log` 分別紀錄`request`、`response`

### Spring Open API

結合Spring Open API，可以使用`@Operation`、`@Schema`等註釋說明API規格

如果不需要非常詳細的描述API，我會選擇省略`@Operation`，透過`@Svc`簡單設置

左 `@Svc`右 `@Operation`

`tags()` = `tags()`

`summary()` = `summary()`

`desc()` = `description()`

**預設路徑**

- springdoc.swagger-ui.path : /swagger-ui.html

- springdoc.api-docs.path : /v3/api-docs

### SvcBinderComponent 取得Svc資訊

可以透過`getSvcBinderModelMap()` 取得目前所有的`Svc`資訊

### Build Action

#### 目錄規則 (建議)

```tex
|-action
|----${system name or kind} <- put logic java
|-------request <- put request model java
|-------response  <- put response model java
```

#### Java File 命名規則 (建議)

- {ActionName}.java

- {ActionName}Req.java

- {ActionName}Res.java

#### 如何建立Action

必須繼承`AbstractAction`，並且定義`Action`類型及實作範圍，所以不推薦單獨`class`直接繼承，而是透過`parent class`去處理統一邏輯

`proccessAction`是個callback方法，透過`doAction`

##### 實作

###### 實作1 - 直接實作`proccessAction`方法

**Parent Class**

```java
public abstract class AbstractDemoAction<Req extends ActionReqModel, Res extends ActionResModel> extends AbstractAction<Req, Res, ActionReqData, ActionResData<Res>> {

    @Override
    public String getType() {
        return "demo";
    }
}
```

**Sub Class**

```java
@Action(desc = "combine name")
public class NameCombineDemo extends AbstractDemoAction<NameCombineDemoReq, NameCombineDemoRes> {

    @Override
    protected NameCombineDemoRes processAction(NameCombineDemoReq reqModel, ActionReqData reqData, ActionResData<NameCombineDemoRes> resData) {
        if (reqModel.getFirstName().equals("")) {
            breakActionLogic("X001", "first name can not be null");
        }
        if (reqModel.getLastName().equals("")) {
            breakActionLogic("X002", "last name can not be null");
        }
        NameCombineDemoRes nameCombineDemoRes = new NameCombineDemoRes();
        nameCombineDemoRes.setFullName(reqModel.getFirstName() + reqModel.getLastName());
        return nameCombineDemoRes;
    }

}
```

###### 實作 2 - 透過Parent Class實作callback方法

**Parent Class**

```java
public abstract class AbstractDemoAction<Req extends ActionReqModel, Res extends ActionResModel> extends AbstractAction<Req, Res, ActionReqData, ActionResData<Res>> {
    @Override
    protected Res processAction(Req reqModel, ActionReqData reqData, ActionResData<Res> resData) {
        // common logic
        return callBack(reqModel);
    }

    protected abstract Res callBack(Req reqModel);

    @Override
    public String getType() {
        return "demo";
    }
}
```

**Sub Class**

```java
@Action(desc = "combine name")
public class NameCombineDemo extends AbstractDemoAction<NameCombineDemoReq, NameCombineDemoRes> {

    @Override
    protected NameCombineDemoRes callBack(NameCombineDemoReq reqModel) {
        if (reqModel.getFirstName().equals("")) {
            breakActionLogic("X001", "first name can not be null");
        }
        if (reqModel.getLastName().equals("")) {
            breakActionLogic("X002", "last name can not be null");
        }
        NameCombineDemoRes nameCombineDemoRes = new NameCombineDemoRes();
        nameCombineDemoRes.setFullName(reqModel.getFirstName() + reqModel.getLastName());
        return nameCombineDemoRes;
    }

}
```

**Request Model**

```java
public class NameCombineDemoReq extends ActionReqModel {

    private String firstName;

    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```

**Response Model**

```java
public class NameCombineDemoRes extends ActionResModel {

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
```

##### 執行Action

透過`@Autowired`注入`Action`，並呼叫`doAction`方法

```java
@Svc(desc = "Demo for Svc")
public class Demo extends AbstractSvcLogic<DemoReq, DemoRes> {

    @Autowired
    NameCombineDemo nameCombineDemo;

    @Override
    public DemoRes doService(DemoReq reqModel, DemoRes resModel) {
        ActionResData<NameCombineDemoRes> nameCombineDemoResActionResData = nameCombineDemo.doAction(nameCombineDemoReq(reqModel));
        if (!nameCombineDemoResActionResData.isSuccess()) {
            return returnSvcLogic(nameCombineDemoResActionResData); // ... fail process
        }
        resModel.setFullName(nameCombineDemoResActionResData.getResModel().getFullName());
        return resModel;
    }

    private NameCombineDemoReq nameCombineDemoReq(DemoReq reqModel) {
        NameCombineDemoReq nameCombineDemoReq = new NameCombineDemoReq();
        nameCombineDemoReq.setFirstName(reqModel.getFirstName());
        nameCombineDemoReq.setLastName(reqModel.getLastName());
        return nameCombineDemoReq;
    }

}
```

透過`ActionResData`取得結果，通常都會判斷是否成功，再取得`ResModel`

**Request :**

```json
{  
    "firstName": "ming",  
    "lastName": "le"
}
```

**Response :**

Success :

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "fullName": "mingle"
  }
}
```

Fail :

```json
{
  "code": "X001",
  "desc": "first name can not be null",
  "resBody": {}
}
```

##### AutoBreak

如果此功能開啟，將會自動拋出`ActionAutoBreakException`中斷`Svc`邏輯，並且把`Action`return `code`、`desc`帶入到`Svc`response裡

如果不需要處理失敗的`Action`，你可以考慮開啟此功能，可以透過`ActionReqData`或全域的Properties設置

```java
@Svc(desc = "Demo for Svc")
public class Demo extends AbstractSvcLogic<DemoReq, DemoRes> {

    @Autowired
    NameCombineDemo nameCombineDemo;

    @Override
    public DemoRes doService(DemoReq reqModel, DemoRes resModel) {
        NameCombineDemoRes nameCombineDemoResActionResData = nameCombineDemo.doAction(nameCombineDemoReq(reqModel), actionReqData()).getResModel();
        resModel.setFullName(nameCombineDemoResActionResData.getFullName());
        return resModel;
    }

    private NameCombineDemoReq nameCombineDemoReq(DemoReq reqModel) {
        NameCombineDemoReq nameCombineDemoReq = new NameCombineDemoReq();
        nameCombineDemoReq.setFirstName(reqModel.getFirstName());
        nameCombineDemoReq.setLastName(reqModel.getLastName());
        return nameCombineDemoReq;
    }

    private ActionReqData actionReqData() {
        ActionReqData actionReqData = new ActionReqData();
        actionReqData.setAutoBreak(AutoBreak.TURE);
        return actionReqData;
    }

}
```

會讓代碼寫得更少，因為`Action`啟用`AutoBreak`將會中斷`Svc`邏輯，所以不用判斷是否成功，甚至可以直接取得`ResModel`

**Request :**

```json
{  
    "firstName": "",  
    "lastName": "le"
}
```

**Response:**

```json
{
  "code": "X001",
  "desc": "first name can not be null",
  "resBody": {}
}
```

> **Note:**
> 
> 如果`Action code`不適合直接顯示在`Svc response body`，你應該關閉此功能，並且自訂`Svc code`去包裝`Action code`顯示

### 日誌紀錄

對於`Svc`、`Action`是重要的功能之一，能夠紀錄`name`、`request`、`response`、`code`、`desc`、`runTime`等，你透過`svcUuid`把`Action`關聯建立起來，可以明確知道`Svc`執行了那些`Action`，甚至成功或失敗都會有相關的資訊

### Handler

以下`Handler`的預設邏輯都可以被覆蓋，只要你`implements`對應`Handler`，並且成為`spring bean`

- `SvcLogHandler` -  `Svc` logging
- `ActionLogHandler` - `Action` logging
- `PayLoadDecryptionHandler` - `request body`解密
- `IPHandler` - 取得IP
- `SvcMsgListHandler` -  `Svc` `response` `code`、`desc`定義
- `SvcResModelHandler` - `response body` template

#### SvcMsgListHandler

##### 實作

資料存放於Database

**建立Table :**

```sql
create table MSGMAPPING
(
    MSGTYPE VARCHAR(100) not null
        primary key,
    CODE    VARCHAR(100) not null
        primary key,
    DESC    VARCHAR(1000),
    MEMO    VARCHAR(1000)
)
```

**資料表內容 :**

| MSGTYPE | CODE | DESC        | MEMO       |
| ------- | ---- | ----------- | ---------- |
| svc     | X001 | test error1 | test error |
| svc     | X002 | test error2 | test error |

**覆蓋預設邏輯**

```java
@Component
public class SvcMsgListHandlerImpl implements SvcMsgListHandler {

    @Autowired
    MsgMappingDao msgMappingDao;

    @Override
    public List<MsgModel> getMsgList() {
        ArrayList<MsgModel> msgModelArrayList = new ArrayList<>();
        msgMappingDao.findAll().forEach(node -> {
            MsgModel msgModel = new MsgModel();
            msgModel.setMsgType(node.getMsgType());
            msgModel.setCode(node.getCode());
            msgModel.setDesc(node.getDesc());
            msgModelArrayList.add(msgModel);
        });
        return msgModelArrayList;
    }

}
```

**MsgModel**

- `msgType` - group msg by type

```java
public class MsgModel {

    private String msgType;

    private String code;

    private String desc;

}
```

覆蓋完成後，將會自動對應`Svc code`並顯示`desc`

覆蓋前 :

```json
{
  "code": "X001",
  "desc": null,
  "resBody": {}
}
```

覆蓋後 :

```json
{
  "code": "X001",
  "desc": "test error1",
  "resBody": {}
}
```

>  **Note** : 
> 
> `Svc msgType`必須設置為svc，`Action msgType`預設為action

#### SvcResModelHandler

覆蓋預設`response body` template

**實作**

```java
@Component
public class SvcResponseModelHandlerImpl extends SvcResModelHandler {

    @JsonProperty("ResponseCode")
    private final ResponseCode responseCode = new ResponseCode();

    public static class ResponseCode {

        @JsonProperty("code")
        private String code;
        @JsonProperty("desc")
        private String desc;

    }

    @JsonIgnore
    @Override
    public void setCode(String code) {
        this.responseCode.code = code;
    }

    @JsonIgnore
    @Override
    public String getCode() {
        return this.responseCode.code;
    }

    @JsonIgnore
    @Override
    public void setDesc(String desc) {
        this.responseCode.desc = desc;
    }

    @JsonIgnore
    @Override
    public String getDesc() {
        return this.responseCode.desc;
    }

}
```

覆蓋前 :

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "FullName": "mingle"
  }
}
```

覆蓋後 :

```json
{
    "resBody": {
        "FullName": "mingle"
    },
    "ResponseCode": {
        "code": "0",
        "desc": "successful"
    }
}
```

> **Note :**
> 
> `resBody` 是固定的位置，只允許修改名稱，但是`code`、`desc`可以變更位置及名稱，但是必須使用`@JsonIgnore`在覆寫方法上，避免重複產生

## Exception Handler

當`Svc`、`Action`拋出的`Exception`，可以透過`handler`自訂`return code`，如果沒有自訂則會使用預設`handler`

* `AbstractExceptionHandler` - for `Svc` scope handler，預設`return code`為`MG01`

* `AbstractActionExceptionHandler` - for `Action` scope handler，預設`return code`為`MGA01`

如果不想使用`MG01`、`MGA01`邏輯，請實作`AbstractExceptionHandler<Exception>`、`AbstractActionExceptionHandler<Exception>`

### Svc Exception

Svc scope

* `ReqBodyNotJsonFormatException` 

* `ReqModelDeserializeFailException`

* `SvcAuthenticationException`

* `SvcReqModelValidFailException`

**實作 :**

當拋出`ReqBodyNotJsonFormatException`會得到 :

```json
{
    "code": "MG01",
    "desc": "Unknown exception",
    "resBody": {}
```

 實作`AbstractExceptionHandler`

```java
@Component
public class ReqBodyNotJsonFormatExceptionHandler extends AbstractExceptionHandler<ReqBodyNotJsonFormatException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(ReqBodyNotJsonFormatException ex) {
        return build("X001","not a json");
    }
}
```

再次拋出將會得到 : 

```json
{
    "code": "X001",
    "desc": "not a json",
    "resBody": {}
}
```

## Properties

| Name                             | Required | Default Value | Description                           |
| -------------------------------- | -------- | ------------- | ------------------------------------- |
| `mingle.svc.path`                |          | `/svc`        | `Svc`路徑                               |
| `mingle.svc.security.ip.openapi` |          |               | `spring open api`IP白名單設置              |
| `mingle.svc.action.logging`      |          | `disable`     | 設置為enable，啟用日誌紀錄                      |
| `mingle.svc.successCode`         |          | `0`           | 成功代碼                                  |
| `mingle.svc.successDesc`         |          | `successful`  | 成功訊息                                  |
| `mingle.svc.action.autoBreak`    |          | `false`       | 設置為`true`，如果`Action`不成功，將會自動中斷`Svc`邏輯 |
