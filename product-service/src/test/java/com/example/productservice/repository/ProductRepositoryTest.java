package com.example.productservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.productservice.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("ProductRepository \ub2e8\uc704 \ud14c\uc2a4\ud2b8")
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;

  @Test
  @DisplayName("\uce74\ud14c\uace0\ub9ac\ubcc4 \uc0c1\ud488 \uc870\ud68c\uac00 \uc815\uc0c1 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_FindProducts_when_CategoryExists() {
    // given
    Product product1 =
        Product.builder()
            .name("Laptop")
            .price(BigDecimal.valueOf(1500000))
            .stock(10)
            .category("Electronics")
            .build();
    Product product2 =
        Product.builder()
            .name("T-Shirt")
            .price(BigDecimal.valueOf(30000))
            .stock(50)
            .category("Clothing")
            .build();
    productRepository.save(product1);
    productRepository.save(product2);

    // when
    List<Product> electronics = productRepository.findByCategory("Electronics");

    // then
    assertThat(electronics).hasSize(1);
    assertThat(electronics.get(0).getName()).isEqualTo("Laptop");
  }

  @Test
  @DisplayName("\uc7ac\uace0\uac00 \ud2b9\uc815 \uc218\ub7c9 \uc774\uc0c1\uc778 \uc0c1\ud488\uc744 \uc870\ud68c\ud560 \uc218 \uc788\uc5b4\uc57c \ud55c\ub2e4")
  void should_FindProducts_when_StockGreaterThan() {
    // given
    Product product1 =
        Product.builder()
            .name("Product A")
            .price(BigDecimal.valueOf(1000))
            .stock(5)
            .build();
    Product product2 =
        Product.builder()
            .name("Product B")
            .price(BigDecimal.valueOf(2000))
            .stock(20)
            .build();
    productRepository.save(product1);
    productRepository.save(product2);

    // when
    List<Product> result = productRepository.findByStockGreaterThan(10);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Product B");
  }

  @Test
  @DisplayName("\uc0c1\ud488\uba85 \uac80\uc0c9\uc774 \ub300\uc18c\ubb38\uc790 \uad6c\ubd84 \uc5c6\uc774 \ub3d9\uc791\ud574\uc57c \ud55c\ub2e4")
  void should_FindProducts_when_NameContainsIgnoreCase() {
    // given
    Product product =
        Product.builder()
            .name("MacBook Pro")
            .price(BigDecimal.valueOf(2500000))
            .stock(10)
            .build();
    productRepository.save(product);

    // when
    List<Product> result = productRepository.findByNameContainingIgnoreCase("macbook");

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("MacBook Pro");
  }
}
