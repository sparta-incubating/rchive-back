package kr.sparta.rchive.global.security;

import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByEmailAndPlatform(String email, OAuthTypeEnum oauthTypeEnum) throws UsernameNotFoundException {

        User user = userRepository.findByEmailAndOAuthType(email, oauthTypeEnum)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));

        return new UserDetailsImpl(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
