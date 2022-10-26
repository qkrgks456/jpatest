package com.example.jpatest.api;

import com.example.jpatest.domain.Order;
import com.example.jpatest.dto.OrderSimpleDto;
import com.example.jpatest.repository.OrderRepository;
import com.example.jpatest.repository.OrderSearch;
import com.example.jpatest.repository.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// xtoOne (ManyToOne,OneToOne)
// Order -> Member
// Order -> Delivery
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        // 이렇게 하면 무한루프 탄다
        // JsonIgnore,hibernate모듈 등등 방법은 있으나 그냥 dto로 주면 복잡한거 다 사라짐
        return orderRepository.findAllByString(new OrderSearch());
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleDto> orderV2() {
        // 그냥 dto로 주면 복잡한거 다 사라짐 편안
        // 그런데 LAZY 로딩으로 쿼리가 많이 나간다 -> N + 1 = 1 + 회원 N + 배송 N
        // EAGER 쓰면 더 박살남
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleDto> orderV3() {
        // 쿼리 단 한번에 엔딩 "편안"
        return orderRepository.findAllwithMemberDelivery()
                .stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());
    }

    // V3와 V4번 각각 장단점
    // V3 장점 : 페치조인이라서 엔티티로 담겨있기 때문에 비즈니스 로직에서 값 수정 가능, 재사용성 높음
    // V3 단점 : 코드 길다 (dto로 변환하는 로직 필요), 원하는 컬럼만 x
    // V4 장점 : 원하는 컬럼만 조회 성능 좋음, 코드 간결
    // V4 단점 : 재사용성 안좋음 정해진 Dto로만 받을 수 있음

    // * 중요
    // 그런데 컬럼 몇개가 더 있다고 실제 성능에 영향을 줄까?
    // 영한팀장님 피셜 where 또는 join index 같은 쿼리들이 성능에 더 주요한 영향을 미친다.
    // 실무에서 컬럼이 너무 많거나 트래픽이 높지 요청이 아닌 이상 거의 대부분 성능 이슈가 없었다고 한다.
    // 그리고 성능에 영향을 크게 주는 부분을 신경쓰는게 낫다.

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleDto> orderV4() {
        // dto까지 한방에 그리고 원하는 컬럼만 가져온다. 일반조인
        // 페치 조인은 데이터의 정합성 규칙으로 인하여 원하는 컬럼만 선택 불가
        return orderSimpleRepository.findOrderDtos();
    }

    // 결론 권장 방법
    // 1. 엔티티를 dto로 변환 (V2)
    // 2. 필요하면 fetch조인 사용 (V3) -- 대부분 여기서 해결됨
    // 3. 그래도 안되면 맞춤 dto 사용 (V4)
    // 4. 네이티브 SQL, spring jdbc template 사용

}
