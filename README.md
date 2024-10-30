# auth-service

Данный микросервис предназначен для создания и проверки JWT токена, а также проверки доступа пользователя к функционалу.

Является частью [этого проекта](https://github.com/vitmvit/core-service/tree/dev)

# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

#### Аутентификация и авторизация:

Реализована аутентификация и авторизация с помощью Spring-Boot и JWT.

Для создания нового пользователя необходимо отправить POST-запрос на конечную точку с телом, содержащим логин, пароль и
одну из доступных ролей.

Доступные роли:

```text
ADMIN, USER, JOURNALIST, SUBSCRIBER
```

## Swagger

http://localhost:8081/api/doc/swagger-ui/index.html

## Реализация

### AuthController

#### POST JwtDto signUp(@RequestBody @Valid SignUpDto dto:

Request:

```http request
http://localhost:8081/api/auth/signUp
```

```json
{
  "login": "Admin10",
  "password": "Admin10",
  "role": "ADMIN"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBZG1pbjEwIiwidXNlcm5hbWUiOiJBZG1pbjEwIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzA5MTY1NzkwfQ.GuRAIDqD_PsJw048K9tp9ayLcX2Wg44R7kz2uosp8bg"
}
```

Если пользователь существует:

```json
{
  "errorMessage": "Username is exists",
  "errorCode": 302
}
```

#### POST JwtDto signIn(@RequestBody @Valid SignInDto dto):

Request:

```http request
http://localhost:8081/api/auth/signIn
```

```json
{
  "login": "Admin10",
  "password": "Admin10"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBZG1pbjEwIiwidXNlcm5hbWUiOiJBZG1pbjEwIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzA5MTY2MjA1fQ.JirTl6UuczKbtWt09kTgUIVwh3MKIPjfoMmkgo-SY-E"
}
```

Если пользователь не найден:

```json
{
  "errorMessage": "Username not exists",
  "errorCode": 302
}
```

#### POST boolean check(@RequestHeader(AUTHORIZATION_HEADER) String auth):

Token in header Authorization

Request:

```http request
http://localhost:8081/api/auth/check

```

Response если токен действителен:

```text
true
```

Response если токен не действителен:

```text
false
```