package com.example.jpatest.api;

import com.example.jpatest.domain.Address;
import com.example.jpatest.domain.Order;
import com.example.jpatest.domain.OrderItem;
import com.example.jpatest.domain.OrderStatus;
import com.example.jpatest.dto.OrderFlatDto;
import com.example.jpatest.dto.OrderQueryDto;
import com.example.jpatest.repository.OrderRepository;
import com.example.jpatest.repository.OrderSearch;
import com.example.jpatest.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        // V1 안 좋은 예시
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        // N + 1 쿼리 진짜 엄청 나감
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        // 페치 조인 적용
        // 이거 뭐시냐 데이터베이스에서 쿼리 더 나와서 중복된 엔티티 나옴 distinct 해줍시다 (엔티티 중복도 처리해줌)
        // 심지어 V2랑 로직차이 거의없음 패치만 추가됐음
        // * 일대다를 페치 조인 하는순간 페이징 힘듬 *
        // * 컬렉션 페치 조인은 딱 1개만 더 하지마 제발 *
        // 쿼리는 1방에 나가지만 일대다 데이터는 늘어나는데 중복데이터가 많아짐 -> 용량이 많아짐 성능이 안 좋음
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "offset", defaultValue = "100") int limit) {
        // 페이징 처리
        // join fetch끼리 먼저 호출하고 나머지는 지연로딩으로
        // 쿼리는 3방 나가지만 위 V3의 이슈처럼 중복이 없음 데이터가 잘 정재됨
        // In 쿼리 사용하기 때문에 정규화된 데이터로 잘 가져옴 default_batch_fetch_size 설정해야됨
        List<Order> orders = orderRepository.findAllwithMemberDelivery(offset, limit);

        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        // 가독성이 좋고 유지보수가 쉽다 단 N+1
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        // 쿼리 2번 in절 활용했음
        return orderQueryRepository.findAllByDtos_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        // 쿼리 1번이지만 페이징 불가 중복처리 해주는 로직이 자바단에서 처리해줘야함
        return orderQueryRepository.findAllByDtos_flat();
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems; // 이렇게 매핑해서도 노출되면 안된다

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
