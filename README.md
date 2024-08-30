## Monetization-System

### ✍ 프로젝트 소개
동영상을 등록하여 해당 동영상과 광고의 시청 기록을 바탕으로
통계 및 정산 데이터를 추출하는 시스템입니다. 

통계 및 정산 작업은 배치 작업을 통해 이루어지며<br>
1일, 1주일, 1달의 기간동안의<br>
총 정산금액, 영상별 정산금액, 광고별 정산금액을 확인할 수 있습니다.

- 1주일: 현재 날짜의 월요일부터 일요일까지의 기간
- 1달: 현재 날짜의 1일부터 31일까지의 기간


배치 프로젝트: https://github.com/wonjune0426/Monetization-System_Batch

### 🛠 기술스택
<img src="https://img.shields.io/badge/language-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/21-515151?style=for-the-badge">
<br>
<img src="https://img.shields.io/badge/framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.3.0-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/6.3.0-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/springbatch-6DB33F?style=for-the-badge&logo=springbatch&logoColor=white"><img src="https://img.shields.io/badge/5.1.2-515151?style=for-the-badge">
<br>
<img src="https://img.shields.io/badge/Containers and Deployment-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/26.1.4-515151?style=for-the-badge">
<br>
<img src="https://img.shields.io/badge/CI/CD-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">
<br>
<img src="https://img.shields.io/badge/DB-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/8.4.1-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/3.3.0-515151?style=for-the-badge">
<br>
<img src="https://img.shields.io/badge/Build Tool-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/gradle-4479A1?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/8.8-515151?style=for-the-badge">

### ⚙ Structure
- AWS 배포 작업 후 추가 

### 📊 ERD
https://drawsql.app/teams/team-3679/diagrams/erd
![drawSQL-image-export-2024-07-19](https://github.com/user-attachments/assets/ab0b3974-2622-472f-8f2a-eeead452b209)

### ‼ 핵심 기능 
- JWT/Spring Security를 통한 인증/인가
- CQRS 패턴을 통한 DB 부하 분산
- Redis를 통해 어뷰징 방지 및 마지막 시청 시간 관리
- Spring Batch를 통한 비디오와 광고에 대한 통계 및 정산을 실시

### 🗒API 명세
- https://documenter.getpostman.com/view/19618546/2sA3kUG2cw

### 성능개선
- [spring batch 성능개선 기록](www.naver.com)
- 1차 개선 : chunk, paging size 증가
- 2차 개선 : job의 병렬처리 수행

|  | 개선 전 | 1차 | 2차 |
| :---: | :---: | :---: | :---: |
| 영상 조회 기록 (2천만 건) | 2 | 3 | 0 |
| 광고 조회 기록 (6천만 건) | 5 | 6 | 0 |
| 총 합 | 1 | 1 | 0 |

### 동시성처리
- 동시성 처리 관련 내용 추가
