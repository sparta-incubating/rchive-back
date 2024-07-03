package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.domain.user.repository.UserRepository;
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
    
    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackIdByUserEmail(String userEmail) {
        return userRepository.findTrackIdByUserEmail(userEmail);

    }
}
