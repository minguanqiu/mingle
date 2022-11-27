# mingle-core

Core of Mingle,using [Service](#Service) and [Action](#Action) concept,easily building business logic

#### slogan

Multiple `Action` to build one `Service`

### Service

Service (alias Svc) is restful api entrance,coding business logic place,every Service
is single will not interdependent

#### Feature

* Using return code to decide successful or fail
* Auto register api entrance
* Auto register swagger open API
* Request and response using pojo model design
* Easy setting status by `@Svc`
* Aop logging

##### Spring open api

default path

* springdoc.swagger-ui.path : /swagger-ui.html
* springdoc.api-docs.path : /v3/api-docs

### Action

Is a module or component,every type can be Action

refer to [mingle-svc-action-rest](mingle-svc-action-rest) - implement by client feature

#### Feature

* Using return code to decide successful or fail
* Request and response using pojo model design
* Aop logging
* Thread safe

## Getting Started

#### Create maven project and setting pom.xml

add dependencies:

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

for spring packaging:

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

#### Create spring boot application

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

}
```

now startup server,but you will get this exception message

```text
MingleRuntimeException: must create at least one service
```

so you must go first [Build Svc](#Build-Svc)

## Build Svc

#### Package Rule (Recommended)

```text
|-svc <- put logic java
|----request <- put request model java
|----response  <- put response model java
```

#### Java File Naming (Recommended)

* {SvcName}.java
* {SvcName}Req.java
* {SvcName}Res.java

#### Notice

* Default prefix path by /svc example: `http://localhost/{contextPath}/svc/{SvcName}` ,change
  by [Properties](#Properties)
* Default http method by POST
* Default produces and consumes by application/json
* Singleton scope bean
* Generic type must be set

#### Practice 1

Practice for empty request response body

**Svc Logic**

```java

@Svc(desc = "Simple for Svc")
public class Simple extends AbstractSvcLogic<SvcReqModel, SvcResModel> {

    @Override
    public SvcResModel doService(SvcReqModel reqModel, SvcResModel resModel) {
        return resModel;
    }

}

```

after you can run application,and send request,you will get this response like this

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {}
}
```

#### Practice 2

Example for not empty request response body

**Svc Logic**

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

* `@JsonProperty` defined field deserialize key name (Recommend)
* Using java validation feature (Recommend)
* `@Schema` defined field description (Recommend)

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

* `@JsonProperty` defined field serialize key name (Recommend)
* `@Schema` defined field description (Recommend)

```java

@Setter
public class SimpleRes extends SvcResModel {

    @Schema(description = "Message")
    @JsonProperty("Message")
    private String Message;

}

```

you will get this response like this:

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "Name": "Hello : mingle"
  }
}
```

#### `@Svc` attribute

