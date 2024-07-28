package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p " +
            "join fetch p.track t " +
            "left join fetch p.postTagList pt " +
            "left join fetch pt.tag tag " +
            "where p.id = :postId")
    Post findPostWithDetailByPostId(Long postId);

    List<Post> findPostByIdIn(List<Long> postIdList);
}
