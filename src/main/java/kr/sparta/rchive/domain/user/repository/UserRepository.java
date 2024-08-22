package kr.sparta.rchive.domain.user.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    // User와 Role을 조인하여 트랙 ID를 유저의 email로 검색하는 로직
    @Query("select r.track.id from User u join fetch Role r on u.id = r.user.id where u.email = :email")
    Long findTrackIdByUserEmail(String email);

    @Query("select u from User u where u.username = :username and u.phone = :phone order by u.createdAt asc")
    List<User> findUsersByUsernameAndPhone(String username, String phone);

    boolean existsUserByEmailAndUsernameAndPhone(String email, String username, String phone);
}
