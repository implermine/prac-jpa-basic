# 프로젝션에는 3가지 종류가 있다.
1. 엔티티 프로젝션
```text
SELECT m FROM Member m
```

```text
SELECT m.team FROM Member m
```

2. 임베디드 타입 프로젝션
```text
SELECT m.address FROM Member m
```
3. 스칼라 타입 프로젝션

```text
SELECT m.username, m.age FROM Member m
```

이 중에 엔티티 프로젝션은 프로젝션한 엔티티를 영속화 한다.

따라서 하이버네이트 1차 캐시에서 캐싱된 결과를 반환 할 수 있으며, 더티체킹의 대상이 된다.

---
# JPQL SELECT 절에 사용할 수 있는 alias는 JOIN 과 FROM이 정한다.

```sql
SELECT m FROM Member m
```
이면  m을 사용 가능하고

```sql
SELECT t FROM Member m INNER JOIN m.team t
```

이면 여기서부턴 m과 t 둘 다 사용 가능하다.

또한,

```sql
SELECT t FROM Member m, Team t WHERE m.username = t.name
```
과 같이 FROM 절에 2개 이상의 엔티티를 사용할 수 있다.

---
# 엔티티 프로젝션을 수행할 때, 같은 결과를 내는 두 쿼리 중 하나를 선택해야 한다.

## 1)
```sql
SELECT m.team FROM Member m
```

## 2)
2. d
```sql
SELECT t FROM Member m join m.team t
```


해당 JPQL은 다음과 같은 쿼리가 발생한다.

```sql
SELECT t.*
FROM Member as m
INNER JOIN Team as t
ON m.TEAM_ID = t.ID
```

그런데, `1번`과 같은 쿼리는 이런 JOIN 쿼리를 예상하기 어렵다.
따라서, `2번`과 같이 구성하는게 더 좋다.

> 2번은 JPQL join 쿼리를 어떻게 짜야하는지에 대한 insight도 제공한다.   
> JPQL JOIN 쿼리의 구성은 다음과 같다.

```sql
SELECT [alias for projection] FROM [entity] [alias] JOIN [entity.referEntity] [alias] 
```

따라서, JOIN [x] 에서 원래 테이블 지정하던것을 엔티티가 연관한 또다른 엔티티를 지정한다.
그에따라, 두 엔티티 연관관계에 설정되어있는 FK를 자동으로 사용하므로 ON 절은 필요없다.


