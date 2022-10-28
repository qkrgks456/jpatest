package com.example.jpatest.repository.query;

import com.example.jpatest.domain.Order;
import com.example.jpatest.domain.OrderStatus;
import com.example.jpatest.repository.OrderSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.jpatest.domain.QMember.member;
import static com.example.jpatest.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Order> findAllBySearch(OrderSearch orderSearch) {
        return queryFactory.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        return statusCond == null ? null : order.status.eq(statusCond);
    }


    private BooleanExpression nameLike(String nameCond) {
        return StringUtils.hasText(nameCond) ? member.name.like(nameCond) : null;
    }
}
