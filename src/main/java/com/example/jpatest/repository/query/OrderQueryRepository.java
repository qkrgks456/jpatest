package com.example.jpatest.repository.query;

import com.example.jpatest.dto.OrderFlatDto;
import com.example.jpatest.dto.OrderItemQueryDto;
import com.example.jpatest.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager entityManager;


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(orderQueryDto -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId()); // 쿼리 N번
            orderQueryDto.setOrderItems(orderItems);
        });
        return orders;
    }


    public List<OrderQueryDto> findAllByDtos_optimization() {
        List<OrderQueryDto> orders = findOrders();

        List<Long> orderIds = orders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = entityManager.createQuery("select new com.example.jpatest.dto.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                        "from OrderItem oi join oi.item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery("select new com.example.jpatest.dto.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                        "from OrderItem oi join oi.item i " +
                        "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery("select new com.example.jpatest.dto.OrderQueryDto(o.id,m.name,o.orderDate,o.status,m.address) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderQueryDto.class).getResultList();
    }

    public List<OrderFlatDto> findAllByDtos_flat() {
        return entityManager.createQuery("select new com.example.jpatest.dto.OrderFlatDto(o.id,m.name,o.orderDate,o.status,m.address,i.name,i.price,oi.count) from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
