# LAZY가 불가한 측

OneToOne 관계에서, FK를 갖지 않은 `연관관계 미주인` 측에서 `연관관계 주인`     
즉, FK를 가진측을 LAZY하게 호출 할 수 `없다`.

# 그 이유
https://woodcock.tistory.com/23

```text
That’s because Hibernate needs to know if it shall initialize the manuscript attribute with null or a proxy class.
It can only find that out, by querying the manuscript table to find a record that references this Book entity.
The Hibernate team decided that if they have to query the manuscript table anyways, it’s best to fetch the associated entity eagerly.
```

```
그 이유는 Hibernate가 null 또는 프록시 클래스로 원고 속성을 초기화해야 하는지 알아야 하기 때문입니다.

그것은 오직 원고 테이블을 쿼리하여 이 책의 실체를 참조하는 기록을 찾는 것으로 그것을 알아낼 수 있다.

Hibernate 팀은 어차피 원고 테이블을 쿼리해야 한다면 관련 엔티티를 열심히 가져오는 것이 가장 좋다고 결정했습니다.
```

블로그의 글을 보면 `컬렉션이 아니기 때문에 값이 없을 경우 null이 와야 한다.`라고 서술한다.

이는 Optional spec이 도입되기전에 Hibernate가 정립되었을 것이라는 예상을 할 수 있게 한다.

그리고 더 나아가, 컬렉션이였다면 내가 No-Arg-Constructor에 new ArrayList<>(); 를 하지 않았더라도 늘 NOT NULL하게
emptyList를 넣어줄 수 있다는 반증이기도 하다.

확인해보면. empty가 맞다.

즉, Optional이 없는 상황에서의 한계라고 생각 할 수 있다.
