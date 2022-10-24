package com.example.jpatest.service;

import com.example.jpatest.domain.Delivery;
import com.example.jpatest.domain.Member;
import com.example.jpatest.domain.Order;
import com.example.jpatest.domain.OrderItem;
import com.example.jpatest.domain.item.Item;
import com.example.jpatest.repository.ItemRepository;
import com.example.jpatest.repository.MemberRepository;
import com.example.jpatest.repository.OrderRepository;
import com.example.jpatest.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orederItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orederItem);

        // 원래 delivery도 save 하고 orderItem도 save 해야하는데 아래만 한줄이냐면
        // cascade = CascadeType.ALL 이거때문에 한방에 해줌 order에서만 사용되기 때문에 사용한거 그렇지않으면 사용하지말자
        orderRepository.save(order);

        return order.getId();
    }

    // 취소

    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        // 객체내의 값이 변경될껀데 근데 마이바티스등 이용하면 값을 또 다 꺼내서 쿼리를 실행 시켜야한다
        // -> 하지만! jpa는 엔티티의 값만 바꿔주면 변경감지해서 데이터베이스 업데이트 쿼리를 찹찹 날린다 지리네? (dirty checking 또는 변경감지)
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
