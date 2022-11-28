# mingle-action-rest

This module will provide action implements for rest client feature

## Building

### Setting pom.xml

add pom.xml :

```xml

<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-action-rest</artifactId>
</dependency>
```

### Build Rest Action

create parent abstract class

* `buildRequestCacheHeaderValue()` - build request header by cache
* `buildCacheSuccessHttpCode()` - allow http code
* `getType()` - defined Action type (not null)

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

`buildCacheSuccessHttpCode()` - allow http code

return null will disable action http code auto break
,if set only 200 will auto break by except 200 http code

create subClass extends parent class

`buildRequestCacheHeaderValue()` - build request header by cache

if return map will set request header by every times

`before(Req reqModel)` - send request before will call

`after(Res resModel)` - send request after will call

`getType()` - defined Action type (not null)

* @RestAction must add on rest Action class

```java

@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName extends AbstractRestDemoAction<GetNameReq, GetNameRes> {


}
```

#### Defined Action uri

properties :

```properties
mingle.svc.action.rest.RestDemo.uri=http://localhost:8081/restdemo
mingle.svc.action.rest.RestDemo.impl.uri.GetName=/getName
```

yaml :

```yaml
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

Uri rule two part

* `mingle.svc.action.rest.{action type}.uri` context path
* `mingle.svc.action.rest.{action type}.impl.uri.{action class name}` api path

That's why `getType()` must not be null reason

#### Feature for request type

#### Get Method

When http method is GET,will use request model field to build uri

target API :

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

logic :

```java

@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName extends AbstractRestDemoAction<GetNameReq, GetNameRes> {


}
```

request model :

```java

@Setter
public class GetNameReq extends ActionReqModel {

    private String name;

}
```

response model :

```java

@Getter
public class GetNameRes extends ActionResModel {

    private String name;

}
```

Svc logic :

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

call `doAction` after will see by print

```text
http://localhost:8081/restdemo/getName?name=Mingle
Mingle
```

#### PathVariable

`@PathVariable` - defined path variable field  

`@ExcludeRequestBody` - exclude field with body

target API :

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

logic :

```java

@RestAction(desc = "Get Name", method = HttpMethod.GET)
public class GetName1 extends AbstractRestDemoAction<GetName1Req, GetName1Res> {


}
```

request model :

```java

@Setter
public class GetName1Req extends ActionReqModel {

    @PathVariable
    @ExcludeRequestBody
    private String name;

}
```

response model :

```java

@Getter
public class GetNameRes extends ActionResModel {

    private String name;

}

```

call `doAction` after will see by print

```text
http://localhost:8081/restdemo/getName1/Mingle
Mingle
```

if no add `@ExcludeRequestBody` uri will become 

```text
http://localhost:8081/restdemo/getName?name=Mingle
```

#### Mock Action

Provide action mock feature

Defined `mingle.svc.action.rest.mock.path` and drop `${action class name}.json` file

* `sleep` - unit is millisecond
* `data` - mock API response body

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

run after won't send request to target server,will using this mock data to do action logic

## Action Code Keyword

|   Code   |                   Description                    |
|:--------:|:------------------------------------------------:|
| `MGRA01` |             build request body fail              |
| `MGRA02` |         client error :  + e.getMessage()         |
| `MGRA03` | mediaType parse fail,please check mediaType type |
| `MGRA04` |           request model serialize fail           |
| `MGRA05` |           client code error : " + code           |
| `MGRA06` |                 mock data error                  |
| `MGRA07` |              resModel format error               |
| `MGRA08` |                data format error                 |

## Properties

| Name                                           | Required | Default Value |       Description       |
|:-----------------------------------------------|:--------:|:-------------:|:-----------------------:|
| `mingle.svc.action.rest.mock.path`             |          |               |    action mock path     |
| `mingle.svc.action.rest.client.connectTimeOut` |          |   `3000 ms`   | client connect time out |
| `mingle.svc.action.rest.client.readTimeOut`    |          |  `70000 ms`   |  client read time out   |
| `mingle.svc.action.rest.client.ignoreSSL`      |          |    `false`    |    client ignore SSL    |