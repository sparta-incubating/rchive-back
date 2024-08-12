package kr.sparta.rchive.domain.post.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = "SELECT * FROM tb_tag "
        + "WHERE REPLACE(tag_name, ' ', '') LIKE CONCAT('%', REPLACE(:tagName, ' ', ''), '%') "
        + "OR REPLACE(tag_name, '-', '') LIKE CONCAT('%', REPLACE(:tagName, ' ', ''), '%') "
        + "OR REPLACE(tag_name, '_', '') LIKE CONCAT('%', REPLACE(:tagName, ' ', ''), '%') "
        + "OR tag_name LIKE CONCAT('%', :tagName, '%')", nativeQuery = true)
    List<Tag> findByTagNameContains(String tagName);

    Optional<Tag> findByTagName(String tagName);

    @Query("select t from Tag t where t.tagName = :tagName")
    Tag findByTagNameNotOptional(String tagName);

    List<Tag> findAllByIdIn(List<Long> tagIdList);
}
