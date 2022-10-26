package com.example.jpatest;

import com.example.jpatest.domain.*;
import com.example.jpatest.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager entityManager;

        public void dbInit1() {
            Member member = createMember("userA", "test", "dfgdfsg", "sdfsdaf");
            entityManager.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000);
            entityManager.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "sdf", "dfgdfsg", "sdfsdaf");
            entityManager.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 10000);
            entityManager.persist(book1);
            Book book2 = createBook("SPRING2 BOOK", 20000);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String JPA1_BOOK, int price) {
            Book book1 = Book.builder()
                    .name(JPA1_BOOK)
                    .price(price)
                    .stockQuantity(100)
                    .build();
            return book1;
        }

        private static Member createMember(String name, String city, String street, String zipCode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipCode));
            return member;
        }
    }
}


