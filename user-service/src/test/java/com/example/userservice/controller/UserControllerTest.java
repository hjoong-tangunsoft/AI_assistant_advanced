package com.example.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.userservice.config.GlobalExceptionHandler;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(UserController.class)
@DisplayName("UserController 단위 테스트")
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  @Test
  @DisplayName("사용자 생성 API가 정상 동작해야 한다")
  void should_CreateUser_when_ValidRequest() throws Exception {
    CreateUserRequest request =
        CreateUserRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .fullName("Test User")
            .build();

    UserResponse response =
        UserResponse.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .fullName("Test User")
            .createdAt(LocalDateTime.now())
            .build();

    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.username").value("testuser"));
  }

  @Test
  @DisplayName("ID로 사용자 조회 API가 정상 동작해야 한다")
  void should_GetUserById_when_UserExists() throws Exception {
    UserResponse response =
        UserResponse.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .createdAt(LocalDateTime.now())
            .build();

    when(userService.getUserById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  @DisplayName("전체 사용자 조회 API가 정상 동작해야 한다")
  void should_GetAllUsers_when_Requested() throws Exception {
    List<UserResponse> responses =
        Arrays.asList(
            UserResponse.builder().id(1L).username("user1").email("u1@test.com").createdAt(LocalDateTime.now()).build(),
            UserResponse.builder().id(2L).username("user2").email("u2@test.com").createdAt(LocalDateTime.now()).build());

    when(userService.getAllUsers()).thenReturn(responses);

    mockMvc
        .perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(2));
  }

  @Test
  @DisplayName("사용자 삭제 API가 정상 동작해야 한다")
  void should_DeleteUser_when_UserExists() throws Exception {
    mockMvc
        .perform(delete("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @DisplayName("유효하지 않은 요청 시 400 에러가 반환되어야 한다")
  void should_Return400_when_InvalidRequest() throws Exception {
    CreateUserRequest request = CreateUserRequest.builder().build();

    mockMvc
        .perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
