package com.example.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.productservice.config.GlobalExceptionHandler;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;
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

@WebMvcTest(ProductController.class)
@DisplayName("ProductController \ub2e8\uc704 \ud14c\uc2a4\ud2b8")
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ProductService productService;

  @Test
  @DisplayName("\uc0c1\ud488 \uc0dd\uc131 API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_CreateProduct_when_ValidRequest() throws Exception {
    CreateProductRequest request =
        CreateProductRequest.builder()
            .name("Test Product")
            .description("Test Description")
            .price(BigDecimal.valueOf(10000))
            .stock(100)
            .category("Electronics")
            .build();

    ProductResponse response =
        ProductResponse.builder()
            .id(1L)
            .name("Test Product")
            .description("Test Description")
            .price(BigDecimal.valueOf(10000))
            .stock(100)
            .category("Electronics")
            .createdAt(LocalDateTime.now())
            .build();

    when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("Test Product"));
  }

  @Test
  @DisplayName("ID\ub85c \uc0c1\ud488 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetProductById_when_ProductExists() throws Exception {
    ProductResponse response =
        ProductResponse.builder()
            .id(1L)
            .name("Test Product")
            .price(BigDecimal.valueOf(10000))
            .stock(100)
            .createdAt(LocalDateTime.now())
            .build();

    when(productService.getProductById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/api/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  @DisplayName("\uc804\uccb4 \uc0c1\ud488 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetAllProducts_when_Requested() throws Exception {
    List<ProductResponse> responses =
        Arrays.asList(
            ProductResponse.builder().id(1L).name("Product1").price(BigDecimal.valueOf(1000)).stock(10).createdAt(LocalDateTime.now()).build(),
            ProductResponse.builder().id(2L).name("Product2").price(BigDecimal.valueOf(2000)).stock(20).createdAt(LocalDateTime.now()).build());

    when(productService.getAllProducts()).thenReturn(responses);

    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(2));
  }

  @Test
  @DisplayName("\uce74\ud14c\uace0\ub9ac\ubcc4 \uc0c1\ud488 \uc870\ud68c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_GetProductsByCategory_when_CategoryExists() throws Exception {
    List<ProductResponse> responses =
        Arrays.asList(
            ProductResponse.builder().id(1L).name("Product1").category("Electronics").price(BigDecimal.valueOf(1000)).stock(10).createdAt(LocalDateTime.now()).build());

    when(productService.getProductsByCategory("Electronics")).thenReturn(responses);

    mockMvc
        .perform(get("/api/products/category/Electronics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  @DisplayName("\uc0c1\ud488 \uc0ad\uc81c API\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_DeleteProduct_when_ProductExists() throws Exception {
    mockMvc
        .perform(delete("/api/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("\uc720\ud6a8\ud558\uc9c0 \uc54a\uc740 \uc0c1\ud488 \uc0dd\uc131 \uc694\uccad \uc2dc 400 \uc5d0\ub7ec\uac00 \ubc18\ud658\ub418\uc5b4\uc57c \ud55c\ub2e4")
  void should_Return400_when_InvalidProductRequest() throws Exception {
    CreateProductRequest request = CreateProductRequest.builder().build();

    mockMvc
        .perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
