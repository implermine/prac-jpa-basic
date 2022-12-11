package org.example.embed;


import org.assertj.core.api.Assertions;
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
        member.setAddress(new Address("city","street","zipCode"));

        em.persist(member);

        em.flush();
        em.clear();

        MemberWithEmbedded foundMember = em.find(MemberWithEmbedded.class, 1L);
        System.out.println(foundMember.getWorkPeriod().toString());
        System.out.println(foundMember.getAddress().toString());
    }

}
