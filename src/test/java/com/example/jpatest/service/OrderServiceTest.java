package com.example.jpatest.service;

import com.example.jpatest.domain.Address;
import com.example.jpatest.domain.Member;
import com.example.jpatest.domain.Order;
import com.example.jpatest.domain.OrderStatus;
import com.example.jpatest.domain.item.Book;
import com.example.jpatest.exception.NotEnoughStockException;
import com.example.jpatest.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() {


        Member member = getMember();

        Book book = getBook("JPA 신", 10000, 10);


        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);


        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(1, getOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    public void 재고수량초과() throws Exception {
        Member member = getMember();
        Book book = getBook("JPA 신", 10000, 10);
        int orderCount = 11;
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    public void 주문취소() throws Exception {
        Member member = getMember();
        Book book = getBook("JPA 신", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, book.getStockQuantity());

    }

    private Book getBook(String name, int price, int quantity) {
        Book book = Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(quantity)
                .build();
        entityManager.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        entityManager.persist(member);
        return member;
    }


}