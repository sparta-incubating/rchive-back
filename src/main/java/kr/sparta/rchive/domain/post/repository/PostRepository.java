package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
