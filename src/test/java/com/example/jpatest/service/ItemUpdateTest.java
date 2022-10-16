package com.example.jpatest.service;

import com.example.jpatest.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager entityManager;

    @Test
    public void updateTest() {
        Book book = entityManager.find(Book.class, 1L);

        // tx
        book.setAuthor("sffds");
        // 변경감지 == dirty checking
        // tx commit

    }
}
