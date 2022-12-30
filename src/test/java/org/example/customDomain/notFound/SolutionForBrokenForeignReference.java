package org.example.customDomain.notFound;

import org.example.BaseCondition;
import org.example.customDomain.notFound.noForeignKey.Member_noFk;
import org.example.customDomain.notFound.noForeignKey.Member_noFk_DTO;
import org.example.customDomain.notFound.noForeignKey.Team_noFk;
import org.example.customDomain.notFound.noForeignKey.Team_noFk_DTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionForBrokenForeignReference extends BaseCondition {

    /**
     * https://thorben-janssen.com/hibernates-notfound/ 의 often better `alternative`에 따르면
     *
     * The enforced eager fetching of Hibernate’s @NotFound mapping can cause performance problems.
     * Even though the implementation might be more complex,
     * it’s often better `not to annotate your association with @NotFound` and `handle the broken foreign key reference in your business code.`
     *
     * @NotFound 어노테이션을 사용하지 않고
     * broken foreign key reference(외래키 제약 조건의 위반)을 business code로 풀어라
     *
     */
    @Test
    @DisplayName("외래키 제약 조건의 위반을 business code로 풀기 - problem")
    void problem(){ // Problem이니 Team2 LAZY SELECT 할 때, EntityNotFoundException 발생한다.
        List<Member_noFk> memberList = em.createQuery("SELECT m FROM Member_noFk m", Member_noFk.class).getResultList();

        for(var eachMember : memberList){
            System.out.println("=================================================");
            System.out.println("ID = " + eachMember.getId());
            System.out.println("TeamName = " + eachMember.getTeam().getName());
            System.out.println("=================================================");
        }
    }

    /**
     * DTO를 사용해서 수동으로 fetch join 해보자.
     */
    @Test
    @DisplayName("외래키 제약 조건의 위반을 business code로 풀기 - solve")
    // 하고자 하는 일, 모든 Member를 가져오되, team을 fetch join 하고 싶고,
    // teamId가 broken foreign reference를 가졌다면, null을 대입.(business code)
    void solve(){ // Problem이니 Team2 LAZY SELECT 할 때, EntityNotFoundException 발생한다.

        String query = "SELECT new org.example.customDomain.notFound.noForeignKey.Member_noFk_DTO(m.id, m.name, m.team.id)" +
                "FROM Member_noFk m";

        List<Member_noFk_DTO> memberDtoList = em.createQuery(query, Member_noFk_DTO.class).getResultList();

        // TeamId로 조회한 Team이 broken fk reference라서 없음. 캐시, Team_noFK_DTO는 nullable
        // 캐시를 사용하지 않고 Hibernate 1차 캐시를 사용할 수 있지만, broken reference가 존재하는 Member마다 전부 lazy 쿼리가 나간다. -> solve2에서 확인(확인해보니 null 추가 쿼리 발생 안함)
        Map<Long, Team_noFk_DTO> teamByTeamId = new HashMap<>();
        teamByTeamId.put(null,null); // teamId가 null이면 그 refer된 team도 당연히 null이다.


        String secondQuery = "SELECT new org.example.customDomain.notFound.noForeignKey.Team_noFk_DTO(t.id, t.name) "+
                "FROM Team_noFk t "+
                "WHERE t.id = :id ";

        for(var eachMember : memberDtoList){

            Long teamId = eachMember.obtainTeamId();

            //갖고 있으면
            if(teamByTeamId.containsKey(teamId)){
                eachMember.setTeam(teamByTeamId.get(teamId));
                continue;
            }

            TypedQuery<Team_noFk_DTO> secondTypedQuery = em.createQuery(secondQuery, Team_noFk_DTO.class);
            secondTypedQuery.setParameter("id",teamId);

            Team_noFk_DTO teamDto;
            try{
                teamDto = secondTypedQuery.getSingleResult();
            }catch (NoResultException noResultException){
                teamDto = null;
            }

            eachMember.setTeam(teamDto);

            // 그리고 Map에 put
            teamByTeamId.put(teamId,teamDto);
        }

        System.out.println("hey check memberDtoList");
    }

    /**
     * <<<<<<<<<<<<<<<<<<<<현재로써 가장 그나마 나은 방안>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */
    // Member -> Team reference가 LAZY
    /**
     * EntityNotFoundException을 발생시키면, 그 exception이 HibernateProxy 내에 남아있으므로, NotFound(ignore)처럼
     * null한지를 몰라서 추가 쿼리를 발생시키는 일은 없다. 신기하다.
     *
     * NotFoundTest의 `notFound_ignore_and_lazy_and_many`와 달리
     * notFound인지 여부를 proxy 내에 기억할 수 있다.
     *
     * 그렇지만, 여전히 Member의 갯수 N만큼 반복해야 한다는 사실은 변하지 않는다.
     * 이를 Grouping등으로 I/O를 더 줄일 수 있는 방법은 있지만, 여기까지만...
     */
    @Test
    @DisplayName("외래키 제약 조건의 위반을 business code로 풀기 - solve2 (엔티티 기준)")
    void solve2(){

        String query = "SELECT m " +
                "FROM Member_noFk m ";

        List<Member_noFk> memberList = em.createQuery(query, Member_noFk.class).getResultList(); // proxying되어있는 상태.

        for(var eachMember : memberList){

            String memberName = eachMember.getName();
            System.out.println("memberName = " + memberName);

            // 이미 FK가 null한 경우
            if(eachMember.getTeam() == null){
                continue;
            }
//            Long teamId = eachMember.getTeam().getId(); // proxy에서 Id 가져오는건 Proxy해제가 아니니까 괜찮겠지? (Y/N) --- Y
            try{
                System.out.println(eachMember.getTeam().getName()); // load
            }catch (EntityNotFoundException e){
                eachMember.setTeam(null);
            }
        }

        System.out.println("hey check memberDtoList");
    }

    @BeforeEach
    void scenario(){
        //given

        /**
         * memberA, memberB는 Team1에 소속
         * memberC, memberD는 Team2에 소속
         *
         * Team1을 삭제
         */
        Member_noFk memberA = new Member_noFk(1L, "memberA");
        Member_noFk memberB = new Member_noFk(2L, "memberB");
        Member_noFk memberC = new Member_noFk(3L, "memberC");
        Member_noFk memberD = new Member_noFk(4L, "memberD");
        Member_noFk memberE = new Member_noFk(5L,"memberE");
        Team_noFk team1 = new Team_noFk(1L,"team1");
        Team_noFk team2 = new Team_noFk(2L,"team2");
        memberA.setTeam(team1);
        memberB.setTeam(team1);
        memberC.setTeam(team2);
        memberD.setTeam(team2);
        // memberE.setTeam(team2); // memberE의 team은 null
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
        em.flush();
        em.clear();


        // team2 삭제
        Team_noFk foundTeam = em.find(Team_noFk.class, 2L);
        em.remove(foundTeam);
        em.flush();
        em.clear();

        line("after scenario");
    }
}
