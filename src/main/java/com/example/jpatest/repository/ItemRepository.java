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
        entityManager.persist(item);
        /*if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            entityManager.merge(item); // 이거 쓰지 마세요 수정할때 set 안하면 null로 그냥 업데이트 됩니다 모든 컬럼을 업데이트하기 때문에 위험
        }*/
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
