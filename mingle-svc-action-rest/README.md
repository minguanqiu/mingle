# mingle-svc-action-rest

[action](../mingle-svc-action/README.md) 的實現，提供`HTTP`的`client`功能模組，進行`API`呼叫處理。

## 特點
- [action](../mingle-svc-action/README.md) 模組化設計
- 開箱即用的`client`功能

## Getting Started

**Maven設定**

```xml
<dependencies>
    <dependency>
      <groupId>io.github.minguanqiu</groupId>
      <artifactId>mingle-svc-action-rest</artifactId>
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

有一個名稱為`Simple`的系統，以下是相關`API`規格。

```text
URL : http://localhost:8080/svc/SimpleDemoSvc
Method : POST
```

**request body :**

```json
{
    "text1": "Hello",
    "text2": "World"
}
```

**response body :**

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "text": "Hello World"
    }
}
```

要透過程式的方式呼叫`API`，就必須要撰寫`client`相關程式，但是透過此模組就可以很輕鬆的完成。

首先，繼承`AbstractRestAction`為`Simple`系統建立抽象類，並建立通用類型方法。

```java
public abstract class AbstractSimpleAction<Req extends RestActionRequest, ResB extends ActionResponseBody> extends
    AbstractRestAction<Req, ResB> {

  public AbstractSimpleAction(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  public HttpMethod getHttpMethod() {
    return HttpMethod.POST;
  }

  @Override
  public String getServerName() {
    return "Simple";
  }

  @Override
  public String getType() {
    return "Simple";
  }

  @Override
  protected RestActionResponseBodyHeader buildResponseBodyHeader(JsonNode resultNode) {
    RestActionResponseBodyHeader restActionResponseBodyHeader = new RestActionResponseBodyHeader();
    restActionResponseBodyHeader.setSuccessCode("0");
    restActionResponseBodyHeader.setCode(resultNode.get("code").asText());
    restActionResponseBodyHeader.setMsg(resultNode.get("msg").asText());
    return restActionResponseBodyHeader;
  }

  @Override
  protected ResB deserializeResponseBody(JsonNode resultNode) {
    return restActionJacksonUtils.readValue(resultNode.get("body").toString(), resClass)
        .orElseThrow(ActionResponseBodyDeserializeErrorException::new);
  }

}
```
配置屬性：
```yaml
mingle:
  svc:
    action:
      rest:
        server:
          Simple: #依據getServerName()
            scheme: http
            host: 127.0.0.1
            port: 8080
            path-segments:
              - svc #maybe context path
```



以上方法簡單介紹：
- getHttpMethod - 設定`API`的`HTTP`方法
- getServerName - 設定目標系統名稱，並搭配配置屬性取得路徑
- getType - `action`的類型
- buildResponseBodyHeader - 如果此系統有狀態碼，可以透過此方法設定，預設為`null`。(可選)
- deserializeResponseBody - 截取部分`response body`，執行`JSON`反序列化成為`ActionResponseBody`物件，預設為整個`response body`。(可選)

接下來，我們就可以針對目標的API，建立相對應的`action`。

**RestActionRequest：**

```java
@Setter
@Getter
public class SimpleDemoSvcReq extends RestActionRequest {

  private String text1;

  private String text2;

}
```

**ActionResponseBody：**
```java
@Getter
@Setter
public class SimpleDemoSvcRes extends ActionResponseBody {

  private String text;

}
```

**AbstractRestAction：**
```java
@Service
public class SimpleDemoSvc extends AbstractSimpleAction<SimpleDemoSvcReq, SimpleDemoSvcRes> {

  public SimpleDemoSvc(RestActionProperties actionProperties) {
    super(actionProperties);
  }

  @Override
  protected List<String> buildRestPath(SimpleActionReq request) {
    return List.of("SimpleSvc");
  }
}

```
- buildRestPath - 只設定API路徑。

建立`service`裡面包含rest action。
```java
@Svc(tags = "svc", summary = "RestTestSvc", description = "Test rest service")
public class RestTestSvc extends AbstractService<RestTestSvcReq, RestTestSvcRes> {

  @Autowired
  private SimpleDemoSvc simpleDemoSvc;

  public RestTestSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public RestTestSvcRes doService(RestTestSvcReq request) {
    ActionResponse<SimpleDemoSvcRes> simpleDemoSvcResActionResponse = simpleDemoSvc.doAction(simpleDemoSvcReq(request));
    RestTestSvcRes restTestSvcRes = new RestTestSvcRes();
    restTestSvcRes.setText(simpleDemoSvcResActionResponse.getResponseBody().get().getText());
    return restTestSvcRes;
  }

  private SimpleDemoSvcReq simpleDemoSvcReq(RestTestSvcReq request) {
    SimpleDemoSvcReq simpleDemoSvcReq = new SimpleDemoSvcReq();
    simpleDemoSvcReq.setText1(request.getText1());
    simpleDemoSvcReq.setText2(request.getText2());
    return simpleDemoSvcReq;
  }

}
```
完成後，發送請求至`service`

```text
URL : http://localhost:8080/svc/RestTestSvc
Method : POST
```

**request body：**

```json
{
    "text1": "Hello",
    "text2": "World"
}
```

**response body：**

```json
{
    "code": "0000",
    "msg": "successful",
    "body": {
        "text": "Hello World"
    }
}
```

你會發現其實跟一般`action`使用方式一致，只是用途不同。

## 主要組件

### RestActionRequest
自動將物件序列化成`JSON`字串變成`request body`。
- `OkHttpClient.Builder` - 彈性調整相關`client`設定
- `@QueryParameter` - 欄位加入URL
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位

### ActionResponseBody
自動將`response body`反序列化成物件
- `@ExcludeLog` - 日誌記錄時，忽略相對欄位

### AbstractRestAction
`action`的抽象實作，`RESTful`模組，透過自動的序列化、反序列化，來達到快速、方便的應用。
> [!NOTE]
> 通常由父類繼承，而不是子類直接繼承。

## 配置屬性

下面列出了主要配置屬性及其描述。


| 屬性                                                    | 預設值                  | 描述                                |
|---------------------------------------------------------|-------------------------|-------------------------------------|
| `mingle.action.rest.server.{server name}.scheme`               | `http`                  | 服務器的協議（如 `http` 或 `https`）|
| `mingle.action.rest.server.{server name}.host`                 | 無                      | 服務器的主機名                      |
| `mingle.action.rest.server.{server name}.port`                 | 無                      | 服務器的端口號                      |
| `mingle.action.rest.server.{server name}.pathSegments`         | 無                      | URL 路徑段，為數組格式              |
| `mingle.action.rest.mock.{action name}.code`                   | `200`                   | 模擬響應的狀態碼                    |
| `mingle.action.rest.mock.{action name}.header`                 | 空 Map                  | 模擬響應的頭信息                    |
| `mingle.action.rest.mock.{action name}.responseBody.mediaType` | `application/json`      | 模擬響應的媒體類型                  |
| `mingle.action.rest.mock.{action name}.responseBody.content`   | 無                      | 模擬響應的內容                      |
| `mingle.action.rest.mock.{action name}.message`                | `200 OK`                | 模擬響應的狀態訊息                  |
| `mingle.action.rest.mock.{action name}.delay`                  | `0`                     | 模擬響應的延遲時間，以毫秒為單位    |

