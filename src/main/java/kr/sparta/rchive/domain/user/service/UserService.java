package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
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
    public boolean signup(UserSignupReq req){

        if(userRepository.existsByEmail(req.email())){
            return false;
        }

        if(userRepository.existsByNickname(req.nickname())){
            return false;
        }

        User user = User.builder()
                .email(req.email())
                .password(bCryptPasswordEncoder.encode(req.password()))
                .oAuthType(req.oAuthType())
//                .oAuthId(req.oAuthId())
                .birth(req.birth())
                .phone(req.phone())
                .gender(req.gender())
                .nickname(req.nickname())
                .userRole(req.userRole())
                .isDeleted(false)
                .build();

        userRepository.save(user);
        return true;
    }
    
    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackIdByUserEmail(String userEmail) {
        return userRepository.findTrackIdByUserEmail(userEmail);

    }
}
