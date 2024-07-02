# mingle-svc-redis

This project will provide redis construct and spring redis feature

## Dependency

* Spring Data Redis

## Building

#### 1. install redis

https://redis.io/

#### 2. setting pom.xml

add pom.xml :

```xml

<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-svc-redis</artifactId>
</dependency>
```

#### 3. build redis logic

##### create entity

- `@RedisPrefix` configuration redis key prefix (Required)
- must implements RedisEntity

```java

@Data
@RedisPrefix("data")
public class DataEntity extends RedisEntity {

    private String data1;

    private String data2;

}
```

##### create dao

- must extend `AbstractRedisDao<R extends Redis>` and through implicit variable `redis` to do logic
- `Redis0<V extends RedisEntity>` is a redis database 0
- `DataEntity` is a redis value

```java

@Service
public class DataDao extends AbstractRedisDao<Redis0<DataEntity>> {

    public void set(RedisKey redisKey, DataEntity dataEntity, Duration duration) {
        redis.set(redisKey, dataEntity, duration);
    }

    public Optional<DataEntity> get(RedisKey redisKey) {
        return redis.get(redisKey);
    }

}
```

##### create Svc

```java

@Svc(desc = "Test for redis")
public class TestRedis extends AbstractSvcLogic<SvcNoReq, SvcNoRes> {

    @Autowired
    DataDao dataDao;

    @Override
    public SvcNoRes doService(SvcNoReq reqModel, SvcNoRes resModel) {
        RedisKey redisKey = new RedisKey(DataEntity.class);
        redisKey.addParam("DataOnlyValue"); // set param of key

        DataEntity dataEntity = new DataEntity(); // json entity
        dataEntity.setData1("Data1"); // body data value
        dataEntity.setData2("Data2"); // body data value

        dataDao.set(redisKey, dataEntity, Duration.ofMinutes(10));

        dataDao.get(redisKey);

        return resModel;
    }

}
```

open your `redis-cli` you will get :

key : `data:DataOnlyValue`

value :

```json
{
  "@class": "com.example.demo.dao.redis0.entity.DataEntity",
  "data1": "Data1",
  "data2": "Data2"
}
```

## Properties

| Name                       | Required | Default Value |           Description            |
|:---------------------------|:--------:|:-------------:|:--------------------------------:|
| `mingle.svc.redis.logging` |          |   `disable`   | aop logging for redis set enable |

please watch `spring.redis` properties

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#appendix.application-properties.data
