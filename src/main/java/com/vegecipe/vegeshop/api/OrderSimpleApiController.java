package com.vegecipe.vegeshop.api;

import com.vegecipe.vegeshop.domain.Address;
import com.vegecipe.vegeshop.domain.Order;
import com.vegecipe.vegeshop.domain.OrderStatus;
import com.vegecipe.vegeshop.repository.OrderRepository;
import com.vegecipe.vegeshop.repository.OrderSearch;
import com.vegecipe.vegeshop.repository.order.query.OrderQueryRepository;
import com.vegecipe.vegeshop.repository.order.query.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * X to One(ManyToOne, OneToOne) 관계
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order: all) {
            order.getMember().getName(); // LAZY 로딩 강제 실행
            order.getDelivery().getAddress(); // LAZY 로딩 강제 실행
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3/simple-orders")    // 실무에서 가장 많이 쓰는 방법임 Fetch join
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    // 성능은 V3에 조금 나을수 있다.(생각보다 미비) // 자주 발생하는 고객의 트레픽에서는 한번 생각해봐야함.
    // 하지만. Fix 되버린 DTO 때문에 확장성이 V3에 비해 나쁘다.
    // Repository에 API 스펙이 드러와서 API 스펙이 변경되면 Repository를 뜯어 고쳐야 됨.
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress();
        }
    }
}

