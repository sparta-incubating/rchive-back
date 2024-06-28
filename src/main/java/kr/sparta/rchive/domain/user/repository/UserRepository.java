package kr.sparta.rchive.domain.user.repository;

import kr.sparta.rchive.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // User와 Role을 조인하여 트랙 ID를 검색하는 로직
    @Query("select r.track.id from User u join fetch Role r on u.id = r.user.id where u.email = :email")
    Long findTrackId(String email);
}
