# 리더보드 예제

http://blog.zepinos.com 에 올린 Ranking(Leaderboard) 에 대한 예제 프로그램입니다.

## Embeded Redis Server 설정

Embedded Redis Server 을 사용하기 위해서는 Spring Profile 에 *embeddedRedisServer* Profile 이 추가되어야 합니다.
~~~
-Dspring.profiles.active=embeddedRedisServer
~~~
