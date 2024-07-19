package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostModifyReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.exception.PostCustomExeption;
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

    public Post createPost(PostCreateReq request, Track track) {
        Post createPost = Post.builder()
                .postType(request.postType())
                .title(request.title())
                .tutor(request.tutor())
                .uploadedAt(request.uploadedAt())
                .thumbnailUrl(request.thumbnailUrl())
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

    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostCustomExeption(PostExceptionCode.NOT_FOUND_POST_NOT_EXIST)
        );
    }

    @Transactional
    public void openPost(Long postId) {
        Post post = findPostById(postId);

        post.openPost();

        postRepository.save(post);
    }

    @Transactional
    public void closePost(Long postId) {
        Post post = findPostById(postId);

        post.closePost();

        postRepository.save(post);
    }

    public List<Post> findPostListInBackOfficePostTypeAll(Track track, LocalDate startDate, LocalDate endDate, Integer searchPeriod, Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeAllByPm(startDate, endDate, isOpened, searchPeriod);
        }
        return postRepository.findPostListInBackOfficePostTypeAllByApm(startDate, endDate, isOpened, track.getPeriod());
    }

    public List<Post> findPostListInBackOffice(Track track, PostTypeEnum postType, LocalDate startDate, LocalDate endDate, Integer searchPeriod, Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeNotNullByPM(postType, startDate, endDate, searchPeriod, isOpened);
        }
        return postRepository.findPostListInBackOfficePostTypeNotNullApm(postType, startDate, endDate, track.getPeriod(), isOpened);
    }

    public List<Post> findPostListByPostTypeAndTrackId(UserRoleEnum userRole, PostTypeEnum postType, Track track) {
        if(userRole.equals(UserRoleEnum.USER)) {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleUser(postType, track.getId());
        }
        else {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleManager(postType, track.getId());
        }
    }

    public Post findPostWithDetailByPostId(Long postId) {
        return postRepository.findPostWithDetailByPostId(postId);
    }

    public List<Post> findPostListByTagIdWithTagList(Long tagId, Long trackId) {
        return postRepository.findPostListByTagIdAndTrackIdWithTagList(tagId, trackId);
    }
}
