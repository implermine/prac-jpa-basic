## Ref
[Hibernate @NotFound](https://thorben-janssen.com/hibernates-notfound/)

## NotFound default docs 들어가면 나오는 내용
NotFound action is... 

Possible actions when the database contains a non-null fk with no matching target.

그러니까, fk는 있고, non-null 할 때(값이 있을 때), 그 fk로 찾은 부모 객체가 존재하지 않을 때. 에 관한 내용이다.

개인적으로, 비식별관계인 Database에서 충분히 존재할 수 있는 문제라고 생각한다.

관계형 데이터베이스의 무결성이 꼭 지켜져야 하는가? 에 대한 도전적인 질문이지만 이러한 상황이 일어날 수 있다는것에는
충분히 가능성을 열어둬야 한다고 생각한다.


## NotFound의 효과

1. Hibernate `assumes that the table model doesn’t define a foreign key constraint for that association` and `doesn’t generate one if it generates the table model`.
```text
해당 Association(연관관계)에 대해서는 FK 제약조건이 없다고 가정하고, ddl-auto를 통해 Table을 Generate 할 때,
Alter Table ... 하고 FK 제약조건을 집어넣어주지 않는다.

이는 위의 NotFound default docs 들어가면 나오는 내용에서 미루어 알 수 있듯이,

fk가 non-null 한데, 그 fk로 주어진 부모객체가 없다는 것은 `외래키 제약 조건`을 위반하는 행위이고,

이런 비정규화 데이터베이스에서 발생하는 일에 대해서 `어떻게 핸들링 할 지`를 다루는 annotation이므로, `외래키 제약 조건`이 존재하지

않는 상황이라 가정하고 1. 과 같은 일이 수행된다.
```

2. You define if Hibernate shall `ignore` broken foreign key references `or` `throw an exception`.

```text
broken foreign key reference는 `참조 키(외래키) 제약 조건`이 broken 되었다는 것을 의미한다.

이런, broken 된 `참조 키 제약 조건` 에 대해서 어떻게 handling할 지, 이 @NotFound 어노테이션을 통해

제어할 수 있음을 뜻한다.

```


3. Hibernate fetches the association eagerly, even if you set its FetchType to LAZY.

```text
`JPA는 기본적으로 참조 키 제약조건이 broken 된 것을 용인하고 Proxying하지 않는다.`

따라서, Lazy-Loading이 불가하다.

이는 사뭇 @OneToOne 관계에서 연관관계의 주인이 아닌측의 행동과 유사한데. ( https://stackoverflow.com/a/1445694 <- 그렇게 도움되진 않음.)

연관관계의 주인이 아닌 측은, 참조 무결성 제약조건에의해 반대편 값의 존재여부를 알 수 없기 때문에

not null이 보장되야 하는 proxying을 할 수 없다.

proxy가 not null이 보장되야 하는 이유는,

this = null; 이란 구문이 존재하지 않기 때문에 (https://stackoverflow.com/questions/18293594/is-it-possible-to-set-an-object-to-be-null-from-within-itself)

proxy 내부에서 this object를 null하게 만들 방법이 없다.

따라서, proxy에 lazy하게 null을 fetching 할 수 없기 때문에 lazy-loading이 불가하다.

근데 왜 @NotFound가 아닌 상황에선 Exception(NotFoundException)이 있는데 이렇게 해 둔 걸까?
적어도 action이 ignore가 아닌 exception을 발생시킬 만 한데, 왜 늘 EAGER일까...

아무래도 runtime exception을 방지하고자하는 JPA의 컨셉 아닐까..
```


## 결론짓자면...

broken fk constraint를 사용하고, 자식 객체는 전부 의미있다고 가정하고 가져오고 싶다면..(Member)

3가지 방법이 존재한다.

1. @NotFound(action=IGNORE)를 사용한다.

이는 Forced-EAGER Loading을 야기한다.
broken fk constraint에 대해 null을 주입한다.

2. @NotFound(action=EXCEPTION) (default)를 사용한다.

이는 Forced-EAGER Loading을 야기한다.

broken fk constraint에 대해 주테이블(자식테이블, FK를 가진 측 테이블)도 NULL이라 단언해버린다.

뭐가 바뀐건진 모르겠는데, Hibernate 버그인가? EXCEPTION이 발생해야 될 것 같은데,
오히려 EXCEPTION없이 persistence context에 로딩자체를 안해버린다.
오히려 EntityNotFoundException을 사용하는게 나을 정도.

em.find를 하면 Exception발생 없이 걍 NULL 단언함.
그러나 em.createQuery를 이용한 JPQL을 이용해서 강제 join fetch를 수행하면
Fetch어쩌고Exception 발생

3. @NotFound없이 FK 조건만 없애거나, 그러한 Legacy Database를 사용할 경우

이는 Force-EAGER Loading이 존재하지 않는다.
다만 fk 로 lazy loading을 수행 할 때 마다,  EntityNotFoundException을 터트리므로,
try-catch를 걸어서 exception이 발생한 Entity마다 association을 null로 재설정 해야된다.

4. fetch join을 쓰는 경우

2번과 같은 결과를 보인다.


그럼 어캄? 어카냐 진짜 ㅋㅋㅋㅋ

