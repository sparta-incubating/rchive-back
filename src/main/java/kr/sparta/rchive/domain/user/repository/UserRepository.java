package kr.sparta.rchive.domain.user.repository;

import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmailAndOAuthType(String email, OAuthTypeEnum oAuthTypeEnum);

    Optional<User> findByEmailAndOAuthType(String email, OAuthTypeEnum oAuthTypeEnum);

    Optional<User> findByEmailAndRefreshToken(String email, String refreshToken);
}
