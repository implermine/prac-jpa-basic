package org.example.proxy;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.persistence.PersistenceUnitUtil;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProxyTest extends BaseCondition {

    /**
     * `애초에` JPA는 같은 트랜잭션 안에서 동일 엔티티를 동일하다고 보장해준다.
     */
    @Test
    @DisplayName("프록시 조회 후, 일반 조회를 시도하면, 프록시 조회시에 persistence context에 이미 @Id로 entity를 영속화 시켜뒀기에 그 다음 call은 HibernateProxy이다.")
    // 이와 마찬가지로 영속성 컨텍스트에 이미 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
    void test(){
        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");

        em.persist(member);
        em.flush();
        em.clear();

        Member referenceMember = em.getReference(Member.class, 1L);

        Member findMember = em.find(Member.class, 1L);

        /**
         * findMember도 class.org.example.relation.Member가 아니라. proxy로 감싸져있다.
         */
        assertThat(referenceMember).isSameAs(findMember);

        System.out.println("referenceMember.getClass() = " + referenceMember.getClass().getName());
        System.out.println("findMember.getClass() = " + findMember.getClass().getName());


    }

    @Test
    @DisplayName("Proxy는 Touch하면 Proxy를 깐 상태로 두는가? --> 그렇지 않음")
    void test2(){
        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");

        em.persist(member);
        em.flush();
        em.clear();

        Member referenceMember = em.getReference(Member.class, 1L);

        System.out.println(" \n\n referenceMember.getClass() = " + referenceMember.getClass() + "\n\n");

        String username = referenceMember.getUsername();

        System.out.println("\n\n referenceMember.getClass() = " + referenceMember.getClass() + "\n\n");
    }

    @Test
    @DisplayName("준영속 상태에서 프록시를 초기화하려면 어떤 문제가 발생")
    /**
     * 준영속 상태를 만드는것부터
    */
    void test3(){

        //given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");

        em.persist(member);
        em.flush();
        em.clear();

        // when
        Member referenceMember = em.getReference(Member.class, 1L);
        // 준영속
        em.detach(referenceMember);

        // then
        Executable executable = new Executable(){
            @Override
            public void execute() throws Throwable {
                referenceMember.getUsername();
            }
        };

        assertThrows(LazyInitializationException.class,executable);

    }


    @Test
    @DisplayName("프록시 관련 테스트할때 유용한 메서드들")
    void test4(){
        //given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");

        em.persist(member);
        em.flush();
        em.clear();

        //when
        Member reference = em.getReference(Member.class, 1L);

        // 프록시 인스턴스의 초기화 여부 확인
        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
        boolean loaded = persistenceUnitUtil.isLoaded(reference);
        assertThat(loaded).isFalse();

        // 프록시 인스턴스 강제 초기화(참고로 JPA 표준은 강제 초기화 없음 )
        Hibernate.initialize(reference);
//        org.hibernate.Hibernate.initialize(reference);

        // 프록시 인스턴스의 초기화 여부 확인2
        boolean loaded1 = persistenceUnitUtil.isLoaded(reference);
        assertThat(loaded1).isTrue();


    }
}
