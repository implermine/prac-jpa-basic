# When
## NoFk / Team is Deleted

### findById(em.find) [단건 조회] / EAGER
left outer join과 inner join 모두 null 값을 가져오며

의도 된 (자연스러운) 동작이다.

---

### findById(em.find) [단건 조회] / LAZY

join없이 쿼리가 나가며

lazy-loading을 위해 쿼리가 나가면서 EntityNotFoundException이 발생한다.

이 점에 대해서는 나도 어느정도 공감한다, @NotFound도 그냥 이렇게, Runtime Exception을 내면 안되나? 왜 굳이 EAGER하려는거지?

---

### findAll(em.createQuerty(jpql)) [다건 조회] / EAGER

join 쿼리가 발생하진 않는다. (JPQL로 그냥 JOIN없이 쿼리 친거라서)

그렇지만 EAGER에 의해서, N+1 쿼리를 발생시킨다.

이때 EntityNotFoundException이 발생한다.

---

### findAll(em.createQuerty(jpql)) [다건 조회] / LAZY

join 쿼리는 당연히 발생하지 않으며
LAZY하게 쿼리를 발생시키며 EntityNotFoundException을 낼 수 있다.


# 그럼 어캄?

나도 모름, 어캄 진짜