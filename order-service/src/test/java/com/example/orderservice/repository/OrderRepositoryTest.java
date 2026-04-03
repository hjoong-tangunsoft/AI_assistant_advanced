package com.example.orderservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("OrderRepository \ub2e8\uc704 \ud14c\uc2a4\ud2b8")
class OrderRepositoryTest {

  @Autowired private OrderRepository orderRepository;

  @Test
  @DisplayName("\uc0ac\uc6a9\uc790 ID\ub85c \uc8fc\ubb38\uc744 \uc870\ud68c\ud560 \uc218 \uc788\uc5b4\uc57c \ud55c\ub2e4")
  void should_FindOrders_when_UserIdExists() {
    // given
    Order order1 =
        Order.builder()
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.PENDING)
            .build();
    Order order2 =
        Order.builder()
            .userId(1L)
            .productId(2L)
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(10000))
            .status(OrderStatus.CONFIRMED)
            .build();
    Order order3 =
        Order.builder()
            .userId(2L)
            .productId(1L)
            .quantity(3)
            .totalPrice(BigDecimal.valueOf(30000))
            .status(OrderStatus.PENDING)
            .build();
    orderRepository.save(order1);
    orderRepository.save(order2);
    orderRepository.save(order3);

    // when
    List<Order> orders = orderRepository.findByUserId(1L);

    // then
    assertThat(orders).hasSize(2);
  }

  @Test
  @DisplayName("\uc8fc\ubb38 \uc0c1\ud0dc\ub85c \uc870\ud68c\ud560 \uc218 \uc788\uc5b4\uc57c \ud55c\ub2e4")
  void should_FindOrders_when_StatusMatches() {
    // given
    Order order1 =
        Order.builder()
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.PENDING)
            .build();
    Order order2 =
        Order.builder()
            .userId(2L)
            .productId(2L)
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(10000))
            .status(OrderStatus.CONFIRMED)
            .build();
    orderRepository.save(order1);
    orderRepository.save(order2);

    // when
    List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

    // then
    assertThat(pendingOrders).hasSize(1);
    assertThat(pendingOrders.get(0).getUserId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("\uc0ac\uc6a9\uc790 ID\uc640 \uc0c1\ud0dc\ub85c \uc8fc\ubb38\uc744 \uc870\ud68c\ud560 \uc218 \uc788\uc5b4\uc57c \ud55c\ub2e4")
  void should_FindOrders_when_UserIdAndStatusMatch() {
    // given
    Order order1 =
        Order.builder()
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.PENDING)
            .build();
    Order order2 =
        Order.builder()
            .userId(1L)
            .productId(2L)
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(10000))
            .status(OrderStatus.CONFIRMED)
            .build();
    orderRepository.save(order1);
    orderRepository.save(order2);

    // when
    List<Order> result = orderRepository.findByUserIdAndStatus(1L, OrderStatus.PENDING);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
  }
}
