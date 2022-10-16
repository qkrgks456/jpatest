package com.example.jpatest.repository;

import com.example.jpatest.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager entityManager;

    public void save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            entityManager.merge(item); // 우선은 update 갬성이라고 이해 추후에 보자
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager
                .createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
