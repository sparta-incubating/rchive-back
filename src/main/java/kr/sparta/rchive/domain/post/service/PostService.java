package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostModifyReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomExeption;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostCustomExeption(PostExceptionCode.NOT_FOUND_POST_NOT_EXIST)
        );
    }

    public Post createPost(PostCreateReq request, Track track) {
        Post createPost = Post.builder()
                .postType(request.postType())
                .title(request.title())
                .tutor(request.tutor())
                .uploadedAt(request.uploadedAt())
                .videoLink(request.videoLink())
                .contentLink(request.contentLink())
                .isOpened(request.isOpened())
                .track(track)
                .build();

        return postRepository.save(createPost);
    }

    public Post updatePost(Long id, PostModifyReq request, Track track) {
        Post findPost = findPostById(id);

        findPost.update(request, track);

        return postRepository.save(findPost);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post findPost = findPostById(postId);

        findPost.delete(true);

        postRepository.save(findPost);
    }

    public List<Long> filterPostIdListByTrackId(List<Long> postIdList, Long trackId) {
        return postIdList.stream().filter(
                postId -> Objects.equals(findPostById(postId).getTrack().getId(), trackId)
        ).collect(Collectors.toList());
    }

    public List<Long> filterPostIdListByTrackName(List<Long> postIdList, TrackNameEnum trackName) {
        return postIdList.stream().filter(
                postId -> findPostById(postId).getTrack().getTrackName().equals(trackName)
        ).collect(Collectors.toList());
    }
}
