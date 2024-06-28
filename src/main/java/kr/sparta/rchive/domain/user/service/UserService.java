package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    // 유저의 Email로 트랙 ID 찾아오는 로직
    public Long findUserTrackId(User user) {
        return userRepository.findTrackIdByUserEmail(user.getEmail());
    }
}
