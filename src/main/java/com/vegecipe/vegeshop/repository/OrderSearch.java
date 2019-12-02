package com.vegecipe.vegeshop.repository;

import com.vegecipe.vegeshop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter @Setter
public class OrderSearch {

    private String memberName; //회원 이름
    private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]
}
