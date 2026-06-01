# 🎾 테리우스 (Terrius) — Backend

> **당신의 모든 랠리를 데이터로 기록합니다.**
>
> _"인생네컷처럼 기록하고, 프로 선수처럼 분석받는다."_

코트에 설치된 카메라로 촬영된 테니스 경기 영상을 **하이라이트 클립**으로 남기는 것을 넘어,
**자세 분석 · 경기 분석 데이터 리포트**까지 제공하는 테니스 성장 리포트 서비스의 백엔드입니다.

사용자가 경기 영상에 리포트를 신청하면, 무거운 AI 처리는 내부 중계 서버(**FastAPI**)에 위임하고
그 결과를 가공·저장해 앱에 전달합니다. Spring Boot 기반 모놀리식 애플리케이션이며,
**카카오 OAuth2 로그인 + JWT 인증**, **AWS S3 영상 저장**, **FastAPI 분석 연동**을 담당합니다.

---

## 🔹 주요 기능

### 1. 🔐 인증 / 인가
- 카카오 OAuth2 소셜 로그인
- 로그인 성공 시 JWT(Access Token) 발급 → 이후 모든 요청은 `Authorization: Bearer <token>` 헤더 사용
- 사용자 정보 조회

### 2. 🏟️ 경기장 / 코트 조회
- 경기장 목록 조회 (지역·도시·이름 필터)
- 경기장 상세 조회
- 코트별 경기 영상이 존재하는 날짜 / 시간대 조회

### 3. 🎥 경기 영상 & 리포트 신청
- 코트에서 촬영된 경기 영상(`MatchVideo`) 관리
- 사용자가 특정 경기 영상에 대해 **리포트 분석 신청**
- 신청 가능한 영상 목록 / 신청 현황 조회

### 4. 🤖 AI 분석 (FastAPI 연동)
- 분석 서버 헬스 체크
- 경기 영상 분석 요청 → 결과 수신 후 리포트 생성
- (개발용) 고정 CSV 기반 분석 테스트 엔드포인트

### 5. 📊 리포트 조회
- 내 리포트 목록 조회 (정렬 지원)
- 선수별(`PLAYER_ONE` / `PLAYER_TWO`) 리포트 상세 조회

리포트는 다음 3단위의 분석 데이터로 구성됩니다.

| 단위 | 내용 |
|---|---|
| 🎯 **경기(Match)** | 최고 타구 속도, 평균/최대/최소 랠리 횟수, 총 샷 수, 퍼스트/세컨드 서브 성공률 및 비중 |
| 🎾 **샷(Shot)** | 샷 종류(서브·포핸드·백핸드·발리·스매시) 자동 태깅, 방향 및 코트 내 착지점 |
| 👤 **자세(Biomechanics)** | 샷별 어깨/척추/허리 회전각, 개선 포인트, 점수 |

- 자세 교정이 필요한 구간의 **하이라이트 영상** 제공

---

## 🚀 기술 스택

- **Language / Runtime**: Java 21 (Temurin)

  <img src="https://img.shields.io/badge/Java%2021-007396?style=flat-square&logo=openjdk&logoColor=white" />

- **Framework**: Spring Boot 4.0.3, Spring Web, Spring Data JPA

  <img src="https://img.shields.io/badge/Spring%20Boot%204.0.3-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Web-6DB33F?style=flat-square&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-59666C?style=flat-square&logo=hibernate&logoColor=white" />

- **Database**: MySQL (Hibernate / JPA)

  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white" />

- **Auth**: Kakao OAuth2 Login, JWT (jjwt 0.12.3)

  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/Kakao%20Login-FFCD00?style=flat-square&logo=kakao&logoColor=black" />
  <img src="https://img.shields.io/badge/JWT%20(jjwt%200.12.3)-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" />

- **Storage**: AWS S3 (spring-cloud-aws 4.0.0)

  <img src="https://img.shields.io/badge/AWS%20S3-569A31?style=flat-square&logo=amazons3&logoColor=white" />
  <img src="https://img.shields.io/badge/spring--cloud--aws-FF9900?style=flat-square&logo=amazonaws&logoColor=white" />

