## Ref
https://thorben-janssen.com/hibernates-notfound/

## NotFound default docs 들어가면 나오는 내용
NotFound action is... 

Possible actions when the database contains a non-null fk with no matching target.

그러니까, fk는 있고, non-null 할 때(값이 있을 때), 그 fk로 찾은 부모 객체가 존재하지 않을 때. 에 관한 내용이다.

## NotFound의 효과

1. Hibernate `assumes that the table model doesn’t define a foreign key constraint for that association` and `doesn’t generate one if it generates the table model`.
```text
해당 Association(연관관계)에 대해서는 FK 제약조건이 없다고 가정하고, ddl-auto를 통해 Table을 Generate 할 때, Alter Table ... 하고 FK 제약조건을 집어넣어주지 않는다.

이는 위의 NotFound default docs 들어가면 나오는 내용에서 미루어 알 수 있듯이,

fk가 non-null 한데, 그 fk로 주어진 부모객체가 없다는 것은 `외래키 제약 조건`을 위반하는 행위이고,

이런 비정규화 데이터베이스에서 발생하는 일에 대해서 `어떻게 핸들링 할 지`를 다루는 annotation이므로, `외래키 제약 조건`이 존재하지

않는 상황이라 가정하고 1. 과 같은 일이 수행된다.
```

2. You define if Hibernate shall `ignore` broken foreign key references or `throw an exception`.

```text
broken foreign key reference는 `참조 키(외래키) 제약 조건`이 contaminate 되었다는 것을 의미한다.

이런, contaminate 된 `참조 키 제약 조건` 에 대해서 어떻게 handling할 지, 이 @NotFound 어노테이션을 통해

제어할 수 있음을 뜻한다.

`JPA는 기본적으로 참조 키 제약조건이 broken 된 것을 용인하고 Proxying하지 않는다.`

```


3. Hibernate fetches the association eagerly, even if you set its FetchType to LAZY.

```text
Hibernate는 @NotFound 어노테이션이 존재하는 연관관계에 대해, 늘 EAGER하게 로딩한다 !!!

추가적으로 이 action은 @OneToOne의 fk를 가지지 않은측이 강제로 EAGERLY하게 로딩하는것과 유사하다.

id로 proxy object를 설정하려면, 그 id로 proxying을 칠 수 있는지 여부를 알아야 하는데,

연관관계를 가지지 않은 측에선, 반대측 id를 알아야만 proxying을 칠 수 있기에, 불가하고.

이 경우에도 이 fk(반대쪽 pk)로 proxying을 치더라도 loading이 가능한지 여부를 모르기에 Eagerly하게 로딩한다.

이 맥락에서, fk-pk로 이루어져있지 않은 관계는 늘 eager한지 알아볼 필요가 있다.
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

