package com.example.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.userservice.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("UserRepository 단위 테스트")
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("이메일로 사용자를 조회할 수 있어야 한다")
  void should_FindUser_when_EmailExists() {
    // given
    User user =
        User.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .fullName("Test User")
            .build();
    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByEmail("test@example.com");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("testuser");
  }

  @Test
  @DisplayName("사용자명으로 사용자를 조회할 수 있어야 한다")
  void should_FindUser_when_UsernameExists() {
    // given
    User user =
        User.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .build();
    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByUsername("testuser");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  @DisplayName("이메일 중복 여부를 확인할 수 있어야 한다")
  void should_ReturnTrue_when_EmailExists() {
    // given
    User user =
        User.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .build();
    userRepository.save(user);

    // when & then
    assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
  }

  @Test
  @DisplayName("사용자명 중복 여부를 확인할 수 있어야 한다")
  void should_ReturnTrue_when_UsernameExists() {
    // given
    User user =
        User.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .build();
    userRepository.save(user);

    // when & then
    assertThat(userRepository.existsByUsername("testuser")).isTrue();
    assertThat(userRepository.existsByUsername("otheruser")).isFalse();
  }
}
