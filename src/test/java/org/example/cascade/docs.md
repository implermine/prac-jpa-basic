## 보면 좋은것
* [CASCADETYPE-PERSIST를 함부로 사용하면 안되는 이유](https://joont92.github.io/jpa/CascadeType-PERSIST%EB%A5%BC-%ED%95%A8%EB%B6%80%EB%A1%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A9%B4-%EC%95%88%EB%90%98%EB%8A%94-%EC%9D%B4%EC%9C%A0/)
* [cascade vs orphanRemoval](https://www.objectdb.com/java/jpa/persistence/delete#Orphan_Removal_)
* [hibernate Bug of using sole orphanRemoval feature without cascade](https://www.inflearn.com/questions/137740)



## Miscellaneous
cascade는 예를들어, `부모` 엔티티를 저장할 때 `자식` 엔티티도 같이 저장하고 싶을 때 사용한다.

cascade는 연관관계와는 아무런 연관이 없으며, 영속화와 관련되어 있다.

## 언제 사용할만한가?
두 연관관계가 sole한 경우,
예를들어, 게시물은 게시물의 주인과 연관관계를 가졌지만,
비즈니스상 게시물은 게시물의 주인이 사라져도 남아있어야한다. 
따라서 이런경우 sole하지 않다고 판단하고 cascade를 지양한다.
+) 다시말해, 두 엔티티의 라이프사이클(혹은 수명)이 같다고 판단되면 사용해 볼 법 하다.

## CASCADE의 종류
* ALL: 모두 적용
* PERSIST: 영속
* REMOVE: 삭제
* MERGE: 병합
* REFRESH
* DETACH

## orphan
고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제

여기서 연관관계가 끊어졌다는 의미는,

DB layer에서 부모 레코드가 삭제된 의미가 아니다.

정규화된 Database에선 fk가 바라보는 Parent Record는 삭제될 수 없다(Constraint)

따라서, 여기서 말하는 `고아`는 객체 즉 WAS단의 이야기 이고,

자식이 부모를 잃어버린 경우가 아닌,
```java
child.setParent(null);
```

부모가 자식을 버린 경우이다.
```java
parent.getChildList().remove(child);
```

마찬가지로 둘의 관계가 sole하며 수명이 같을 때 사용한다.

## Cascade.ALL 및 orphanRemoval을 둘다 키면

부모 엔티티를 통해 자식 엔티티의 수명(및 라이프사이클)을 관리할 수 있다.
DDD에 유리 ...?(Aggregate Root)

## CascadeType = REMOVE 와 orphanRemoval의 차이

orphanRemoval(+PERSIST)가 더 큰 의미이다.
orphanRemoval은 @OneToOne일 경우 set(null) 하거나 @OneToMany일 경우 remove(child) 하면서
자식 엔티티를 삭제할 수 있으나,

CascadeType = REMOVE는 em.remove(parent) 에만 동작한다.