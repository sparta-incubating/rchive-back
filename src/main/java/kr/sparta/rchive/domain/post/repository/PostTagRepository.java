package kr.sparta.rchive.domain.post.repository;


import java.util.List;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query("select pt.post.id from PostTag pt join fetch Tag t on pt.tag.Id = t.Id "
            + "join fetch Post p on pt.post.id = p.id "
            + "where t.Id = :tagId and p.isDeleted = false")
    List<Long> findPostTagListByTagIdAndALIVE(Long tagId);

    @Query("select p from PostTag p where p.post.id in :postList")
    List<PostTag> findByPostIdIn(List<Long> postList);

    List<PostTag> findByPostId(Long postId);
}
