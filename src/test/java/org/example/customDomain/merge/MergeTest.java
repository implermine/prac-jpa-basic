package org.example.customDomain.merge;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.example.relation.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Ref.
 * merge 스펙
 * https://stackoverflow.com/questions/4509086/what-is-the-difference-between-persist-and-merge-in-jpa-and-hibernate/4509389#4509389
 *
 *
 * https://stackoverflow.com/questions/66939760/merge-is-not-working-in-the-context-of-jpa
 */


/**
 * merge는 표면적으론 UPSERT 이며, (발생하는 양상을 보았을 때,)
 * 그 의의는 같은 @Id에 데이터를 병합하는것 그 이상 그이하도 없음.
 * <p>
 * 다음과 같은 경우로 나뉠 수 있다. (save()를 호출 할 때, persist와 merge가 isNew()에 의해 분기되는것은 차치하고)
 * <p>
 * 1. 영속성 컨텍스트에 해당 엔티티가 존재하지 않을 때,
 * 1-1. DB에서 불러와서 영속화가 불가할 때,
 * 1-2. DB에서 불러와서 영속화가 가능할 때,
 * 1-2-1. 값이 다를 때
 * 1-2-2. 값이 같을 때
 * <p>
 * 2. 영속성 컨텍스트에 해당 엔티티가 존재할 때,
 * 2-1. 값이 다를 때,
 * 2-2. 값이 같을 때.
 */
public class MergeTest extends BaseCondition {

    @Nested
    @DisplayName("영속성 컨텍스트에 해당 엔티티가 존재하지 않을 때")
    class case1 {
        @Test
        @DisplayName("DB에서 불러와서 영속화가 불가할 때")
        /**
         * 이는 New라고 판단되며,
         * 다음 명제와 같다.
         *
         * If X is a new entity instance,
         * a new managed entity instance X' is created and the state of X is copied into the new managed entity instance X'.
         *
         * 따라서, merge의 반환값은 X가 아닌 X'이다.
         */
        void case1_1() {
            /**
             * 영속성 컨텍스트에도 해당 엔티티가 존재하지 않으며,
             * DB에서도 가져올 수 없는 상황
             */

            Member memberA = new Member(1L, "memberA");

            // 값을 merge 하려고 보니 없음, 없으니 SELECT, 이제 있으니 값 병합.
            Member mergedMember = em.merge(memberA); // SELECT 쿼리 발생

            em.flush(); // INSERT 쿼리 발생

            /**
             * [Expect]
             *
             * em.merge 할 때, SELECT 쿼리 하나 발생하고,
             * em.flush 할 때, 그냥 INSERT 쿼리 발생
             *
             * 이는 UPSERT 중 INSERT의 행동양상과 같다.
             */


            assertThat(em.contains(memberA)).isFalse();
            assertThat(em.contains(mergedMember)).isTrue();
            assertThat(memberA).isNotSameAs(mergedMember);
        }

        @Nested
        @DisplayName("DB에서 불러와서 영속화가 가능할 때")
        class case1_2 {

            /**
             * 가장 많이 발생하는 케이스라 생각한다.
             * <p>
             * sequence를 사용하지 않아, isNew()에서 new하지 않다고 판단되었고, merge를 타게 되었으며,
             * DB엔 데이터가 존재한다면?
             */
            @Test
            @DisplayName("값이 다를 때")
            void case1_2_1() {
                // 미리 INSERT 해두기
                Member memberA = new Member(1L, "memberA");
                em.persist(memberA);
                em.flush();
                em.clear();

                line("INSERT 끝");

                Member memberB = new Member(1L, "memberA");
                em.merge(memberB);

                /**
                 * merge하려고, SELECT 쿼리 날리고, 있으니까 영속화하고, merge한 후에 UPDATE 쿼리 안나감.
                 */
                em.flush();

            }

            @Test
            @DisplayName("값이 다를 때")
            void case1_2_2() {
                // 미리 INSERT 해두기
                Member memberA = new Member(1L, "memberA");
                em.persist(memberA);
                em.flush();
                em.clear();

                line("INSERT 끝");

                Member memberB = new Member(1L, "memberB"); //memberB는 ID로 SELECT 후 영속화가 가능하기에 detached라고 간주된다.

                /**
                 * 여기서 일어나는 일.
                 * If X is a detached entity,
                 *  the state of X is copied onto a pre-existing managed entity instance X' of the same identity
                 *  or a new managed copy X' of X is created.
                 *
                 *
                 * 1. memberB는 detached entity이다.
                 * 2. memberB의 상태(state = username)는 X`(=memberA)로 copy된다.
                 */
                em.merge(memberB);

                /**
                 * merge하려고, SELECT 쿼리 날리고, 있으니까 영속화하고, merge한 후에 UPDATE 쿼리 날린다.
                 */
                em.flush();

            }


        }
    }

    @Nested
    @DisplayName("영속성 컨텍스트에 해당 엔티티가 존재할 때")
    class case2 {