* `desc()` - Description for Svc
* `path()` - Custom Svc path
* `log()` - Enable logging will call `SvcLogHandler` logic
* `encryption()` - Request body need encryption will call `PayLoadDecryptionHandler` decryption logic
* `ipAddressSecure()` - Ip address protected [ipAddressSecure](#ipAddressSecure)
* `custom()` - Custom own controller to break away Svc rule

```java

@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Svc {

    String desc();

    String path() default "";

    boolean log() default true;

    boolean encryption() default false;

    boolean ipAddressSecure() default false;

    boolean custom() default false;

}
```

##### ipAddressSecure

if set ture,must set `mingle.svc.security.ip.{SvcName}` in spring properties or yaml

```properties
mingle.svc.security.ip.TestSimple=127.0.0.1,127.0.0.2
```

multiple ip using `,` separate

Congratulations you can start building yourself logic

#### Interrupt Svc Logic

you can decide logic process flow,if you want interrupt logic, call `returnSvcLogic` or `breakSvcLogic` method in Svc
Logic

* `returnSvcLogic` normal return logic to interrupt
* `breakSvcLogic` throw exception to interrupt logic,but will be caught by Svc logic process

```java

@Svc(desc = "Service Demo")
public class DemoSvc extends AbstractSvcLogic<DemoReq, SvcResModel> {

    @Override
    public SvcResModel doService(DemoReq reqModel, SvcResModel resModel) {

        if (reqModel.getName().equals("")) {
            return returnSvcLogic("X001"); // will return Svc logic
        }

        if (reqModel.getName().equals("")) {
            breakSvcLogic("X002");  // will throw exception to interrupt Svc logic
        }

        return resModel;
    }
}
```

## Build Action

#### Package Rule (Recommended)

```text
|-action
|----${system name or kind} <- put logic java
|-------request <- put request model java
|-------response  <- put response model java
```

#### Java File Naming (Recommended)

* {ActionName}.java
* {ActionName}Req.java
* {ActionName}Res.java

> **Note** `@Action` must add on Action become to spring bean

#### How to build Action module ?

`Action` module usually create middle parent abstract class to implements `AbstractAction`,through parent to design some
common method or variable up to your logic or type

`processAction` is a callback method,through `doAction` method trigger

#### AbstractAction

Every Action must need extends

```java
public abstract class AbstractAction<Req extends ActionReqModel, Res extends ActionResModel, ReqData extends ActionReqData, ResData extends ActionResData<Res>> {
}

```

**Generic Type**

* Req - Action request model,using like body
* Res - Action response model,using like body
* ReqData - Action request data,using like header
* ResData - Action response data,using like header

**Practice 1**

Parent Class

this practice didn't need custom `ReqData` and `ResData` ,so defined default `ActionReqData` and `ActionResData<Res>`

```java
public abstract class AbstractDemoAction<Req extends ActionReqModel, Res extends ActionResModel> extends AbstractAction<Req, Res, ActionReqData, ActionResData<Res>> {

    @Override
    public String getType() {
        return "demo";
    }
}
```

Sub Class

subclass extend this parent class to do logic

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

**Practice 2 (Recommend)**

Let parent class override `processAction` and new abstract `callBack` method,so you can process common logic
in `processAction` like aop

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

#### Execute Action

must use @Autowired to inject action module,and call doAction method

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

Success example :

request :

```json
{
  "firstName": "ming",
  "lastName": "le"
}
```

response :

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "FullName": "mingle"
  }
}
```

Fail example :

request :

```json
{
  "firstName": "",
  "lastName": "le"
}
```

response :

```json
{
  "code": "X001",
  "desc": "first name can not be null",
  "resBody": {}
}
```

`ActionResData`

action response data

* `getCode()` - get return code
* `getDesc()` - get return desc
* `getResModel()` - get response model
* `isSuccess()` - action status (if code != success code always fail)

`autoBreak`

this feature will auto throw `ActionAutoBreakException` to break Svc logic,and auto take action code and desc to Svc
response

if you don't want process fail action,you can through action request data autoBreak ture or set global
by [Properties](#Properties),default using global properties value

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

this example let code write less,because action will auto break Svc logic,so you can direct call `getResModel()`

Fail example :

request :

```json
{
  "firstName": "",
  "lastName": "le"
}
```

response :

```json
{
  "code": "X001",
  "desc": "first name can not be null",
  "resBody": {}
}
```

> **Note** <br>
> if action code not suitable for direct show to Svc response,you should disable autoBreak feature,and custom Svc code
> to wrapper

## Handler

These handler can be implements and custom by yourself,must using @Component|@Service become spring bean to override

- `SvcLogHandler` - process `Svc` log handler
- `ActionLogHandler` - process `Action` log handler
- `PayLoadDecryptionHandler` - process request body for decryption
- `IPHandler` - process get ip logic
- `SvcMsgListHandler` - build `Svc` response code and desc mapping
- `SvcResModelHandler` - custom response body

#### SvcMsgListHandler

example for data from database:

create Msg mapping table

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

| MSGTYPE | CODE | DESC        | MEMO       |
|---------|------|-------------|------------|
| svc     | X001 | test error1 | test error |
| svc     | X002 | test error2 | test error |

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

* msgType - group by type

```java
public class MsgModel {

    private String msgType;

    private String code;

    private String desc;

}
```

will auto process code and desc mapping

response example :

before :

```json
{
  "code": "X001",
  "desc": null,
  "resBody": {}
}

```

after:

```json
{
  "code": "X001",
  "desc": "test error1",
  "resBody": {}
}
```

> **Note** : <br>
> Svc default msgType is svc <br>
> Action default msgType is action

#### SvcResModelHandler

change default response body format

> **Note** <br>
> `resBody` is fixed only change name,but `code` and `desc` can change location or name,must use @JsonIgnore on override method,prevent json serialize bug happened

Example for change default format

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

before :

```json
{
  "code": "0",
  "desc": "successful",
  "resBody": {
    "FullName": "mingle"
  }
}
```

after : 

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

### Svc code keyword

you can override system code description by `SvcMsgListHandler`

|  Code  |         Description          |
|:------:|:----------------------------:|
| `MG01` |      Unknown exception       |
| `MG02` |   Request deserialize fail   |
| `MG03` |   Request body valid error   |
| `MG04` |        Access Denied         |
| `MG05` | Request body not json format |

### Action code keyword

|  Code   |          Description          |
|:-------:|:-----------------------------:|
| `MGA01` | Exception : " + e.getCause()  |
| `MGA02` | Request data newInstance fail |

## Properties

| Name                             | Required | Default Value |                                  Description                                   |
|:---------------------------------|:--------:|:-------------:|:------------------------------------------------------------------------------:|
| `mingle.svc.path`                |          |    `/svc`     |                                    svc path                                    |
| `mingle.svc.security.ip.openapi` |          |               |                      protected spring open api path by ip                      |
| `mingle.svc.action.logging`      |          |   `disable`   |                    aop logging for action module set enable                    |
| `mingle.svc.successCode`         |          |      `0`      |                                  success code                                  |
| `mingle.svc.successDesc`         |          | `successful`  |                                  success desc                                  |
| `mingle.svc.action.autoBreak`    |          |    `false`    | if action module not success code will auto throw exception to break Svc logic |
