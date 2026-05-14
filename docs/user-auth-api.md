## ℹ️ 전달사항

<aside>

- 카카오 인가 코드 발급 URL
- `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={KAKAO_CLIENT_ID}&redirect_uri=http://localhost:8080/api/users/kakao/login`
- 현재 회원가입과 로그인은 분리되어 있지 않다.
- 카카오 로그인 성공 후 조회된 이메일 기준으로 우리 서비스 DB에 사용자가 없으면 자동 저장하고, 있으면 기존 사용자로 로그인 처리한다.
- 현재는 `Refresh Token` 없이 `Access Token`만 발급한다.

</aside>

## ☑️ API 기본 정보

| 항목 | 내용 |
| --- | --- |
| API 명 | 카카오 로그인 |
| Method | `GET` |
| URL | `/api/users/kakao/login` |
| 설명 | 카카오 인가 코드를 전달받아 카카오 사용자 조회 후 서비스 로그인 처리 및 JWT Access Token 발급 |

## ☑️ Request Header

| **이름** | **태그** | **설명** |
| --- | --- | --- |
| Content-Type | application/json | 콘텐츠 타입 |

- 본 API는 로그인 진입 API이므로 `Authorization` 헤더는 필요하지 않다.

## ☑️ Query Parameters

| **키** | **타입** | **설명(필수 여부)** |
| --- | --- | --- |
| `code` | `String` | 카카오에서 전달하는 인가 코드 (필수) |

## ☑️ Path Variable

| **키** | **타입** | **설명(필수 여부)** |
| --- | --- | --- |
| - | - | 없음 |

## ☑️ Request Body

| **필드명** | **타입** | **설명(필수 여부)** |
| --- | --- | --- |
| - | - | 없음 |

```json
{}
```

## 🔊 Response

### 200 OK

#### 설명

- 카카오 로그인에 성공했다.
- 우리 서비스 DB에 사용자가 없으면 신규 저장 후 로그인 처리한다.
- 우리 서비스 JWT Access Token을 반환한다.

#### Response Body

| 필드명 | 타입 | 설명 |
| --- | --- | --- |
| `isSuccess` | `Boolean` | 성공 여부 |
| `timestamp` | `String` | 응답 시각 |
| `code` | `String` | 응답 코드 |
| `message` | `String` | 응답 메시지 |
| `httpStatus` | `int` | HTTP 상태 코드 |
| `data.id` | `Long` | 사용자 ID |
| `data.name` | `String` | 사용자 닉네임 |
| `data.token.accessToken` | `String` | 우리 서비스 JWT Access Token |

#### Response Example

```json
{
  "isSuccess": true,
  "timestamp": "2025-08-21 14:30:00",
  "code": "GLOBAL_200",
  "message": "호출에 성공했습니다.",
  "httpStatus": 200,
  "data": {
    "id": 1,
    "name": "terrius-user",
    "token": {
      "accessToken": "<JWT_ACCESS_TOKEN>"
    }
  }
}
```

### 400 Bad Request

#### 발생 가능 케이스

- 카카오 응답 JSON 파싱 실패
- 카카오 사용자 정보 응답에서 이메일, 닉네임 등 필수 정보 누락
- `code` 쿼리 파라미터 타입/형식 오류

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `400` | `USER_400_1` | 카카오 응답을 해석하지 못했습니다. | 카카오 응답 JSON 파싱 실패 |
| `400` | `USER_400_2` | 카카오 계정 정보가 올바르지 않습니다. | 카카오 계정 필수 정보 누락 |
| `400` | `GLOBAL_400_3` | HTTP 요청 파라미터 형식이 잘못되었습니다. | 요청 파라미터 타입 변환 실패 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "USER_400_1",
  "message": "카카오 응답을 해석하지 못했습니다.",
  "httpStatus": 400,
  "data": null
}
```

### 401 Unauthorized

#### 발생 가능 케이스

- 카카오 토큰 요청 실패
- 카카오 응답에 access token 누락
- 카카오 사용자 정보 조회 실패

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `401` | `USER_401_2` | 카카오 인증에 실패했습니다. | 카카오 access token 요청 실패 또는 응답 토큰 누락 |
| `401` | `USER_401_3` | 카카오 사용자 정보를 가져오지 못했습니다. | 카카오 사용자 정보 조회 실패 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "USER_401_2",
  "message": "카카오 인증에 실패했습니다.",
  "httpStatus": 401,
  "data": null
}
```

### 403 Forbidden

#### 발생 가능 케이스

- 현재 이 API 자체는 인증 없이 호출하므로 일반적인 정상 흐름에서는 발생하지 않는다.
- 보안 설정 변경으로 접근 권한 제한이 걸릴 경우 발생 가능하다.

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `403` | `GLOBAL_403` | 해당 요청에 접근 권한이 없습니다. | 접근 권한 없음 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "GLOBAL_403",
  "message": "해당 요청에 접근 권한이 없습니다.",
  "httpStatus": 403,
  "data": null
}
```

### 404 Not Found

#### 발생 가능 케이스

- 잘못된 URL로 요청한 경우

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `404` | `GLOBAL_404` | 존재하지 않는 앤드포인트입니다. 요청 URL을 확인해주세요. | 존재하지 않는 엔드포인트 호출 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "GLOBAL_404",
  "message": "존재하지 않는 앤드포인트입니다. 요청 URL을 확인해주세요.",
  "httpStatus": 404,
  "data": null
}
```

### 405 Method Not Allowed

#### 발생 가능 케이스

- `POST`, `PUT`, `DELETE` 등 지원하지 않는 메서드로 호출한 경우

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `405` | `GLOBAL_405` | 지원하지 않는 HTTP 메소드입니다. | 지원하지 않는 HTTP 메서드 호출 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "GLOBAL_405",
  "message": "지원하지 않는 HTTP 메소드입니다.",
  "httpStatus": 405,
  "data": null
}
```

### 500 Internal Server Error

#### 발생 가능 케이스

- 처리되지 않은 서버 내부 예외 발생

#### Response Code

| HTTP Status | Code | Message | 설명 |
| --- | --- | --- | --- |
| `500` | `GLOBAL_500` | 서버 내부에서 알 수 없는 에러가 발생했습니다. | 서버 내부 예외 |

#### Response Example

```json
{
  "isSuccess": false,
  "timestamp": "2025-08-21 14:31:00",
  "code": "GLOBAL_500",
  "message": "서버 내부에서 알 수 없는 에러가 발생했습니다.",
  "httpStatus": 500,
  "data": null
}
```

## ✅ 동작 방식

1. 클라이언트가 카카오 인가 코드와 함께 `/api/users/kakao/login`을 호출한다.
2. 서버가 인가 코드로 카카오 Access Token을 요청한다.
3. 서버가 카카오 사용자 정보를 조회한다.
4. 이메일 기준으로 사용자 존재 여부를 확인한다.
5. 사용자가 없으면 신규 저장한다.
6. JWT Access Token을 발급한다.
7. 로그인 응답을 반환한다.
