package com.example.jpatest.repository.query;

import com.example.jpatest.dto.OrderSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager entityManager;

    public List<OrderSimpleDto> findOrderDtos() {
        return entityManager.createQuery("select new com.example.jpatest.dto.OrderSimpleDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleDto.class)
                .getResultList();
    }
}
