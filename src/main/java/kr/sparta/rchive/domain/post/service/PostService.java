package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.dto.response.PostGetPostTypeRes;
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
import java.util.ArrayList;
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
                .content(request.content())
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

        post.delete();

        postRepository.save(post);
    }

    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostCustomException(PostExceptionCode.NOT_FOUND_POST)
        );
    }

    @Transactional
    public void openPost(List<Post> postList) {
        postList.forEach(Post::openPost);
        postRepository.saveAll(postList);
    }

    public void closePost(List<Post> postList) {
        postList.forEach(Post::closePost);
        postRepository.saveAll(postList);
    }

    public List<Post> findPostListInBackOfficePostTypeAll(Track track, String title, LocalDate startDate,
                                                          LocalDate endDate, Integer searchPeriod, Long tutorId,
                                                          Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeAllByPm(title, startDate, endDate,
                    isOpened, searchPeriod, track.getTrackName(), tutorId);
        }
        return postRepository.findPostListInBackOfficePostTypeAllByApm(title, startDate, endDate, isOpened,
                track.getId(), tutorId);
    }

    public List<Post> findPostListInBackOffice(Track track, PostTypeEnum postType,
        String title, LocalDate startDate, LocalDate endDate, Integer searchPeriod,
                                               Long tutorId, Boolean isOpened) {
        if (track.getPeriod() == 0) {
            return postRepository.findPostListInBackOfficePostTypeNotNullByPM(postType, title, startDate,
                    endDate, searchPeriod, isOpened, track.getTrackName(), tutorId);
        }
        return postRepository.findPostListInBackOfficePostTypeNotNullApm(postType, title, startDate,
                endDate, track.getId(), isOpened, tutorId);
    }

    public List<Post> findPostListByPostTypeAndTrackId(UserRoleEnum userRole, PostTypeEnum postType,
                                                       Track track, Long tutorId) {
        if (userRole.equals(UserRoleEnum.USER)) {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleUser(postType, track.getId(), tutorId);
        } else {
            return postRepository.findAllByPostTypeAndTrackIdUserRoleManager(postType, track.getId(), tutorId);
        }
    }

    public Post findPostWithDetailByPostId(Long postId) {
        return postRepository.findPostWithDetailByPostId(postId);
    }

    public List<Post> findPostListByTagIdWithTagList(Long tagId, Long trackId, PostTypeEnum postType) {
        return postRepository.findPostListByTagIdAndTrackIdWithTagList(tagId, trackId, postType);
    }

    public List<Post> findPostListByPostIdList(List<Long> postIdList) {
        return postRepository.findPostByIdIn(postIdList);
    }

    public List<Post> searchPost(PostTypeEnum postType, String keyword, Long tutorId, Long trackId) {
        return postRepository.findPost(postType, keyword, tutorId, trackId);
    }

    public Post findPostDetail(Long postId) {
        return postRepository.findPostDetail(postId);
    }

    public void deleteThumbnail(Post post) {
        post.deleteThumbnail();

        postRepository.save(post);
    }

    public List<PostGetPostTypeRes> getCategory() {
        List<PostGetPostTypeRes> postGetPostTypeList = new ArrayList<>();

        for(PostTypeEnum postType : PostTypeEnum.values()) {
            PostGetPostTypeRes postGetPostType = PostGetPostTypeRes.builder()
                    .postTypeEnum(postType)
                    .postType(postType.getName())
                    .build();

            postGetPostTypeList.add(postGetPostType);
        }

        return postGetPostTypeList;
    }
}
