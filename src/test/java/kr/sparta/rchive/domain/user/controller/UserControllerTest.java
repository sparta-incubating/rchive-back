package kr.sparta.rchive.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneReq;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneValidReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindEmailReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordCheckEmailReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordCheckPhoneReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordUpdateReq;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.dto.response.FindEmailRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.test.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = {UserController.class})
@ActiveProfiles("test")
public class UserControllerTest implements UserTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper obj;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("USER-001 회원가입 테스트")
    public void 회원가입() throws Exception {
        // Given
        UserSignupReq request = UserSignupReq.builder()
                .oAuthType(TEST_STUDENT_USER.getOAuthType())
                .email(TEST_STUDENT_USER.getEmail())
                .password(TEST_STUDENT_USER.getPassword())
                .username(TEST_STUDENT_USER.getUsername())
                .birth(TEST_STUDENT_USER.getBirth())
                .phone(TEST_STUDENT_USER.getPhone())
                .gender(TEST_STUDENT_USER.getGender())
                .profileImg(TEST_STUDENT_USER.getProfileImg())
                .userRole(TEST_STUDENT_USER.getUserRole())
                .termUserAge(TEST_STUDENT_USER.getTermUserAge())
                .termUseService(TEST_STUDENT_USER.getTermUseService())
                .termPersonalInfo(TEST_STUDENT_USER.getTermPersonalInfo())
                .termAdvertisement(TEST_STUDENT_USER.getTermAdvertisement())
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.signup(any(UserSignupReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_SIGNUP.getMessage())
                );
    }

    @Test
    @DisplayName("USER-004 로그아웃 테스트")
    public void 로그아웃() throws Exception {
        // Given
        HttpServletResponse mockResponse = new MockHttpServletResponse();

        // When
        userService.logout(any(HttpServletResponse.class), any(User.class));

        // Then
        mockMvc.perform(delete("/apis/v1/users/logout"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_LOGOUT.getMessage())
                );
    }

    @Test
    @DisplayName("USER-005 토큰 재발급 테스트")
    public void 토큰_재발급() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        userService.reissue(any(HttpServletRequest.class), any(HttpServletResponse.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/reissue"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_REISSUE.getMessage())
                );
    }

    @Test
    @DisplayName("USER-006 회원 탈퇴 테스트")
    public void 회원탈퇴() throws Exception {
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        userService.logout(any(HttpServletResponse.class), any(User.class));
        userService.withdraw(any(User.class));

        // Then
        mockMvc.perform(delete("/apis/v1/users"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_DELETE_USER.getMessage())
                );
    }

    @Test
    @DisplayName("USER-007 이메일 중복 여부 조회 테스트")
    public void 이메일_중복_여부_조회() throws Exception {
        // Given
        String email = TEST_STUDENT_USER.getEmail();

        // When
        when(userService.overlapEmail(any(String.class))).thenReturn(true);

        // Then
        mockMvc.perform(get("/apis/v1/users/overlap/email")
                        .param("email", email))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_OVERLAP_EMAIL.getMessage()),
                        jsonPath("$.data").value(true)
                );

        // When
        when(userService.overlapEmail(any(String.class))).thenReturn(false);

        // Then
        mockMvc.perform(get("/apis/v1/users/overlap/email")
                        .param("email", email))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(UserResponseCode.OK_OVERLAP_EMAIL.getMessage()),
                        jsonPath("$.data").value(false)
                );
    }

    @Test
    @DisplayName("USER-008 닉네임 중복 여부 조회 테스트")
    public void 닉네임_중복_여부_조회() throws Exception {
        // Given
        String nickname = TEST_STUDENT_USER.getNickname();

        // When
        when(userService.overlapNickname(any(String.class))).thenReturn(true);

        // Then
        mockMvc.perform(get("/apis/v1/users/overlap/nickname")
                        .param("nickname", nickname))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_OVERLAP_NICKNAME.getMessage()),
                        jsonPath("$.data").value(true)
                );

        // When
        when(userService.overlapNickname(any(String.class))).thenReturn(false);

        // Then
        mockMvc.perform(get("/apis/v1/users/overlap/nickname")
                        .param("nickname", nickname))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_OVERLAP_NICKNAME.getMessage()),
                        jsonPath("$.data").value(false)
                );
    }

    @Test
    @DisplayName("USER-009 유저 토큰 만료 여부 조회 테스트")
    public void 유저_토큰_만료_여부_조회() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // When
        when(userService.tokenExpired(any(HttpServletRequest.class))).thenReturn(true);

        // Then
        mockMvc.perform(get("/apis/v1/users/token/expired"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_ACCESS_TOKEN_IS_EXPIRED.getMessage()),
                        jsonPath("$.data").value(true)
                );

        // When
        when(userService.tokenExpired(any(HttpServletRequest.class))).thenReturn(false);

        // Then
        mockMvc.perform(get("/apis/v1/users/token/expired"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_ACCESS_TOKEN_IS_EXPIRED.getMessage()),
                        jsonPath("$.data").value(false)
                );
    }

    @Test
    @DisplayName("USER-010 휴대폰 인증 전송 테스트")
    public void 휴대폰_인증_전송() throws Exception {
        // Given
        AuthPhoneReq request = AuthPhoneReq.builder()
                .username(TEST_STUDENT_USER.getUsername())
                .phone(TEST_STUDENT_USER.getPhone())
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.sendAuthPhone(any(AuthPhoneReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/auth/phone/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_SEND_AUTH_PHONE.getMessage())
                );
    }

    @Test
    @DisplayName("USER-011 휴대폰 인증 확인 테스트")
    public void 휴대폰_인증_확인() throws Exception {
        // Given
        AuthPhoneValidReq request = AuthPhoneValidReq.builder()
                .username(TEST_STUDENT_USER.getUsername())
                .phone(TEST_STUDENT_USER.getPhone())
                .authCode("123456")
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.validAuthPhone(any(AuthPhoneValidReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/auth/phone/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_VALID_AUTH_PHONE.getMessage())
                );
    }

    @Test
    @DisplayName("USER-012 이메일 찾기 테스트")
    public void 이메일_찾기() throws Exception {
        // Given
        UserFindEmailReq request = UserFindEmailReq.builder()
                .username(TEST_STUDENT_USER.getUsername())
                .phone(TEST_STUDENT_USER.getPhone())
                .build();
        String json = obj.writeValueAsString(request);

        List<FindEmailRes> responseList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FindEmailRes response = FindEmailRes.builder()
                    .email(TEST_STUDENT_USER.getEmail())
                    .createdAt(LocalDate.parse("2024-09-01"))
                    .build();
            responseList.add(response);
        }

        // When
        given(userService.findUserEmail(any(UserFindEmailReq.class))).willReturn(responseList);

        // Then
        mockMvc.perform(post("/apis/v1/users/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_FIND_EMAIL.getMessage()),
                        jsonPath("$.data[0].email").value(TEST_STUDENT_USER.getEmail()),
                        jsonPath("$.data[0].createdAt").value("2024-09-01")
                );
    }

    @Test
    @DisplayName("USER-013 비밀번호 찾기: 이메일 확인 테스트")
    public void 비밀번호_찾기_이메일_확인() throws Exception {
        // Given
        UserFindPasswordCheckEmailReq request = UserFindPasswordCheckEmailReq.builder()
                .email(TEST_STUDENT_USER.getEmail())
                .username(TEST_STUDENT_USER.getUsername())
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.findUserPasswordCheckEmail(any(UserFindPasswordCheckEmailReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/find/password/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_FIND_PASSWORD_CHECK_EMAIL.getMessage())
                );
    }

    @Test
    @DisplayName("USER-014 비밀번호 찾기: 휴대폰번호 확인 테스트")
    public void 비밀번호_찾기_휴대폰번호_확인() throws Exception {
        // Given
        UserFindPasswordCheckPhoneReq request = UserFindPasswordCheckPhoneReq.builder()
                .email(TEST_STUDENT_USER.getEmail())
                .username(TEST_STUDENT_USER.getUsername())
                .phone(TEST_STUDENT_USER.getPhone())
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.findUserPasswordCheckPhone(any(UserFindPasswordCheckPhoneReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/users/find/password/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_FIND_PASSWORD_CHECK_PHONE.getMessage())
                );
    }

    @Test
    @DisplayName("USER-015 비밀번호 찾기: 비밀번호 변경 테스트")
    public void 비밀번호_찾기_비밀번호_변경() throws Exception {
        // Given
        UserFindPasswordUpdateReq request = UserFindPasswordUpdateReq.builder()
                .email(TEST_STUDENT_USER.getEmail())
                .username(TEST_STUDENT_USER.getUsername())
                .phone(TEST_STUDENT_USER.getPhone())
                .newPassword(TEST_STUDENT_USER.getPassword())
                .build();
        String json = obj.writeValueAsString(request);

        // When
        userService.findUserPasswordUpdate(any(UserFindPasswordUpdateReq.class));

        // Then
        mockMvc.perform(patch("/apis/v1/users/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value(
                                UserResponseCode.OK_FIND_PASSWORD_UPDATE.getMessage())
                );
    }
}
