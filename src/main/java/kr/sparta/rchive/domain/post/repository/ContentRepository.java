package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Content findByPostId(Long postId);
}
