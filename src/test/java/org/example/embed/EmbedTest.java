package org.example.embed;


import org.example.BaseCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

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

    /**
     * 값 타입 Collection의 orphanRemoval behavior
     *
     * 너무 많은 삭제
     *
     * 값 타입은 엔티티와 다르게 식별자 개념이 없기 때문에 값을 변경하면 추적이 어렵다.
     *
     * https://developer-hm.tistory.com/48
     */
    @Test
    @DisplayName("값 타입 Collection은 CASCADE.ALL + orphanRemoval= true 이다.")
    void test3(){
        MemberWithEmbedded member = new MemberWithEmbedded();
        member.setId(1L);
        member.setName("member1");
        member.setHomeAddress(new Address("city","street","zipCode"));

        member.getFavoriteFoods().add("치킨");
        member.getFavoriteFoods().add("족발");
        member.getFavoriteFoods().add("피자");

        member.getAddressHistories().add(new Address("oldCity1", "oldStreet1", "oldZipCode1"));
        member.getAddressHistories().add(new Address("oldCity2", "oldStreet2", "oldZipCode2"));

        em.persist(member);

        line("1 - flush 전");
        em.flush();
        // 여기에 INSERT 쿼리 발생하면, CASCADE.PERSIST가 맞다. -> 맞음
        line("1 - flush 후");
        em.clear();

        //========================================================================================================

        MemberWithEmbedded foundMember = em.find(MemberWithEmbedded.class, 1L);
        List<Address> addressHistories = foundMember.getAddressHistories();

        // 이렇게 하지 못하는 이유는 removeFirstIf라서이다.
        // addressHistories.removeIf(next -> next.equals((new Address("oldCity1", "oldStreet1", "oldZipCode1"))));

        Iterator<Address> iterator = addressHistories.iterator();
        while(iterator.hasNext()){
            Address next = iterator.next();
            if(next.equals((new Address("oldCity1", "oldStreet1", "oldZipCode1")))){
                iterator.remove();
                break;
            }
        }


        line("2 - flush 전");
        em.flush();
        // 여기에 REMOVE 쿼리 발생하면, orphanRemoval=true가 맞다.

        // 아니 뭔데 다 날리고 2만 다시 집어넣는 미친 로직이냐ㅋㅋㅋㅋ
        line("2 - flush 후");

        tx.commit();

    }

}