- **AI 연동**: FastAPI (RestClient 기반 HTTP 통신)

  <img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white" />

- **Build**: Gradle (Wrapper)

  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" />

- **Etc.**: Lombok, JPA Auditing

  <img src="https://img.shields.io/badge/Lombok-BC2323?style=flat-square&logo=lombok&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA%20Auditing-0FAAFF?style=flat-square&logo=clockify&logoColor=white" />

---

## 🏗️ 아키텍처 개요

```
📱 Mobile App
    │  카카오 로그인 · JWT
    ▼
🎾 Terrius Backend (Spring Boot)  ⇄  🤖 FastAPI 분석 서버  (모션 · 샷 분석)
    │
    ├─▶  🗄️ MySQL        (도메인 데이터)
    └─▶  🪣 AWS S3        (영상 / 리포트)
```

- 무거운 AI 분석은 **FastAPI 서버로 위임**하고, 백엔드는 도메인 데이터(사용자·경기장·영상·리포트)와 인증을 담당합니다.
- FastAPI 통신은 타임아웃이 설정된 `RestClient`(`FastApiAnalysisClient`)로 처리합니다.

---

## 📁 패키지 구조

```
src/main/java/hansung/org/terrius
├─ domain
│  ├─ user       # 사용자, 카카오 OAuth2, JWT 발급
│  ├─ stadium    # 경기장 · 코트, 영상/리포트 신청 조회
│  ├─ match      # 경기 영상(MatchVideo), 경기 유형
│  └─ report     # 리포트 · 모션분석 · 하이라이트
│     └─ analysis  # FastAPI 분석 클라이언트 / 요청·응답 DTO
└─ global
   ├─ config     # Security, S3, Web 설정
   ├─ jwt        # JWT 필터 · 토큰 Provider · UserDetails
   ├─ response   # 공통 응답(BaseResponse / Success / Error)
   ├─ exception  # 전역 예외 처리
   ├─ entity     # BaseEntity (JPA Auditing)
   ├─ s3         # S3Service
   └─ init       # 초기 데이터 로더
```

---

## 🔌 주요 API

| Method | Endpoint | 설명 | 인증 |
|---|---|---|:---:|
| GET | `/users` | 내 사용자 정보 조회 | ✅ |
| GET | `/stadiums` | 경기장 목록 (province/city/name 필터) | — |
| GET | `/stadiums/{stadiumId}` | 경기장 상세 | — |
| GET | `/stadiums/{stadiumId}/report-downloads/dates` | 리포트 신청 가능 날짜 | — |
| GET | `/stadiums/{stadiumId}/report-downloads/times` | 해당 날짜의 경기 영상 시간대 | — |
| GET | `/stadiums/{stadiumId}/report-requests` | 리포트 신청 현황 | — |
| POST | `/stadiums/{stadiumId}/report-requests/{matchVideoId}` | 리포트 분석 신청 | ✅ |
| GET | `/reports/my` | 내 리포트 목록 (`sort` 정렬) | ✅ |
| GET | `/reports/match-videos/{matchVideoId}` | 선수별(`target`) 리포트 상세 | ✅ |
| POST | `/reports/match-videos/{matchVideoId}/download` | 리포트 다운로드 | ✅ |
| GET | `/reports/analysis/health` | FastAPI 분석 서버 헬스 체크 | ✅ |
| POST | `/reports/analysis/match` | 경기 영상 분석 요청 | ✅ |

---

## 👥 팀 구성 (Backend)

<table>
  <tr>
    <td align="center" width="180">
      <a href="https://github.com/ThreeeJ">
        <img src="https://github.com/ThreeeJ.png" width="120" height="120" style="border-radius:50%" /><br/>
        <b>정종진</b>
      </a>
    </td>
    <td align="center" width="180">
      <a href="https://github.com/oroi2009">
        <img src="https://github.com/oroi2009.png" width="120" height="120" style="border-radius:50%" /><br/>
        <b>천성진</b>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">Backend</td>
    <td align="center">Backend</td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/ThreeeJ">@ThreeeJ</a></td>
    <td align="center"><a href="https://github.com/oroi2009">@oroi2009</a></td>
  </tr>
</table>
