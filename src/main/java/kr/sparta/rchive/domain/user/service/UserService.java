package kr.sparta.rchive.domain.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import kr.sparta.rchive.global.execption.GlobalCustomException;
import kr.sparta.rchive.global.execption.GlobalExceptionCode;
import kr.sparta.rchive.global.redis.RedisService;
import kr.sparta.rchive.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
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
    public void signup(UserSignupReq req){

        if(userRepository.existsByEmail(req.email())){
            throw new UserCustomException(UserExceptionCode.CONFLICT_EMAIL);
        }

        if(req.nickname()!=null && userRepository.existsByNickname(req.nickname())){
            throw new UserCustomException(UserExceptionCode.CONFLICT_NICKNAME);
        }

        User user = User.builder()
                .email(req.email())
                .password(bCryptPasswordEncoder.encode(req.password()))
                .oAuthType(req.oAuthType())
                .oAuthId(req.oAuthId())
                .birth(req.birth())
                .phone(req.phone())
                .gender(req.gender())
                .nickname(req.nickname())
                .userRole(req.userRole())
                .build();

        userRepository.save(user);
    }

    public void logout(HttpServletResponse res, User user)
            throws UnsupportedEncodingException {
        redisService.deleteRefreshToken(user);

        Cookie refresh = jwtUtil.addRefreshTokenToCookie("");
        refresh.setMaxAge(0);
        res.addCookie(refresh);
    }

    public void reissue(HttpServletRequest req, HttpServletResponse res)
            throws ParseException, UnsupportedEncodingException {

        String refreshToken = null;
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            throw new GlobalCustomException(GlobalExceptionCode.BAD_REQUEST_REFRESH_TOKEN_NULL);
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GlobalCustomException(GlobalExceptionCode.BAD_REQUEST_TOKEN_EXPIRED);
        }

        String email = jwtUtil.getEmail(refreshToken);
        User user = findByEmailAlive(email);

        String redisRefresh = redisService.getRefreshToken(user);
        if(!redisRefresh.equals(refreshToken)){
            throw new GlobalCustomException(GlobalExceptionCode.BAD_REQUEST_REFRESH_NOT_MATCH);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date issuedAt = jwtUtil.getIssuedAt(redisRefresh);
        Date date = new Date();

        issuedAt = sdf.parse(sdf.format(issuedAt));
        date = sdf.parse(sdf.format(date));

        if(!issuedAt.equals(date)){
            String newRefresh = jwtUtil.createRefreshToken(user);
            redisService.setRefreshToken(user,newRefresh);
            res.addCookie(jwtUtil.addRefreshTokenToCookie(newRefresh));
        }

        String newAccess = jwtUtil.createAccessToken(user);
        res.setHeader("Authorization", newAccess);
    }

    @Transactional
    public void withdraw(User user) {
        user.delete();
        userRepository.save(user);
    }

    public User findByEmailAlive(String email){
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(()-> new UserCustomException(UserExceptionCode.BAD_REQUEST_EMAIL));
    }

    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackIdByUserEmail(String userEmail) {
        return userRepository.findTrackIdByUserEmail(userEmail);

    }
}
