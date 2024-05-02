# mingle-svc-action-rest

此模組透過`Action`實現，提供`Restful`client功能

## Getting Started

add pom.xml : 

```xml
<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-svc-action-rest</artifactId>
</dependency>
```

#### Build Rest Action

首先建立`parent class`，目標`Server`為依據命名

```java
public abstract class AbstractRestDemoAction<Req extends ActionReqModel, Res extends ActionResModel> extends AbstractRestAction<Req, Res> {

    @Override
    protected Map<String, String> buildRequestCacheHeaderValue() {
        return null;
    }

    @Override
    protected Set<Integer> buildCacheSuccessHttpCode() {
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(200);
        return hashSet;
    }

    @Override
    protected void before(Req reqModel) {

    }

    @Override
    protected void after(Res resModel) {

    }


    @Override
    public String getType() {
        return "RestDemo";
    }

}
```

`buildCacheSuccessHttpCode()`

如果只新增`http code`200，其餘`http code`則會讓`Action`不成功

`buildRequestCacheHeaderValue()`

每次皆會把`cache`的值帶入`request header`中

`getType()`

定義目標`serverProperties`名稱

`再建立`sub class`繼承`parent class

```java
@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName extends AbstractRestDemoAction<GetNameReq, GetNameRes> {


}
```

#### Configruation URI

**properties :**

```properties
mingle.svc.action.rest.RestDemo.uri=http://localhost:8081/restdemo
mingle.svc.action.rest.RestDemo.impl.uri.GetName=/getName
```

**yaml :**

```yml
mingle:
  svc:
    action:
      rest:
        RestDemo:
          uri: http://localhost:8081/restdemo
          impl:
            uri:
              GetName: /getName
```

**URI 規則**

- `mingle.svc.action.rest.{action type}.uri` context path
- `mingle.svc.action.rest.{action type}.impl.uri.{action class name}` api path

這就是為甚麼`getType()`不能為null的原因

#### Request Model Convert Body

##### Get Method

透過`request model`的變數`name`、`value`自動建立`query parameter`

**Target API :**

```java
@RestController
public class GetNameController {

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/getName")
    public String getName(@PathParam("name") String name) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", name);
        return objectNode.toString();
    }

}
```

**Logic :**

```java
@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName extends AbstractRestDemoAction<GetNameReq, GetNameRes> {


}
```

**Request Model :**

```java
@Setter
public class GetNameReq extends ActionReqModel {

    private String name;

}
```

**Response Model :**

```java
@Getter
public class GetNameRes extends ActionResModel {

    private String name;

}
```

**Svc Logic :**

```java
@Svc(desc = "Rest action demo")
public class GetNameDemo extends AbstractSvcLogic<SvcReqModel, SvcResModel> {

    @Autowired
    GetName getName;

    @Override
    public SvcResModel doService(SvcReqModel reqModel, SvcResModel resModel) {
        RestActionResData<GetNameRes> getNameResRestActionResData = getName.doAction(getNameReq());
        System.out.println(getNameResRestActionResData.getUri());
        System.out.println(getNameResRestActionResData.getResModel().getName());
        return resModel;
    }

    private GetNameReq getNameReq() {
        GetNameReq model = new GetNameReq();
        model.setName("Mingle");
        return model;
    }

}
```

執行`doAction`將會打印出以下訊息

```tex
http://localhost:8081/restdemo/getName?name=Mingle
Mingle
```

###### PathVariable

* `PathVariable` - 定義`path`變數欄位

* `ExcludeRequestBody` - 排除`body`欄位

**Target API :**

```java
@RestController
public class GetName1Controller {

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/getName1/{name}")
    public String getName(@PathVariable("name") String name) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", name);
        return objectNode.toString();
    }

}
```

**Logic :**

```java
@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName1 extends AbstractRestDemoAction<GetName1Req, GetName1Res> {


}
```

**Request Model :**

因為是`Get Method`所以必須透過`@ExcludeRequest`排除`query paramter`

```java
@Setter
public class GetName1Req extends ActionReqModel {

    @PathVariable
    @ExcludeRequestBody
    private String name;

}
```

**Response Model :**

```java
@Getter
public class GetName1Res extends ActionResModel {

    private String name;

}
```

執行`doAction`將會打印出以下訊息

```
http://localhost:8081/restdemo/getName1/Mingle
Mingle
```

如果沒加入 `@ExcludeRequestBody` `URI` 將會帶入`query parameter`

```
http://localhost:8081/restdemo/getName?name=Mingle
```

##### Post Method

`request model`自動轉換以下`media type`的`request body`

###### multipart/form-data

**Target API :**

```java
@PostMapping(path = "setFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String setFile(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) throws IOException {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("FileName", fileName);
        objectNode.put("FileBase64", Base64.encodeBase64String(file.getBytes()));
        return objectNode.toString();
    }
```

**Request Model :**

```java
@Setter
public class SetFileReq extends ActionReqModel {

    private String fileName;

    private byte[] file;

}
```

###### application/x-www-form-urlencoded

**Target API :**

```java
@PostMapping(path = "getName2", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getName2(@RequestParam MultiValueMap<String, String> paramMap) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Name", paramMap.getFirst("name"));
        objectNode.put("Age", paramMap.getFirst("age"));
        return objectNode.toString();
    }
```

**Request Model :**

```java
@Setter
public class GetName2Req extends ActionReqModel {

    private String name;

    private String age;

}
```

###### application/json

**Target API :**

```java
@PostMapping(path = "jsonTest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String jsonTest(@RequestBody String user) throws IOException {
        System.out.println(user);
        return user;
    }
```

**Request Model :**

```java
@Setter
public class JsonTestReq extends ActionReqModel {

    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private String age;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("authorities")
    private List<Authority> authorities;
    @JsonProperty("datas")
    private List<String> datas;
    @ExcludeRequestBody
    private String ttttt = "tttt";

    @Setter
    public static class Data {

        @JsonProperty("email")
        private String email;
        @JsonProperty("mobile")
        private String mobile;
    }

    @Setter
    public static class Authority {

        @JsonProperty("name")
        private String name;

    }

}
```

一樣可以使用`@ExcludeRequestBody`忽略

#### Response Mock

定義`mingle.svc.action.rest.mock.path`及新增`${action class name}.json`檔案

* `sleep` - 等待時間(毫秒)

* `data` -  response body

**範例 :**

```json
{
  "sleep": "2000",
  "data": {
    "code": "0",
    "desc": "successful",
    "resBody": {
      "Message": "Hello : mingle"
    }
  }
}
```

設置成功後，執行`Action`就不會發送`request`到目標`API`，而是使用`mock data`回應

## Exception

Action scope，請參考[mingle-core](#mingle-core) 實作Exception Handler

- `ActionReqModelSerializeFailException`

- `ActionResModelFormatErrorException`

- `ActionRestResModelFormatFailException`

- `ClientErrorException`

- `HttpCodeErrorException`

- `MediaTypeParseFailException`

- `MockDataParseFailException`

## Properties

| Name                                           | Required | Default Value | Description             |
| ---------------------------------------------- | -------- | ------------- | ----------------------- |
| `mingle.svc.action.rest.mock.path`             |          |               | action mock path        |
| `mingle.svc.action.rest.client.connectTimeOut` |          | `3000 ms`     | client connect time out |
| `mingle.svc.action.rest.client.readTimeOut`    |          | `70000 ms`    | client read time out    |
| `mingle.svc.action.rest.client.ignoreSSL`      |          | `false`       | client ignore SSL       |
