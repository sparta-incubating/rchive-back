package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(PostCreateReq request, Track track, Tutor tutor) {
        Post createPost = Post.builder()
                .postType(request.postType())
                .title(request.title())
                .tutor(tutor)
                .uploadedAt(request.uploadedAt())
                .thumbnailUrl(request.thumbnailUrl())
                .videoLink(request.videoLink())
                .contentLink(request.contentLink())
                .isOpened(request.isOpened())
                .track(track)
                .build();

        return postRepository.save(createPost);
    }

    public Post updatePost(Post post, PostUpdateReq request, Track track, Tutor tutor) {

        post.update(request, track, tutor);

        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Post post) {

        post.delete(true);

        postRepository.save(post);
    }

    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostCustomException(PostExceptionCode.NOT_FOUND_POST)
        );
    }

    @Transactional
    public void openPost(Post post) {
        post.openPost();

        postRepository.save(post);
    }

    @Transactional
    public void closePost(Post post) {
        post.closePost();

        postRepository.save(post);
    }

    public List<Post> findPostListInBackOfficePostTypeAll(Track track, LocalDate startDate,
                                                          LocalDate endDate, Integer searchPeriod, Long tutorId,
                                                          Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeAllByPm(startDate, endDate,
                    isOpened, searchPeriod, track.getTrackName(), tutorId);
        }
        return postRepository.findPostListInBackOfficePostTypeAllByApm(startDate, endDate, isOpened,
                track.getId(), tutorId);
    }

    public List<Post> findPostListInBackOffice(Track track, PostTypeEnum postType,
                                               LocalDate startDate, LocalDate endDate, Integer searchPeriod,
                                               Long tutorId, Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeNotNullByPM(postType, startDate,
                    endDate, searchPeriod, isOpened, track.getTrackName(), tutorId);
        }
        return postRepository.findPostListInBackOfficePostTypeNotNullApm(postType, startDate,
                endDate, track.getId(), isOpened, tutorId);
    }

    public List<Post> findPostListByPostTypeAndTrackId(UserRoleEnum userRole, PostTypeEnum postType,
                                                       Track track) {
        if (userRole.equals(UserRoleEnum.USER)) {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleUser(postType, track.getId());
        } else {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleManager(postType,
                    track.getId());
        }
    }

    public Post findPostWithDetailByPostId(Long postId) {
        return postRepository.findPostWithDetailByPostId(postId);
    }

    public List<Post> findPostListByTagIdWithTagList(Long tagId, Long trackId) {
        return postRepository.findPostListByTagIdAndTrackIdWithTagList(tagId, trackId);
    }
}
