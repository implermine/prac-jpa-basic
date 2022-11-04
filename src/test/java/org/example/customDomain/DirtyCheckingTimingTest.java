package org.example.customDomain;

import org.example.BaseCondition;
import org.example.relation.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Dirty-Checking은 flush 타이밍에 일어나는가? 혹은 tx.commit 타이밍에 일어나는가?
 */
public class DirtyCheckingTimingTest extends BaseCondition {

    @Test
    @DisplayName("Dirty-Checking은 flush 타이밍에 일어나는가? 혹은 tx.commit 타이밍에 일어나는가?") // -> flush
    void test(){
        Team team1 = new Team();
        team1.setId(1L);
        team1.setName("team1");

        em.persist(team1);
        em.flush();
        em.clear();

        Team team = em.find(Team.class, 1L);
        team.setName("Modified team1");
        System.out.println("Before Flush");
        em.flush();
        System.out.println("After Flush");
        System.out.println("Before Commit");
        tx.commit();
        System.out.println("After Commit");
    }
}
