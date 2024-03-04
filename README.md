# auth-service

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

## Реализация

### AuthController

#### POST запрос на создание нового пользователя:

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

#### POST запрос на получение токена аутентификации:

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

#### POST запрос на проверку пользователя и его токена:

Request:

```http request
http://localhost:8081/api/auth/check?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpaSIsInVzZXJuYW1lIjoiaWkiLCJyb2xlIjoiU1VCU0NSSUJFUiIsImV4cCI6MTcwOTQ3ODAzOH0.h2h3MJaAliZQDaf86x5b9PWQAHKvlkYy-TEwW2WiI8Q&login=ii

```

Response если токен действителен и пользователь имеет доступ к данному функционалу:

```text
true
```

Response если токен не действителен или пользователь не имеет доступа к данному функционалу:

```text
false
```


