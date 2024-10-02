# mingle-svc-action

`action`使用模組概念，負責提供`service`部分邏輯，讓開發人員可以重複利用，來減低開發成本。

## 特點
- 使用回傳狀態碼(code)、狀態訊息(msg)決定成功或失敗
- 統一的開發流程
- 鏈式日誌記錄
- 與`service`相同的`request`、`response`的`POJO`設計方法

## Getting Started

### Maven設定

```xml
<dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-svc-action</artifactId>
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

### java命名規則 (建議)
- {actionName}.java
- {actionName}Req.java
- {actionName}Res.java

### package規則 (建議)
```text
com.example
├── DemoApplication.java
├── service(svc)
│   │── action
│   │   └──── {action kind}
│   │         │── request
│   │         │── response
│   │         └──── {actionName}.java
```

### 建立action

首先，必須要先有`service`，請參考[mingle-core](../mingle-core/README.md)。

建立一個負責將值加總的`action`

**request範例**
```java
@Setter
@Getter
public class SumNumReq extends ActionRequest {

  private List<Integer> numbers;

}
```

**response範例**
```java
@Getter
@Setter
public class SumNumRes extends ActionResponseBody {

  private int sum;

}
```

**action範例**
```java
@Component
public class SumNum extends AbstractAction<SumNumReq, SumNumRes> {


  public SumNum(ActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  protected SumNumRes processLogic(SumNumReq sumNumReq, ActionInfo actionInfo) {
    if(sumNumReq.getNumbers() == null || sumNumReq.getNumbers().isEmpty()) {
      actionInfo.setCode("A001");
      actionInfo.setMsg("numbers cannot be null or empty");
      return null;
    }
    SumNumRes sumNumRes = new SumNumRes();
    sumNumRes.setSum(sumNumReq.getNumbers().stream().mapToInt(Integer::intValue).sum());
    return sumNumRes;
  }

  @Override
  public String getType() {
    return "MathAction";
  }

}
```

**service範例**

```java
@Getter
public class SumNumSvcReq extends SvcRequest {

  private List<String> numbers;

}
```

```java
@Setter
@Getter
public class SumNumSvcRes extends SvcResponseBody {

  private String sum;

}
```

```java
@Svc(tags = "sumNum", summary = "Sum numbers", description = "Sum numbers")
public class SumNumSvc extends AbstractService<SumNumSvcReq, SumNumSvcRes> {

  @Autowired
  private SumNum sumNum;

  public SumNumSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public SumNumSvcRes doService(SumNumSvcReq sumNumSvcReq) {
    ActionResponse<SumNumRes> sumNumResActionResponse = sumNum.doAction(sumNumReq(sumNumSvcReq));
    if (!sumNumResActionResponse.isSuccess()) { //判斷action是否成功，成功才能取得response body
      return returnLogic(SvcResponseHeader.builder("A002").msg("action not success").build());
    }
    SumNumSvcRes sumNumSvcRes = new SumNumSvcRes();
    sumNumSvcRes.setSum(String.valueOf(sumNumResActionResponse.getResponseBody().get().getSum()));
    sumNumResActionResponse.getResponseBody().get().getSum();
    return sumNumSvcRes;
  }

