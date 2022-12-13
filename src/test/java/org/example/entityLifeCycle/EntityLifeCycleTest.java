package org.example.entityLifeCycle;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.example.SomeMember;
import org.example.entityLifeCycle.model.IdAndGeneratedValueClass;
import org.example.entityLifeCycle.model.IdClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

/**
 * 엔티티의 생명주기
 *
 * 비영속 (new/transient) : 영속성 컨텍스트로 전혀 관계가 없는 새로운 상태
 *
 * 영속 (managed) : 영속성 컨텍스트에 관리되는 상태
 *
 * 준영속 (detached) : 영속성 컨텍스트에 저장되었다가 분리된 상태
 *
 * 삭제 (removed) : 삭제된 상태
 */
public class EntityLifeCycleTest extends BaseCondition {

    @Test
    @DisplayName("비영속 상태")
    void state_transient(){


        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원1");

        Assertions.assertThat(em.contains(someMember)).isFalse();

    }

    @Test
    @DisplayName("준영속 상태")
    void state_detached(){



        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원1");

        // 영속화
        em.persist(someMember);

        Assertions.assertThat(em.contains(someMember)).isTrue();

        // 준영속화
        em.detach(someMember);

        Assertions.assertThat(em.contains(someMember)).isFalse();

    }

    @Test
    @DisplayName("삭제 상태")
    void state_removed(){


        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원1");

        // 영속화
        em.persist(someMember);

        Assertions.assertThat(em.contains(someMember)).isTrue();

        // 삭제 (삭제 마킹을 해 두고, flush 시, DELETE SQL 생성해서 flush 함)
        // 또한 비영속화 함.
        em.remove(someMember);

        Assertions.assertThat(em.contains(someMember)).isFalse();

    }

    @Test
    @DisplayName("영속상태 일 때, 1차 캐시에서 조회, 추가쿼리가 안나가는것을 확인")
    void no_additional_query_when_select_in_first_cache(){
        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원 1");

        // 1차 캐시에 저장
        em.persist(someMember);

        // DB가 아닌 1차 캐시에서 쿼리하므로 추가 쿼리 발생x
        System.out.println("=== Before Find ===");
        SomeMember someMember1 = em.find(SomeMember.class, 1L);
    }

    @Test
    @DisplayName("영속상태 일 때, 영속 엔티티의 동일성 보장")
    void should_same_when_select_in_same_persistence_context(){
        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원 1");

        // 1차 캐시에 저장
        em.persist(someMember);

        // DB가 아닌 1차 캐시에서 쿼리하므로 추가 쿼리 발생x
        System.out.println("=== Before Find ===");
        SomeMember someMember1 = em.find(SomeMember.class, 1L);
        SomeMember someMember2 = em.find(SomeMember.class, 1L);

        Assertions.assertThat(someMember1).isSameAs(someMember2);
    }

    @Test
    @DisplayName("엔티티 등록, 트랜잭션을 지원하는 쓰기 지연")
    void lazy_write(){
        SomeMember someMember1 = new SomeMember();
        someMember1.setId(1L);
        someMember1.setName("회원 1");

        SomeMember someMember2 = new SomeMember();
        someMember2.setId(2L);
        someMember2.setName("회원 2");

        em.persist(someMember1);
        em.persist(someMember2);
        // 여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
        System.out.println("===Before Commit===");

        // 커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
        tx.commit();

    }
    @Test
    @DisplayName("엔티티 수정 , 변경 감지")
    void dirty_check(){
        SomeMember someMember1 = new SomeMember();
        someMember1.setId(1L);
        someMember1.setName("회원 1");

        em.persist(someMember1);

        SomeMember foundSomeMember = em.find(SomeMember.class, 1L);
        // find하는 순간에 스냅샷을 찍는다.

        foundSomeMember.setName("변경: 회원 1");

        System.out.println("===Before Commit===");

        // 커밋하는 순간 데이터베이스에 UPDATE SQL을 보낸다. 라고 생각했는데,
        // 신기하게도 INSERT와 UPDATE를 둘 다 한다.
        /**
         * 차근차근 생각해보면,
         *
         * em.persist(member)를 한 순간,
         * 1차 캐시에 @Id와 Entity, 그리고 INSERT SQL을 쓰기 지연 SQL 저장소에 등록하였다.
         * 이는 flush할 때 쓰기 지연 SQL 저장소에 쿼리를 저장하는 update나 delete와는 다르다.
         *
         * 그 이후,
         * em.find(member) 는 의미가 없고.
         *
         * tx.commit()을 수행할 때,
         * flush가 수행되며 엔티티와 스냅샷 비교, 그 이후 SQL UPDATE 쿼리가 쓰기 지연 SQL 저장소에 저장된다.
         *
         * 그리하여, 쌓인 INSERT 쿼리와 UPDATE 쿼리 즉, 2종류의 쿼리가 발생하는 것.
         *
         * 따라서, 이런 상황이 발생하지 않게 하려면, em.persist()를 정확히 이해하고,
         *
         * 하나의 tx에서 insert 전에 변경사항을 반영 후에 persist를 하도록하면 이런일이 발생하지 않았다.
         */
        tx.commit();

    }

    /**
     * out-of-scope
     */
    @Test
    @DisplayName("준영속 객체가 persist 되었을 때")
    void test() {
        /**
         * @GeneratedValue 어노테이션이 없는 @Id가 채워져있을 경우,
         * 준영속 관련 에러가 발생하지 않는다.
         *
         * 이는 비영속 상태라고 간주되기 때문.
         *
         * @GeneratedValue 어노테이션이 있는 @Id가 채워져있을 경우,
         * 준영속 관련 에러가 발생한다.
         *
         * 이는 준영속 상태라고 간주되기 때문,
         * 왜냐하면 @GeneratedValue가 존재하는 @Id는 DB에 persist-flush 과정을 겪어야만
         * @Id가 존재하기 때문이다.
         *
         * 준영속 객체는 persist가 아닌, merge를 사용해야한다.
         */

        IdAndGeneratedValueClass obj = new IdAndGeneratedValueClass();
        obj.setId(1L);

        // detached entity passed to persist: org.example.entityLifeCycle.model.IdAndGeneratedValueClass

        try{
            em.persist(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(divider);

        IdClass obj2 = new IdClass();
        obj2.setId(1L);

        try{
            em.persist(obj2);
        }catch (Exception e){
            e.printStackTrace();
        }



        //PersistentObjectException이 hibernate단에서 먼저 던져지고 javax가 받아서 변환한다.
    }

}
