# BackGround

[쿼리 실행 순서](https://zu-techlog.tistory.com/29)

```text
FROM, JOIN > WHERE, GROUP BY, HAVING > SELECT > ORDER BY
```

[쿼리 실행 순서(최적화)](https://stackoverflow.com/questions/10133356/where-clause-before-inner-join)

```text
inner join의 경우엔 where절이 먼저 실행된다.
```


[JPQL에서 JOIN ON](https://taegyunwoo.github.io/jpa/JPA_ObjectQuery_JPQL_Join#15)

여기서 `JOIN ON 절` 쪽을 보면

* ON 절을 사용하면, 조인 대상을 필터링하고 조인할 수 있다.
    * 즉 ON 절의 조건으로 ‘원하는 조인 대상’을 걸러내고, 그에 대한 결과 테이블로 조인시킨다.
* 보통 ON 절은 외부 조인에서만 사용한다.
    * 왜냐하면 내부 조인에서 ON 절을 사용하면, WHERE 절을 사용하는 것과 결과가 같기 때문이다.

이는 쿼리실행 순서에 반하는데, JOIN 전에 WHERE를 칠 수 있는 좋은 방법이다.

```text
ON 절이 JOIN 전에 수행되어 `JOIN 전에 WHERE를 칠 수 있는 방법` 이라는건 이해가 되는데
`보통 ON절은 외부 조인에서만 사용한다.` 라는 표현이 이해가 잘 안됀다.

일단 ON절에 TABLE.NAME = 'implermine' 같이 WHERE같은 조건을 넣을 수 있는건 이해했다.

생각해보니, inner join이든, outer join이든 그냥 join전에 처리하고 싶은것이 있으면
ON절에 넣으면 된다.

그런데, INNER JOIN의 경우엔 WHERE절로 처리해도 될 뿐이다.
```