        @Test
        @DisplayName("값이 다를 때")
        void case2_1() {
            // 영속성 컨텍스트에 해당 엔티티가 존재하며
            Member memberA = new Member(1L, "memberA");
            line("before persist");
            em.persist(memberA);
            line("after persist");


            // 값이 다르다.
            Member memberB = new Member(1L, "memberB");
            line("before merge");
            em.merge(memberB); // 이미 영속성 컨텍스트에 해당 엔티티(같은 @Id를 가진 엔티티)가 존재하니까.
            //따로 SELECT 쿼리를 발생시키지 않음.
            line("after merge");

            line("before flush");
            em.flush(); // 여기서 순서대로
            // 1. INSERT
            // 2. UPDATE 쿼리를 발생시킴. 이 UPDATE 쿼리는 더티체킹을 통해서 발생한 쓰기-지연 쿼리이다.
            // 첫번째로, persist가 일어났을때 초깃값 SNAPSHOT을 찍어 뒀으며,
            // 두번째로, merge가 일어났을 땐, 이미 영속성 컨텍스트에 해당 엔티티(같은 @Id를 가진 엔티티)가 존재하니까.
            // 아무일도 발생하지 않았고, flush가 일어나며 쓰기-지연 UPDATE 쿼리를 생성 한 것이다.
            line("after flush");


            //다시말해 memberB는 detached 상태이며, 값이 다르다.
        }

        @Test
        @DisplayName("값이 같을 때")
        void case2_2() {
            // 영속성 컨텍스트에 해당 엔티티가 존재하며
            Member memberA = new Member(1L, "memberA");
            line("before persist");
            em.persist(memberA);
            line("after persist");


            // 값이 다르다.
            Member memberB = new Member(1L, "memberA");
            line("before merge");
            em.merge(memberB); // 이미 영속성 컨텍스트에 해당 엔티티(같은 @Id를 가진 엔티티)가 존재하니까.
            //따로 SELECT 쿼리를 발생시키지 않음.
            line("after merge");

            line("before flush");
            em.flush(); // 여기서 순서대로
            // 1. INSERT
            // 첫번째로, persist가 일어났을때 초깃값 SNAPSHOT을 찍어 뒀으며,
            // 두번째로, merge가 일어났을 땐, 이미 영속성 컨텍스트에 해당 엔티티(같은 @Id를 가진 엔티티)가 존재하니까.
            // 아무일도 발생하지 않았고, flush가 일어나며 더티체킹을 수행했더니 수정할 사항이 없어서 UPDATE 쿼리가 안나간다.
            line("after flush");


            //다시말해 memberB는 detached 상태이며, 값이 다르다.
        }
    }


    // merge 케이스 분리 이후 내용
    // ===============================================================================================================

    @Test
    @DisplayName("준영속 상태 체크")
    void test() {
        //given
        Member member = new Member(1L, "memberA");

        //when
        em.persist(member);

        //then
        assertThat(em.contains(member)).isTrue(); // member는 영속

        //when
        em.detach(member);

        //then
        assertThat(em.contains(member)).isFalse(); // member는 비영속( 그러나 @Id가 채워져있음 = 준영속)

        //when
        Member mergedMember = em.merge(member); // member는 그대로 비영속 . mergedMember는 영속

        //then
        assertThat(em.contains(mergedMember)).isTrue();
        assertThat(em.contains(member)).isFalse();
        assertThat(member).isNotSameAs(mergedMember);
    }

    @Test
    @DisplayName("SELECT query fires even on detached entity")
    void test2() {
        //given
        Member member = new Member(1L, "memberA");
        em.persist(member);
        super.line("persist");
        em.detach(member); // detach는 PersistenceContext로부터의 Entity 삭제도 야기한다. 그러나 DB에 삭제쿼리를 날리는 행위는 아니다.
        // https://stackoverflow.com/a/18515524/16901221
        super.line("detach");
//        boolean contains = em.contains(member);
//        System.out.println(contains); // false

        // now, Member is detached state, but Entity Information is already registered in PersistenceContext, No

        Member mergedMember = em.merge(member); // is query fires..? yes

        /**
         * https://tech.junhabaek.net/hibernate-jpa-entitymanager-%ED%95%B5%EC%8B%AC-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC-3d0d9ff439a2#2b32
         * 의 `Merge 기본 동작` 에 따르면,
         * @Id에 데이터가 존재하고, Persistence Context에 해당 id의 엔티티가 존재하면,
         * 4번으로 바로 넘어간다, 즉 SELECT 쿼리를 날리지 않는다고 생각해도 됩니다. 이를 test3()에서 구현합니다.
         */

        em.flush();
    }

    @Test
    @DisplayName("Merge 기본 동작")
    void test3() {
        Member member = new Member(1L, "memberA");
        em.persist(member);
        member.setUsername("altered Member name");
        super.line("persist");

        Member mergedMember = em.merge(member); // is query fires..? no

        em.flush();

        // 이번엔 flush 타이밍에 INSERT, UPDATE 쿼리가 나갔다,
        // 생성되는 지연 쓰기 쿼리 저장소에서 각 연산마다 보간을 수행하는것이 아니므로,
        // 순번대로, INSERT, UPDATE 쿼리가 각각 발생했다.
    }

    @Test
    @DisplayName("엔티티가 DB에 존재하는지 체크하고, 없을때만 저장하려고하는데 어떻게 해야해?")
    void test4(){

    }


}
