package org.example.customDomain.notFound;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class NotFoundTest extends BaseCondition {


    /**
     * 시나리오
     *
     * memberA, memberB는 Team1에 소속
     * memberC, memberD는 Team2에 소속
     *
     * Team1을 삭제
     */
    @BeforeEach
    void scenario(){
        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
        Member_NotFound memberB = new Member_NotFound(2L, "memberB");
        Member_NotFound memberC = new Member_NotFound(3L, "memberC");
        Member_NotFound memberD = new Member_NotFound(4L, "memberD");
        Member_NotFound memberE = new Member_NotFound(5L, "memberE");
        Team_NotFound team1 = new Team_NotFound(1L,"team1");
        Team_NotFound team2 = new Team_NotFound(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        memberD.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
        em.flush();
        em.clear();


        // 삭제
        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();
        line("After Scenario");
    }

    /**
     * @ManyToOne(fetch = FetchType.EAGER) // 명시적(explicit) EAGER
     * @JoinColumn(name ="TEAM_ID")
     * @NotFound(action = NotFoundAction.EXCEPTION)
     */
    @Test
    @DisplayName("명시적 EAGER + NotFound(exception)")
    void notFound_and_eager(){
        Member_NotFound member = em.find(Member_NotFound.class, 1L);

        Assertions.assertThat(member).isNull(); // NoFK때랑 같은 양상을 보인다.


        /**
         * 재밌는것은,
         * 이때도 EAGER 로딩이라 Hibernate 기본전략인 left outer join을 사용해서 Member를 가져오려 한다.
         * 그러나 이때, left outer join으로 Team1을 불러오지 못했고,
         *
         * 이에 대해서, EAGER 로딩은 다시 또 Proxying을 해결하지 못했기에,
         * 또 하나의 쿼리를 날린다. (SELECT * FROM TEAM t WHERE t.id = 1)
         *
         * 그럼에도 proxy를 해결하지 못했기에 NoFK 단건조회처럼 left outer join과 inner join 모두 null 하다.
         *
         * 또한, 명시적 EAGER의 경우 `양쪽 null 문제` 가 다시 발생한다.
         *
         * 그럼, 묵시적 EAGER (NotFound를 이용한)은?
         * -> NotFound(Exception)인데 exception도 안내고, 걍 양쪽 null 문제 발생함.
         * -> 근데 명시적 EAGER와는 행동양상이 달랐다. join안하고 쿼리나감.
         */
    }

    /**
     * `그러나 아래 LAZY를 보면 이 경우도 EAGER 했기에, null값이 대입되는 것을 알 수 있다.`
     * 를 검색하면 (바로 위)
     * 이 경우는 NotFound 어노테이션에 의한 강제로딩에 의해 EAGER와 행동양상이 같은 것을 알 수 있다.
     *
     * 이 경우에 어차피 추가 쿼리가 나갈 예정이라면, EAGER로 두어서 left outer join을 발생시키는것 보단
     * LAZY로 두어서 쿼리크기라도 줄이는게 나아보인다.
     *
     *
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name ="TEAM_ID")
     * @NotFound(action = NotFoundAction.EXCEPTION)
     */
    @Test
    @DisplayName("LAZY + NotFound(exception)")
    void notFound_and_lazy(){
        Member_NotFound member = em.find(Member_NotFound.class, 1L);

        Assertions.assertThat(member).isNull(); // NoFK때랑 같은 양상을 보인다.
    }

    /**
     * 그나마 차선이라 생각됨. EAGER 로딩이지만 null한
     *
     *
     * @ManyToOne(fetch = FetchType.LAZY) // NotFound를 쓰면 무시 될 예정
     * @JoinColumn(name = "TEAM_ID")
     * @NotFound(action = NotFoundAction.IGNORE)
     */
    @Test
    @DisplayName("EAGER(=LAZY) + NotFound(ignore)")
    void notFound_ignore_and_lazy(){
        Member_NotFound member = em.find(Member_NotFound.class, 1L);


        // Not null 인것을 명심해야한다. 이 경우엔!
        Assertions.assertThat(member).isNotNull();
        Assertions.assertThat(member.getTeam()).isNull();
    }

    /**
     * 다건 + IGNORE
     *
     * 이건 더더욱 흥미로운 케이스이다.
     *
     * NotFound는 LAZY처럼 값을 불러오는게 아니다.
     *
     * NotFound(exception)는 EAGER하게 값을 불러오려고 하나, 영속성 컨텍스트에 존재하지 않으면,
     *
     * 즉, FK가 1로 엮여있는 Team1이 null 하다는것을 매번 확인해야한다(X), 매번 확인 할 필요가 없다.
     * proxy 내에 EntityNotFoundException을 기억 해 놓는다.
     *
     * 따라서, 아래 테스트는 반복문 내에서 총 1 + 3번의 쿼리가 발생한다.
     *
     * 1. Member 조회 (정상)
     * 2. Team1 조회 (정상 EAGER)
     * 3. Team1 조회 (비정상, null 하다는것을 직전 2에서 알았는데 또 쿼리를 날린다.)
     * 4. Team2 조회
     *
     */
    @Test
    @DisplayName("EAGER(=LAZY) + NotFound(ignore) + 다건")
    void notFound_ignore_and_lazy_and_many(){
        List<Member_NotFound> memberList = em.createQuery("SELECT m FROM Member_NotFound m", Member_NotFound.class).getResultList();

        for (Member_NotFound eachMember : memberList) {
            System.out.println("memberName = " + eachMember.getName());
            System.out.println("teamName = " + (eachMember.getTeam()!=null ? eachMember.getTeam().getName() : null));
            System.out.println("====================================================================================");
        }

    }

    /**
     * NotFound의 exception을 잡아서 해결할 수 있는 방법은 없다.
     * 강제 EAGER 이므로.
     */
    @Test
    @DisplayName("EAGER(=LAZY) + NotFound(exception) + 다건")
    void notFound_exception_and_lazy_and_many(){

        List<Member_NotFound> memberList = null;

        try{
            memberList = em.createQuery("SELECT m FROM Member_NotFound m", Member_NotFound.class).getResultList();
        }catch (Exception e){
            //ignore
            System.out.println("stop");
        }

//        for (Member_NotFound eachMember : memberList) {
//            System.out.println("memberName = " + eachMember.getName());
//            System.out.println("teamName = " + (eachMember.getTeam()!=null ? eachMember.getTeam().getName() : null));
//            System.out.println("====================================================================================");
//        }

        System.out.println("hey check memberList");

    }




    // =================================================================================================================


    @Test
    @DisplayName("Not Found exception trigger")
    void test(){
        // given
        /**
         * <시나리오>
         *
         * MemberA에 Team1에 대한 FK를 걸어두고(TEAM_ID = 1)
         *
         * Team1을 삭제하면
         *
         * MemberA는 broken foreign key constraint를 가지게 된다.
         *
         * 이때, NotFound는 어떻게 동작할 것인지
         */



        em.createNativeQuery("INSERT INTO MEMBER_NOTFOUND VALUES (:id,:name,:teamId)")
                .setParameter("id",1L)
                .setParameter("name","memberA")
                .setParameter("teamId", 1L)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO TEAM_NOTFOUND VALUES (:id,:name)")
                .setParameter("id",1L)
                .setParameter("name","teamA")
                .executeUpdate();


        em.createNativeQuery("DELETE FROM TEAM_NOTFOUND WHERE ID = :id")
                .setParameter("id",1L)
                .executeUpdate();


//        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
//        Team_NotFound team1 = new Team_NotFound(1L,"team1");
//        memberA.changeTeam(team1);
//        em.persist(team1);
//        em.persist(memberA);
//        em.flush();
//        em.clear();
//
//        line("member 와 team insert");
//
//        Member_NotFound foundMember = em.find(Member_NotFound.class, 1L); // 여기서 우선 LAZY 설정되어있는데 NotFound때매 EAGER하게 쿼리 나가는것 확인
//        line("member find");
//
//        em.remove(foundMember.getTeam());
//        line("team delete");
//
//        em.flush();
//        em.clear();
//        line("found member에서 Team delete");


        // when
        /**
         * NotFound가 어노테이션이 명시되어있으면 Eager로딩도 수행하며, Member는 존재하고 Team은 존재하지 않았을 경우, `그냥 Member도 null` 하다고 판단해버린다.
         * 다만, FK만 존재하지 않는 경우를 DB에 환경조성 해 두면, EntityNotFound Exception이 발생하고, 이는 Lazy 로딩도 정상적으로 수행된다.
         */
        Member_NotFound foundMember2 = null;
        try{
//            foundMember2 = em.createQuery("SELECT m FROM Member_NotFound m left outer join fetch m.team WHERE m.id = :id",Member_NotFound.class).setParameter("id",1L).getSingleResult();
            // 이건 또 null 그냥 EXCEPTION 모드일때도 설정 걍 해주네
            foundMember2 = em.find(Member_NotFound.class,1L);

        }catch (Exception e){
            e.printStackTrace();
        }


//        Team_NotFound team = foundMember2.getTeam();
//        line("get team");
//
//        // then
//        Assertions.assertThat(team).isNull();
    }

//    @Test
//    @DisplayName("Not Found Trigger when calling Many Side from One Side") // 이것도 안나는데?
//    void test2(){
//        // given
//        Team_NotFound team1 = new Team_NotFound(1L, "team1");
//        em.persist(team1);
//        em.flush();
//        em.clear();
//
//        // when
//        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
//        // then
//        List<Member_NotFound> members = foundTeam.getMembers();
//        for (Member_NotFound member : members) {
//            member.getName();
//        }
//
//        Assertions.assertThat(members).isEmpty();
//
//    }

    @Test
    @DisplayName("Not Found 없이 살아보기 with List")
    void test3(){

        //given

        // scenario
        /**
         * memberA, memberB는 Team1에 소속
         * memberC는 Team2에 소속
         *
         * Team1을 삭제하고
         *
         * 모든 member를 조회하면, memberA와 memberB의 경우 team을 referencing 할 때, EntityNotFoundException이 발생한다.
         */
        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
        Member_NotFound memberB = new Member_NotFound(2L, "memberB");
        Member_NotFound memberC = new Member_NotFound(3L, "memberC");
        Team_NotFound team1 = new Team_NotFound(1L,"team1");
        Team_NotFound team2 = new Team_NotFound(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.flush();
        em.clear();


        // team1 삭제
        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();


        // when
        List<Member_NotFound> memberList = em.createQuery("SELECT m FROM Member_NotFound as m", Member_NotFound.class).getResultList();

        for (Member_NotFound eachMember : memberList) {
            System.out.println(eachMember.getName()); // 여기까진 LazyLoading 안됌.
            try{
                System.out.println(eachMember.getTeam().getName()); // 여기서 발생하므로 try catch를 걸어서
            }catch (EntityNotFoundException exception){
                eachMember.setTeamNull();
            }
        }
        System.out.println(memberList.get(2).getTeam().getName());

        System.out.println("여기서 디버그 모드로 entity에 null 잘 세팅되었는지 확인");
    }

    @Test
    @DisplayName("Not Found 없이 살아보기 with List2")
    void test4(){

        //given

        // scenario
        /**
         * memberA, memberB는 Team1에 소속
         * memberC는 Team2에 소속
         *
         * Team1을 삭제하고
         *
         * 모든 member를 조회하면, memberA와 memberB의 경우 team을 referencing 할 때, EntityNotFoundException이 발생한다.
         */
        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
        Member_NotFound memberB = new Member_NotFound(2L, "memberB");
        Member_NotFound memberC = new Member_NotFound(3L, "memberC");
        Team_NotFound team1 = new Team_NotFound(1L,"team1");
        Team_NotFound team2 = new Team_NotFound(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.flush();
        em.clear();


        // team1 삭제
        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();


        // when
        List<Member_NotFound> memberList = em.createQuery("SELECT m FROM Member_NotFound m join fetch m.team ", Member_NotFound.class).getResultList();

        // 애초에 하나만 가져오네 이러면 @NotFound 처럼.
        for (Member_NotFound eachMember : memberList) {
            System.out.println(eachMember.getName()); // 여기까진 LazyLoading 안됌.
            System.out.println(eachMember.getTeam().getName()); // 여기서 발생하므로 try catch를 걸어서
        }

        System.out.println("여기서 디버그 모드로 entity에 null 잘 세팅되었는지 확인");
    }
}
