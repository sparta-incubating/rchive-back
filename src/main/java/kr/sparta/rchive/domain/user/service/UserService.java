package kr.sparta.rchive.domain.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import kr.sparta.rchive.global.execption.CustomException;
import kr.sparta.rchive.global.execption.GlobalCustomException;
import kr.sparta.rchive.global.execption.GlobalExceptionCode;
import kr.sparta.rchive.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            throw new GlobalCustomException(GlobalExceptionCode.BAD_REQUEST_REFRESH_TOKEN_NULL);
        }

        // redis에서 꺼내기
        // redis에 없으면 에러
        // 토큰 일치하는지 확인
        // redis에 저장된게 오늘 날짜가 아니면 새로 발급

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GlobalCustomException(GlobalExceptionCode.BAD_REQUEST_TOKEN_EXPIRED);
        }

        String email = jwtUtil.getEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserCustomException(UserExceptionCode.BAD_REQUEST_EMAIL));

        //make new JWT
        String newAccess = jwtUtil.createAccessToken(user);

        //response
        response.setHeader("Authorization", newAccess);
    }


    
    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackIdByUserEmail(String userEmail) {
        return userRepository.findTrackIdByUserEmail(userEmail);

    }
}
