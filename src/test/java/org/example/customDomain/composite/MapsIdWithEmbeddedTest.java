package org.example.customDomain.composite;

import org.example.BaseCondition;
import org.example.customDomain.composite.model.Person;
import org.example.customDomain.composite.model.PersonDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MapsId는
 * MapsId로 지정된 필드 (보통 부모를 가르킴)의 내용을
 * this 엔티티의 Id에 투영하겠다는 의미이다.
 *
 * 다시말해, Map (this field's Id to this entity) Id 이다. -> 반대아님? MapsId로 표현된 각 필드와 객체중에 객체의 Id를 필드에 투영이 맞지않나
 *
 * 이 MapsId는 SAVE할때만 의미가 있다.
 * https://fullstackdeveloper.guru/2021/08/24/what-is-mapsid-used-for-in-jpa-hibernate-part-1/
 *
 * insertable=false, updatable=false와는 반대이다.
 *
 * MapsId를 쓰면, PK,FK인 personId가 readOnly이며
 *
 */
public class MapsIdWithEmbeddedTest extends BaseCondition {

    /**
     * HOW TO SAVE 결론
     *
     * CompositeId에 있는 PersonId, 즉 식별키 (PK,FK)는 SAVE 상황에서 아무 의미가 없다.
     * 그냥 RELATION 에 값 채워서 PERSIST 하면 된다.
     */


    @Test
    @DisplayName("HOW TO SAVE - CHOOSE 1")
    void how_to_save_Choose_1(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setName("이태강");
        person.setHeight(180);

        em.persist(person);

        PersonDetail personDetail = new PersonDetail();
        personDetail.setWeight(30);
        //personDetail.setPerson(); // ?
        //personDetail.setPersonDetailId() // ? what should we choose?

        //personDetail.setPerson(person);
        personDetail.setPersonDetailId(PersonDetail.CompositeId.of( 1L , "왼팔" )); //<-- 이것도 웃기네 personId는 null로 채워두는게
        //안웃기네 이게 왜 되냐
        em.persist(personDetail);

        //personDetail.setPersonDetailId(PersonDetail.CompositeId.of(person.getPersonId(),"왼팔"));
        //em.persist(personDetail);

        tx.commit();
    }

    @Test
    @DisplayName("HOW TO SAVE - CHOOSE 2")
    void how_to_save_Choose_2(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setName("이태강");
        person.setHeight(180);

        em.persist(person);

        Person person2 = new Person();
        person2.setPersonId(2L);
        person2.setName("김태강");
        person2.setHeight(200);

        em.persist(person2);

        PersonDetail personDetail = new PersonDetail();
        personDetail.setWeight(30);
        //personDetail.setPerson(); // ?
        //personDetail.setPersonDetailId() // ? what should we choose?


        personDetail.setPersonDetailId(PersonDetail.CompositeId.of(person2.getPersonId(),"왼팔")); // <- 의미가 없네
        personDetail.setPerson(person);
        em.persist(personDetail);

        tx.commit();
    }

    @Test
    @DisplayName("HOW TO FETCH")
    void how_to_fetch(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setName("이태강");
        person.setHeight(180);


        em.persist(person);

        PersonDetail personDetail = new PersonDetail();
        personDetail.setWeight(30);
        //personDetail.setPerson(); // ?
        //personDetail.setPersonDetailId() // ? what should we choose?


        personDetail.setPersonDetailId(PersonDetail.CompositeId.of(null,"왼팔")); // <- 의미가 없네
        personDetail.setPerson(person);
        em.persist(personDetail);

        em.flush();
        em.clear();

        line("AFTER FLUSH");

        PersonDetail foundPersonDetail = em.find(PersonDetail.class, PersonDetail.CompositeId.of(1L,"왼팔"));

        System.out.println(foundPersonDetail.getPerson());
        System.out.println("hey");
    }

    /**
     * JPA가 PK 변경을 지원하지 않나봄
     */
    @Test
    @DisplayName("mapsId를 쓸 때, 원래 PK는 readOnly인가")
    void test(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setName("이태강");
        person.setHeight(180);


        em.persist(person);

        PersonDetail personDetail = new PersonDetail();
        personDetail.setWeight(30);
        //personDetail.setPerson(); // ?
        //personDetail.setPersonDetailId() // ? what should we choose?


        personDetail.setPersonDetailId(PersonDetail.CompositeId.of(580L,"왼팔")); // <- 의미가 없네
        personDetail.setPerson(person);
        em.persist(personDetail);

        em.flush();
        em.clear();

        line("AFTER FLUSH");

        PersonDetail foundPersonDetail = em.find(PersonDetail.class, PersonDetail.CompositeId.of(1L,"왼팔"));

        Person person2 = new Person();
        person2.setPersonId(2L);
        person2.setName("김태강");
        person2.setHeight(200);
        em.persist(person2);

        //foundPersonDetail.setPerson(person2); // ID가 바로 변경되는가? -> 되겠냐고
        foundPersonDetail.getPersonDetailId().setPersonId(2L);
        foundPersonDetail.setPerson(person2);
        System.out.println("hey1");

        em.persist(foundPersonDetail);

        line("before flush");
        em.flush();
        em.clear();
        line("after flush");


        PersonDetail foundPersonDetail2 = em.find(PersonDetail.class, PersonDetail.CompositeId.of(2L,"왼팔"));
        System.out.println(foundPersonDetail2.getPerson());
        System.out.println("hey2");
    }
}
