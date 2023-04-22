//package org.example.multiple_bag_fetch_exception;
//
//import org.example.BaseCondition;
//import org.hibernate.jpa.QueryHints;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//public class OtherTest extends BaseCondition {
//
//    @BeforeEach
//    void scenario() {
//
//        Vehicle avante = Vehicle.builder()
//                .id(1L)
//                .name("아반떼")
//                .type("CAR")
//                .build();
//
//        em.persist(avante);
//
//        Vehicle bongo = Vehicle.builder()
//                .id(2L)
//                .name("봉고")
//                .type("TRUCK")
//                .build();
//
//        em.persist(bongo);
//
//        Orders avanteOrder1 = Orders.builder()
//                .id(1L)
//                .name("아반떼 주문1")
//                .vehicle(avante)
//                .build();
//
//        em.persist(avanteOrder1);
//
//        Orders avanteOrder2 = Orders.builder()
//                .id(2L)
//                .name("아반떼 주문2")
//                .vehicle(avante)
//                .build();
//
//        em.persist(avanteOrder2);
//
//        Orders bongoOrder1 = Orders.builder()
//                .id(3L)
//                .name("봉고 주문1")
//                .vehicle(bongo)
//                .build();
//
//        em.persist(bongoOrder1);
//
//        OrderDetail avanteOrder1OrderDetail1 = OrderDetail.builder()
//                .id(1L)
//                .name("아반떼 상품A")
//                .price(1000L)
//                .order(avanteOrder1)
//                .build();
//
//        em.persist(avanteOrder1OrderDetail1);
//
//        OrderDetail avanteOrder1OrderDetail2 = OrderDetail.builder()
//                .id(1L)
//                .name("아반떼 상품B")
//                .price(1200L)
//                .order(avanteOrder1)
//                .build();
//    }
//
//    @Test
//    void doc() {
//        String jpql = "SELECT vehicle FROM Vehicle vehicle " +
//                "JOIN vehicle.ordersList order " +
//                "JOIN order.orderDetailList orderDetail ";
//
//        List<Vehicle> resultList = em.createQuery(jpql, Vehicle.class).getResultList();
//    }
//
//    @Test
//    void doc2() {
//        String jpql = "SELECT vehicle FROM Vehicle vehicle " +
//                "JOIN vehicle.ordersList order " +
//                "WHERE vehicle.id = 1 " +
//                "ORDER BY order.id ASC ";
//
//        List<Vehicle> resultList = em.createQuery(jpql, Vehicle.class).getResultList();
//    }
//
//    @Test
//    void doc3(){
//        Vehicle vehicle = em.find(Vehicle.class, 1L);
//        List<Order> ordersList = vehicle.getOrdersList();
//
//        String jpql = "SELECT order FROM Vehicle vehicle " +
//                "JOIN vehicle.ordersList order " +
//                "WHERE vehicle.id = 1";
//
//        List<Vehicle> resultList = em.createQuery(jpql, Vehicle.class).getResultList();
//    }
//
//    @Test
//    void doc4(){
//        String jpql1 = "SELECT DISTINCT vehicle FROM Vehicle vehicle "
//                        + "JOIN FETCH vehicle.ordersList "
//                        + "WHERE vehicle.type = 'CAR' ";
//
//        List<Vehicle> vehicles = em.createQuery(jpql1, Vehicle.class)
//                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
//                .getResultList();
//
//        String jpql2 = "SELECT order FROM Order order "
//                        + "JOIN FETCH order.orderDetailList orderDetail "
//                        + "WHERE order.vehicle IN :vehicles AND "
//                        + "orderDetail.price >= 100 ";
//
//        //caching only
//        em.createQuery(jpql2 , Order.class).getResultList();
//    }
//}
