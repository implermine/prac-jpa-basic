J2SE에선 EntityManager와 PersistenceContext가 1:1 이지만,


J2EE나 Spring에선 EntityManager와 PersistenceContext가 N:1 이다.


EntityManager는 PersistenceContext의 기능을 활용 할 수 있도록 제공된 인터페이스 이다.

사실상 같은 의미라고 봐도되며,

> 엔티티 매니저 == 영속성 컨텍스트

영속성 컨텍스트는 트랜잭션 내에서만 의미가 있다.

따라서, 영속성 컨텍스트 (WAS side) 는 트랜잭션(DB side) 를 빌리며

em.flush()와 tx.commit()의 주체가 다른 이유도 이 때문이다.

