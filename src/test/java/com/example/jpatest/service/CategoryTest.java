package com.example.jpatest.service;


import com.example.jpatest.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class CategoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    public void test() {
        /*Category child1 = new Category();
        child1.setName("자식1");
        Category child2 = new Category();
        child2.setName("자식2");
        Category category = new Category();
        category.setName("부모1");
        category.setChildCategory(child1);
        category.setChildCategory(child2);
        entityManager.persist(category);*/

        Category category = entityManager.find(Category.class, 6L);
        System.out.println("=================== 여기 다음 ? select ");
        System.out.println(category.getParent());
    }

}
