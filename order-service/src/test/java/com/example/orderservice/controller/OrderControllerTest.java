package com.example.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.orderservice.config.GlobalExceptionHandler;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@DisplayName("OrderController \ub2e8\uc704 \ud14c\uc2a4\ud2b8")
class OrderControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private OrderService orderService;

  @Test
  @DisplayName("\uc8fc\ubb38 \uc0dd\uc131 API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_CreateOrder_when_ValidRequest() throws Exception {
    CreateOrderRequest request =
        CreateOrderRequest.builder()
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .unitPrice(BigDecimal.valueOf(10000))
            .build();

    OrderResponse response =
        OrderResponse.builder()
            .id(1L)
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();

    when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("PENDING"));
  }

  @Test
  @DisplayName("ID\ub85c \uc8fc\ubb38 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetOrderById_when_OrderExists() throws Exception {
    OrderResponse response =
        OrderResponse.builder()
            .id(1L)
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();

    when(orderService.getOrderById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/api/orders/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  @DisplayName("\uc804\uccb4 \uc8fc\ubb38 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetAllOrders_when_Requested() throws Exception {
    List<OrderResponse> responses =
        Arrays.asList(
            OrderResponse.builder().id(1L).userId(1L).productId(1L).quantity(1).totalPrice(BigDecimal.valueOf(10000)).status(OrderStatus.PENDING).createdAt(LocalDateTime.now()).build(),
            OrderResponse.builder().id(2L).userId(2L).productId(2L).quantity(3).totalPrice(BigDecimal.valueOf(30000)).status(OrderStatus.CONFIRMED).createdAt(LocalDateTime.now()).build());

    when(orderService.getAllOrders()).thenReturn(responses);

    mockMvc
        .perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(2));
  }

  @Test
  @DisplayName("\uc0ac\uc6a9\uc790 ID\ub85c \uc8fc\ubb38 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetOrdersByUserId_when_UserExists() throws Exception {
    List<OrderResponse> responses =
        Arrays.asList(
            OrderResponse.builder().id(1L).userId(1L).productId(1L).quantity(1).totalPrice(BigDecimal.valueOf(10000)).status(OrderStatus.PENDING).createdAt(LocalDateTime.now()).build());

    when(orderService.getOrdersByUserId(1L)).thenReturn(responses);

    mockMvc
        .perform(get("/api/orders/user/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  @DisplayName("\uc8fc\ubb38 \ucde8\uc18c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_CancelOrder_when_OrderExists() throws Exception {
    OrderResponse response =
        OrderResponse.builder()
            .id(1L)
            .userId(1L)
            .productId(1L)
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(20000))
            .status(OrderStatus.CANCELLED)
            .createdAt(LocalDateTime.now())
            .build();

    when(orderService.cancelOrder(1L)).thenReturn(response);

    mockMvc
        .perform(post("/api/orders/1/cancel"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("CANCELLED"));
  }

  @Test
  @DisplayName("\uc720\ud6a8\ud558\uc9c0 \uc54a\uc740 \uc8fc\ubb38 \uc0dd\uc131 \uc694\uccad \uc2dc 400 \uc5d0\ub7ec\uac00 \ubc18\ud658\ub418\uc5b4\uc57c \ud55c\ub2e4")
  void should_Return400_when_InvalidOrderRequest() throws Exception {
    CreateOrderRequest request = CreateOrderRequest.builder().build();

    mockMvc
        .perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
