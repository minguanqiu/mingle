# mingle-svc-data

This project will provide dao construct and spring data jpa feature

## Prepare skills:

* Spring Data JPA

## Building

#### 1. setting pom.xml

add `pom.xml` :

```xml

<dependency>
    <groupId>io.github.amings</groupId>
    <artifactId>mingle-svc-data</artifactId>
</dependency>
```

#### 2. choose your database class and setting

example for postgresql:

```xml
<!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.5.0</version>
</dependency>
```

yaml
```yaml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
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
properties
```properties
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.hikari.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=60000
```

#### 3. create dao

##### Recommended Package Rule

```text
|-dao
|----{dataBaseName} 
|-----------jpa <- put dao java
|------------------entity  <- put entity java
|------------------repository <- put repository java
```

##### Entity

```java

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    private String account;

    private String name;

}
```

##### Repository

```java
public interface UsersRepository extends CrudRepository<UsersEntity, String>{
    
}
```

##### Dao

- configuration `@Service`
- must extend `AbstractDataDao<T extends Repository>` and through implicit variable `repository` or `entityManager` to do logic 

```java
@Service
public class UsersDao extends AbstractDataDao<UsersRepository> {

    public Optional<UsersEntity> findById(String Id) {
        return repository.findById(Id);
    }

    public void save(UsersEntity entity) {
        repository.save(entity);
    }

}
```

##### Svc

```java
@Svc(desc = "user login")
public class UserLogin extends AbstractSvcLogic<UserLoginReq, LoginRes> {

    @Autowired
    UsersDao usersDao;

    @Override
    public LoginRes doService(UserLoginReq reqModel, LoginRes resModel) {
        Optional<UsersEntity> usersEntityOptional = usersDao.findById(reqModel.getAccount());
        if (usersEntityOptional.isEmpty()) {
            return returnSvcProcess("XXXX");
        }
        UsersEntity usersEntity = usersEntityOptional.get();
        return resModel;
    }

}
```

> **Note** <br>
> Above practice AbstractDataDao is only by one database use,if you need use multiple database,must implements spring data jpa configuration by yourself and dao or new parent extends AbstractDao

## Properties

| Name                     | Required | Default Value |          Description           |
|:-------------------------|:--------:|:-------------:|:------------------------------:|
| `mingle.svc.dao.logging` |          |   `disable`   | aop logging for dao set enable |

please watch .A.5. Data Properties `spring.datasource` properties

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#appendix.application-properties.data
