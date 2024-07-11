package kr.sparta.rchive.domain.post.repository;


import java.util.List;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query("select pt.post.id from PostTag pt join fetch Tag t on pt.tag.id = t.id "
            + "join fetch Post p on pt.post.id = p.id "
            + "where t.id = :tagId and p.isDeleted = false")
    List<Long> findPostTagListByTagIdAlive(Long tagId);

    @Query("select p from PostTag p where p.post.id in :postList")
    List<PostTag> findByPostIdIn(List<Long> postList);

    List<PostTag> findByPostId(Long postId);

    @Query("select pt.tag.id from PostTag pt where pt.post.id = :postId")
    List<Long> findTagIdListByPostId(Long postId);
}
