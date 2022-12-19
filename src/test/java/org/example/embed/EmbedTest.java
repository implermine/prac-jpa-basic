package org.example.embed;


import org.example.BaseCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class EmbedTest extends BaseCondition {

    @Test
    @DisplayName("embed test")
    void test(){
        MemberWithEmbedded member = new MemberWithEmbedded();
        member.setId(1L);
        member.setName("tae gang");
        member.setWorkPeriod(new Period(LocalDateTime.MIN,LocalDateTime.MAX));
        member.setHomeAddress(new Address("city","street","zipCode"));

        em.persist(member);

        em.flush();
        em.clear();

        MemberWithEmbedded foundMember = em.find(MemberWithEmbedded.class, 1L);
        System.out.println(foundMember.getWorkPeriod().toString());
        System.out.println(foundMember.getHomeAddress().toString());
    }

    @Test
    @DisplayName("Embeddable 객체의 Setter없이 그보다 밖의 메서드(Setter)를 이용해서 mutate 해보기")
    void test2(){
        // given
        MemberWithEmbedded member = new MemberWithEmbedded();
        member.setId(1L);
        member.setName("tae gang");
        member.setWorkPeriod(new Period(LocalDateTime.of(2022,3,1,0,0),LocalDateTime.of(2022,5,1,0,0)));
        member.setVacationPeriod(new Period(LocalDateTime.of(2022,6,1,0,0),LocalDateTime.of(2022,7,1,0,0)));
        member.setHomeAddress(new Address("city","street","zipCode"));
        em.persist(member);
        em.flush();
        em.clear();

        // when
        MemberWithEmbedded foundMember = em.find(MemberWithEmbedded.class, 1L);
        foundMember.setVacationPeriod(new Period(LocalDateTime.of(2022,8,1,0,0),LocalDateTime.of(2022,9,1,0,0)));
    }

}
