package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class BaseCondition {
    protected final EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction tx;

    protected String divider = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";

    public BaseCondition() {
        emf = Persistence.createEntityManagerFactory("hello");
    }

    @BeforeEach
    public void setUp(){
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    public void tearDown(){
//        tx.rollback();
        em.close();
    }

    protected void line(int val){

        String format = " [ %s ] " + divider;

        System.out.println(String.format(format,val));
    }

    protected void line(String str){
        String format = " [ %s ] ";
        String madeFormat = String.format(format,str);

        //make divider shorter to fit width with above and below lineDivider
        int endIndexOfDividerToCut =  (divider.length() - 1) - madeFormat.length() + 1; // exclusive index
        String truncatedDivider = divider.substring(0,endIndexOfDividerToCut);

        System.out.println(divider);
        System.out.println(divider);
        System.out.println(String.format(format,str) + truncatedDivider);
        System.out.println(divider);
        System.out.println(divider);
    }
}
