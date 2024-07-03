package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.PostTrack;
import kr.sparta.rchive.domain.post.entity.PostTrackId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTrackRepository extends JpaRepository<PostTrack, PostTrackId> {
    PostTrack findByPostId(Long postId);
}
