package org.example.customDomain.nativeQueryCaches;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;
import java.util.List;

public class NativeQueryCachesTest extends BaseCondition {

    @BeforeEach
    void scenario(){

        /**
         * TeamA(1) - MemberA(1)랑 MemberB(2)
         */

        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);
        em.persist(team);

        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team);
        em.persist(member);

        Member member2 = new Member();
        member2.setUsername("memberB");
        member2.setId(2L);
        member2.setTeam(team);
        em.persist(member2);

        em.flush();
        em.clear();

        line("after scenario");
    }

    // Member가 갖는 Team의 관계가 LAZY 해야함
    @Test
    @DisplayName("네이티브 쿼리도 하이버네이트 1차 캐싱의 도움을 받는가?")
    // 1. 가져온 객체도 하이버네이트 관리하에 있음 (내부 필드 프록싱 되어있음, get으로 fetching 가능)
    // 2. 이미 하이버네이트 1차 캐시에 데이터가 존재해도 네이티브 쿼리는 fire 되는가? -> 그렇다. JPQL도 마찬가지로 그 전에 context를 flush하기때문에.
    void caching(){

        String sql = "SELECT * FROM MEMBER M WHERE M.ID = :ID";

        Query nativeQuery = em.createNativeQuery(sql, Member.class);
        nativeQuery.setParameter("ID", 1L);

        //List<Member> resultList = nativeQuery.getResultList();

        Member member = (Member)nativeQuery.getSingleResult();
        line("after fetch with native");

        // 여기 통과하면 일단 네이티브쿼리로 들고 온 객체도 하이버네이트에서 관리함을 알 수 있음. getTeam이 프록싱이 되어있음
        Team team = member.getTeam();
        System.out.println(team.getName());

        line("after fetch TeamA");

        // teamA를 프록시통해서 들고왔으니, 네이티브 쿼리가 하이버네이트1차 캐싱의 도움을 받는지 확인
        String sql2 = "SELECT * FROM TEAM T WHERE T.ID = :ID";

        Query nativeQuery1 = em.createNativeQuery(sql2, Team.class);
        nativeQuery1.setParameter("ID",1L);

        line("before fetching Team via nativeQuery");
        // 쿼리나감, 아맞다 JPQL 직전에 flush!, 근데 clear는 안함, 그럼에도 불구하고 쿼리는 나감
        Team foundByNativeTeam = (Team) nativeQuery1.getSingleResult();
        line("after fetching Team via nativeQuery");
    }

    @Test
    @DisplayName("네이티브 쿼리가 가져온 엔티티는 하이버네이트 1차캐시에 있는가?")
    void caching2(){
        String sql2 = "SELECT * FROM TEAM T WHERE T.ID = :ID";

        Query nativeQuery1 = em.createNativeQuery(sql2, Team.class);
        nativeQuery1.setParameter("ID",1L);

        Team foundByNativeTeam = (Team) nativeQuery1.getSingleResult();

        // 그리고 여기서 Team이 initialize되었으니 네이티브 쿼리가 가져온 엔티티가 캐싱되었으면 getTeam할때 추가쿼리가 발생하지 않을 것이다.-> 발생안하네
        // 즉, 네이티브 쿼리가 가져온 엔티티는 하이버네이트 1차캐시에 있음.
        Member member = em.find(Member.class, 1L);
        Team team = member.getTeam();
        System.out.println(team.getName());
    }

}
