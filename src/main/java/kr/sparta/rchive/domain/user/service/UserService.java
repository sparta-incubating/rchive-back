package kr.sparta.rchive.domain.user.service;

import jakarta.transaction.Transactional;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

}