  private SumNumReq sumNumReq(SumNumSvcReq sumNumSvcReq) {
    SumNumReq sumNumReq = new SumNumReq();
    sumNumReq.setNumbers(sumNumSvcReq.getNumbers().stream().map(Integer::parseInt).toList());
    return sumNumReq;
  }

}
```

上述流程完成後，發送請求：

```text
URL : http://localhost:8080/svc/SumNumSvc
Method : POST
```
request body :

```json
{
    "numbers": [
        "1",
        "2",
        "3",
        "4"
    ]
}
```

response body :

```json
{
    "code": "0",
    "msg": "successful",
    "body": {
        "sum": "10"
    }
}
```

成功透過`action`取得總和，讓`service`處理並回覆結果。

##主要組件
介紹`ActionRequest`、`ActionResponseBody`、`Action`組件功能：

### ActionRequest

`POJO`物件，提供`action`資料。

- 搭配`jackson`套件使用。
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位。

### ActionResponseBody

`POJO`物件，提供`service`資料。

- 搭配`jackson`套件使用。
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位。

### Action

邏輯層，負責提供`service`模組化邏輯，呼叫`doAction`方法觸發。

## 功能

### 日誌記錄
鏈式的紀錄方式，以`service`紀錄為主，如果`service`沒有紀錄`action`應不該紀錄。
- 序列號
- 類別名稱
- 開始、結束時間
- 請求、回應資料
- 執行時間

```text
【Svc Request】: {"svcSerialNum":"48a23c08-1efa-47e3-ade9-79a65c4ff547","name":"SumNumSvc","startDateTime":"2024/06/10 10:26:13","request":{"numbers":["1","2","3","4"]}}

【Action Request】: {"svcSerialNum":"48a23c08-1efa-47e3-ade9-79a65c4ff547","actSerialNum":"2f755df6-c58d-4473-9ccf-5cc378b03c14","name":"SumNum","startDateTime":"2024/06/10 10:26:13","type":"MathAction"}

【Action Response】: {"svcSerialNum":"48a23c08-1efa-47e3-ade9-79a65c4ff547","actSerialNum":"2f755df6-c58d-4473-9ccf-5cc378b03c14","endDateTime":"2024/06/10 10:26:13","responseBody":{"sum":10},"runTime":"1 ms"}

【Svc Response】: {"svcSerialNum":"48a23c08-1efa-47e3-ade9-79a65c4ff547","endDateTime":"2024/06/10 10:26:13","response":{"sum":"10"},"runTime":"53 ms"}
```
透過`service`將紀錄整個鏈式的交易歷程。

### AutoBreak
此功能為了避免忘記判斷，也讓代碼可讀性提高，執行`action`後，如果非成功，將會拋出`ActionAutoBreakException`，強制中斷`service`邏輯，並且回應`action`的`code`、`msg`。

啟用前，必須手動判斷是否失敗，防止後續邏輯出問題。
```java
ActionResponse<SumNumRes> sumNumResActionResponse = sumNum.doAction(sumNumReq(sumNumSvcReq));
if (!sumNumResActionResponse.isSuccess()) { //判斷action是否成功，成功才能取得response body
    return returnLogic(SvcResponseHeader.builder("A002").msg("action not success").build());
}
sumNumResActionResponse.getResponseBody().get().getSum();
```

啟用後，將自動拋出例外，中斷`service`邏輯。
```java
ActionResponse<SumNumRes> sumNumResActionResponse = sumNum.doAction(sumNumReq(sumNumSvcReq));
sumNumResActionResponse.getResponseBody().get().getSum();
```

**啟用方式**
- `properties`全局設定
- `ActionRequest`屬性設定
  ```java
  SumNumReq sumNumReq = new SumNumReq();
  sumNumReq.setAutoBreak(AutoBreak.TRUE);
  sumNumReq.setNumbers(sumNumSvcReq.getNumbers().stream().map(Integer::parseInt).toList());
  ```

> [!NOTE]
> 建議啟用，遇到單一狀況才透過`ActionRequest`關閉。

### Interceptor
提供`Interceptor`功能，讓`action`在執行時，有`filterChain`的功能。

```java
@Component
public class InterceptorImpl implements ActionInterceptor {

  @Override
  public void intercept(Chain chain) {
    //before logic
    chain.proceed();
    //after logic
  }

}
```

> [!CAUTION]
> 避免在`Interceptor`裡呼叫`action`，造成死循環。

## 配置屬性

下面列出了主要配置屬性及其描述。

| 屬性                                | 預設值         | 描述                                    |
|-------------------------------------|----------------|-----------------------------------------|
| `mingle.svc.action.code`            | `0000`         | 狀態碼，表示成功或失敗                    |
| `mingle.svc.action.msg`             | `successful`   | 狀態訊息                                  |
| `mingle.svc.action.auto-break`      | `false`        | 自動中斷 `service` 邏輯                   |
| `mingle.svc.action.msg-type`        | `action`       | 狀態訊息類型                               |
