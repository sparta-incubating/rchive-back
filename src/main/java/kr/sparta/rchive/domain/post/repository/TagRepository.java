package kr.sparta.rchive.domain.post.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameContains(String tag);

    Optional<Tag> findByTagName(String tag);
}
