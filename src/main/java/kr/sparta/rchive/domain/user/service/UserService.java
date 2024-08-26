package kr.sparta.rchive.domain.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneReq;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneValidReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdateProfileImgReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindEmailReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordCheckEmailReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordCheckPhoneReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePasswordReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePhoneReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdateNicknameReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordUpdateReq;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.dto.response.FindEmailRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import kr.sparta.rchive.global.redis.RedisService;
import kr.sparta.rchive.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    @Value("${effic.access-key}")
    private String efficAccessKey;

    @Transactional
    public void signup(UserSignupReq req) {

        if (!req.termUserAge() || !req.termUseService() || !req.termPersonalInfo()) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_DISAGREE_TERMS);
        }

        if (userRepository.existsByEmail(req.email())) {
            throw new UserCustomException(UserExceptionCode.CONFLICT_EMAIL);
        }

        if (req.nickname().isEmpty()) {
            if (req.userRole() == UserRoleEnum.USER) {
                throw new UserCustomException(UserExceptionCode.BAD_REQUEST_STUDENT_NICKNAME_NULL);
            }
        } else {
            if (userRepository.existsByNickname(req.nickname())) {
                throw new UserCustomException(UserExceptionCode.CONFLICT_NICKNAME);
            }
        }

        User user = User.builder()
                .email(req.email())
                .password(bCryptPasswordEncoder.encode(req.password()))
                .oAuthType(req.oAuthType())
                //.oAuthId(req.oAuthId())
                .username(req.username())
                .birth(req.birth())
                .phone(req.phone())
                .gender(req.gender())
                .profileImg(req.profileImg())
                .nickname(req.nickname())
                .userRole(req.userRole())
                .termUserAge(req.termUserAge())
                .termUseService(req.termUseService())
                .termPersonalInfo(req.termPersonalInfo())
                .termAdvertisement(req.termAdvertisement())
                .build();

        userRepository.save(user);
    }

    public void logout(HttpServletResponse res, User user)
            throws UnsupportedEncodingException {
        redisService.deleteRefreshToken(user);
        res.addHeader("Set-Cookie", jwtUtil.removeRefreshTokenToCookie().toString());
    }

    public void reissue(HttpServletRequest req, HttpServletResponse res)
            throws ParseException, UnsupportedEncodingException {

        String refreshToken = null;
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Refresh")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_REFRESH_TOKEN_NULL);
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_TOKEN_EXPIRED);
        }

        String email = jwtUtil.getEmail(refreshToken);
        User user = findByEmailAlive(email);

        String redisRefresh = redisService.getRefreshToken(user);
        if (!redisRefresh.equals(refreshToken)) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_REFRESH_TOKEN_NOT_MATCH);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date issuedAt = jwtUtil.getIssuedAt(redisRefresh);
        Date date = new Date();

        issuedAt = sdf.parse(sdf.format(issuedAt));
        date = sdf.parse(sdf.format(date));

        if (!issuedAt.equals(date)) {
            String newRefresh = jwtUtil.createRefreshToken(user);
            redisService.setRefreshToken(user, newRefresh);
            res.addHeader("Set-Cookie", jwtUtil.addRefreshTokenToCookie(newRefresh).toString());
        }

        String newAccess = jwtUtil.createAccessToken(user);
        res.setHeader("Authorization", newAccess);
    }

    @Transactional
    public void withdraw(User user) {
        user.delete();
        userRepository.save(user);
    }

    @Transactional
    public void updateNickname(User user, ProfileUpdateNicknameReq req) {
        if (!req.nickname().isEmpty()) {
            if (user.getNickname().equals(req.nickname())) {
                throw new UserCustomException(UserExceptionCode.BAD_REQUEST_SAME_NICKNAME);
            } else if (userRepository.existsByNickname(req.nickname())) {
                throw new UserCustomException(UserExceptionCode.CONFLICT_NICKNAME);
            }
            user.updateNickname(req.nickname());
        }

        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(User user, ProfileUpdatePasswordReq req) {
        if (!bCryptPasswordEncoder.matches(req.originPassword(), user.getPassword())) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_NO_MATCH_PASSWORD);
        }
        user.updatePassword(bCryptPasswordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updatePhone(User user, ProfileUpdatePhoneReq req) {
        user.updatePhone(req.phone());
        userRepository.save(user);
    }

    @Transactional
    public void updateProfileImg(User user, ProfileUpdateProfileImgReq req) {
        user.updateProfileImg(req.profileImg());
        userRepository.save(user);
    }

    public void sendAuthPhone(AuthPhoneReq req) {
        String url = "https://api.effic.biz/auto-message-event/template";
        String authCode = generateAuthCode();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(efficAccessKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "rchive");
        requestBody.put("receiverNumber", req.phone());

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", req.username());
        variables.put("brandName", "르탄이의 아카이브");
        variables.put("verificationCode", authCode);

        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        String responseBody = response.getBody();

        redisService.setAuthPhone(req.username(), req.phone(), authCode);
    }

    public String generateAuthCode() {
        Random random = new Random();
        int authCode = random.nextInt(1000000);
        return String.format("%06d", authCode);
    }

    public void validAuthPhone(AuthPhoneValidReq req) {
        String AuthCodeInRedis = redisService.getAuthPhone(req.username(), req.phone());

        if (AuthCodeInRedis == null) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_PHONE_AUTH_EXPIRED);
        }

        if (!req.authCode().equals(AuthCodeInRedis)) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_PHONE_AUTH);
        }

    }

    public List<FindEmailRes> findUserEmail(UserFindEmailReq req) {
        List<User> userList = userRepository.findUsersByUsernameAndPhoneAndAlive(
                req.username(), req.phone());
        if (userList.isEmpty()) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_USER);
        }

        List<FindEmailRes> resList = new ArrayList<>();
        for (User user : userList) {
            resList.add(FindEmailRes.builder()
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt().toLocalDate())
                    .build());
        }

        return resList;
    }


    public void findUserPasswordCheckEmail(UserFindPasswordCheckEmailReq req) {
        boolean isUser = userRepository.existsUserByEmailAndUsernameAndAlive(
                req.email(), req.username());
        if (!isUser) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_USER);
        }
    }

    public void findUserPasswordCheckPhone(UserFindPasswordCheckPhoneReq req) {
        boolean isUser = userRepository.existsUserByEmailAndUsernameAndPhoneAndAlive(
                req.email(), req.username(), req.phone());
        if (!isUser) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_USER);
        }
    }

    @Transactional
    public void findUserPasswordUpdate(UserFindPasswordUpdateReq req) {
        boolean isUser = userRepository.existsUserByEmailAndUsernameAndPhoneAndAlive(
                req.email(), req.username(), req.phone());
        if (!isUser) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_USER);
        }

        User user = findByEmailAlive(req.email());
        user.updatePassword(bCryptPasswordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    public boolean overlapEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean overlapNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User findByEmailAlive(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UserCustomException(UserExceptionCode.BAD_REQUEST_EMAIL));
    }

    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackIdByUserEmail(String userEmail) {
        return userRepository.findTrackIdByUserEmail(userEmail);
    }

    public Boolean tokenExpired(HttpServletRequest req) {
        String accessToken = req.getHeader("Authorization");

        String token = accessToken.split(" ")[1];

        try {
            return jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }


}
