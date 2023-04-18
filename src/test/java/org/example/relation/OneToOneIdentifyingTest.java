package org.example.relation;


import org.example.BaseCondition;
import org.example.relation.onetoone.Corp;
import org.example.relation.onetoone.CorpDetail;
import org.example.relation.onetoone.Locker_Identifying;
import org.example.relation.onetoone.Member_Identifying;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OneToOneIdentifyingTest extends BaseCondition {

    //@BeforeEach
    //void scenario(){
    //    /**
    //     * CORP와 CORP_DETAIL은 1:1 관계이며 가상 식별관계이다.(가상-실제로 DDL Constraint가 존재하진 않음)
    //     */
    //
    //    //corp1은 detail을 갖고있고
    //    Corp corp1 = new Corp();
    //    corp1.setCorpId(1L);
    //    corp1.setName("corp1");
    //
    //    //2는 없는 상황
    //    Corp corp2 = new Corp();
    //    corp2.setCorpId(2L);
    //    corp2.setName("corp2");
    //
    //    CorpDetail corpDetail1 = new CorpDetail();
    //    corpDetail1.setCorpId(1L);
    //    corpDetail1.setDetailName("corp1-Detail");
    //
    //    em.persist(corp1);
    //    em.persist(corp2);
    //    em.persist(corpDetail1);
    //
    //    em.flush();
    //    em.clear();
    //
    //    line("after scenario");
    //}
    //
    //@Test
    //@DisplayName("기본 테스트 -> LAZY 씹힘")
    ///**
    // * OneToOne의 부테이블이 주테이블을 query할때, 왜 LAZY가 불가한가?
    // * 그 이유는 프록싱이 불가하기 때문이다. 프록싱을 this=null 할 수 없기 때문에.
    // *
    // * 마찬가지로, 식별관계의 주테이블이 부테이블을 query할때, 그 부테이블에
    // * 데이터가 있을지없을지는 명확치 않기 때문에 이런 관계에서 마찬가지로 강제 EAGER가
    // * fire 된다.
    // *
    // * 그렇다면 방법은 있을 지 없을 지 불명확한 관계에서 프록싱을 해두고
    // * EntityNotFoundException을 발생시키는 방법은 없을까?
    // *
    // */
    //void test(){
    //    Corp corp = em.find(Corp.class, 2L);
    //
    //    line("before touch detail");
    //    String detailName = corpDetail.getDetailName();
    //    System.out.println(detailName);
    //    line("after touch detail");
    //}
    //
    ////@Test
    ////@DisplayName("기본 테스트 -> LAZY 씹힘")
    ////void test2(){
    ////    List<Corp> corps = em.createQuery("SELECT c FROM Corp c",Corp.class).getResultList();
    ////
    ////    line("before touch detail");
    ////    for (Corp corp : corps) {
    ////        CorpDetail corpDetail = corp.getCorpDetail();
    ////        String detailName = corpDetail.getDetailName();
    ////        System.out.println(detailName);
    ////    }
    ////    line("after touch detail");
    ////}

    @BeforeEach
    void scenario(){
        Member_Identifying member1 = new Member_Identifying();
        member1.setId(1L);
        member1.setName("member1");

        em.persist(member1);

        Locker_Identifying locker1 = new Locker_Identifying();
        locker1.setId(101L);
        locker1.setName("locker1");

        locker1.setMember(member1);

        em.persist(locker1);

        em.flush();
        em.clear();

        Member_Identifying member_identifying = em.find(Member_Identifying.class, 1L);
        em.remove(member_identifying);
        em.flush();
        em.clear();

        line("after scenario");
    }

    @Test
    void test10(){
        Locker_Identifying foundLocker = em.find(Locker_Identifying.class, 101L);

        line("---");
        Member_Identifying member = foundLocker.getMember();
        String name = member.getName();

        System.out.println("watch out");
    }

    @Test
    void test11(){
        Member_Identifying foundMember = em.find(Member_Identifying.class, 1L);

        Locker_Identifying locker = foundMember.getLocker();
        String name = locker.getName();


        System.out.println("watch out2");
    }
}
