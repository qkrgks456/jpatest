package com.example.jpatest.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Setter // 엔티티에는 @Setter 쓰지말자 안 쓰고도 어떻게든 방법이 있다
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // cascade 하면 한방에 해줌
    // persist(orderItemA);
    // persist(orderItemB);
    // persist(orderItemC);
    // persist(order);
    // |
    // V
    // persist(order);
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // xxToOne 같은경우 fetch타입이 디폴트가 즉시로딩(EAGER) 이다. 이렇게되면 Order가 100번 호출되면 매핑되는
    // Delivery를 100번 단일쿼리로 호출하게된다. -> 성능 저하 엄청 중요하므로 xxToOne 같은경우 지연로딩(LAZY)으로 설정하자
    // Order가 한건이고 한번에 Delivery도 같이 조회하고 싶다면 추후에 fetch join을 이용하면 된다고 한다.
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // Date 하면 어노테이션 사용해야 한다

    @Enumerated(EnumType.STRING) // 스트링으로 해야함 다른거 하면 에러남 (한글깨짐)
    private OrderStatus status; // ORDER, CANCEL


    // 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가 합니다");
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회 로직
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    // 연관관계 편의 메서드 -> 아래 로직을 해주는 메서드
   /* public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        // member.getOrders().add(order);
        order.setMember(member);
    }*/

}
