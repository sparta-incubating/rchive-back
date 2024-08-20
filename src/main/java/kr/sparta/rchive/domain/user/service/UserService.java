package kr.sparta.rchive.domain.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePasswordReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePhoneReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdateReq;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import kr.sparta.rchive.global.execption.GlobalCustomException;
import kr.sparta.rchive.global.execption.GlobalExceptionCode;
import kr.sparta.rchive.global.redis.RedisService;
import kr.sparta.rchive.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Transactional
    public void signup(UserSignupReq req) {

        if (!req.termUserAge() || !req.termUseService() || !req.termPersonalInfo()) {
            throw new UserCustomException(UserExceptionCode.BAD_REQUEST_DISAGREE_TERMS);
        }

        if (userRepository.existsByEmail(req.email())) {
            throw new UserCustomException(UserExceptionCode.CONFLICT_EMAIL);
        }

        if (req.userRole() == UserRoleEnum.USER) {
            if (userRepository.existsByNickname(req.nickname())) {
                throw new UserCustomException(UserExceptionCode.CONFLICT_NICKNAME);
            }
        } else {
            if (req.nickname() != null) {
                throw new UserCustomException(UserExceptionCode.BAD_REQUEST_MANAGER_NICKNAME);
            }
        }

        // TODO: profileImg null이면 기본값 넣어주기

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

//        Cookie refresh = jwtUtil.addRefreshTokenToCookie("");
//        refresh.setMaxAge(0);
//        res.addCookie(refresh);
        res.addHeader("Set-Cookie", jwtUtil.removeRefreshTokenToCookie().toString());

    }

    public void reissue(HttpServletRequest req, HttpServletResponse res)
            throws ParseException, UnsupportedEncodingException {

        String refreshToken = null;
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            System.out.println("yes cookieeeeeeeee");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Refresh")) {
                    refreshToken = cookie.getValue();
                }
            }
        } else {
            System.out.println("no cookieeeeeeeee");
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
//            res.addCookie(jwtUtil.addRefreshTokenToCookie(newRefresh));
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
    public void updateProfile(User user, ProfileUpdateReq req) {
        if (user.getUserRole() == UserRoleEnum.USER) {
            if (userRepository.existsByNickname(req.nickname())) {
                throw new UserCustomException(UserExceptionCode.CONFLICT_NICKNAME);
            }
            user.updateProfileByUser(req.profileImg(), req.nickname());
        } else {
            user.updateProfileByManager(req.profileImg());
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
