package kr.sparta.rchive.domain.post.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameContains(String tagName);

    Optional<Tag> findByTagName(String tagName);

    @Query("select t from Tag t where t.tagName = :tagName")
    Tag findByTagNameNotOptional(String tagName);

    List<Tag> findAllByIdIn(List<Long> tagIdList);
}
