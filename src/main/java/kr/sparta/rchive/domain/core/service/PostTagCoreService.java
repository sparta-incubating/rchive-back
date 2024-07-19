package kr.sparta.rchive.domain.core.service;

import java.util.ArrayList;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.post.dto.PostSearchInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostModifyReq;
import kr.sparta.rchive.domain.post.dto.response.*;
import kr.sparta.rchive.domain.post.entity.Content;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.ContentService;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.PostTagService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostTagCoreService {

    private final PostService postService;
    private final PostTagService postTagService;
    private final TrackService trackService;
    private final TagService tagService;
    private final UserService userService;
    private final RoleService roleService;
    private final ContentService contentService;
    private final CommentService commentService;
    private final RedisService redisService;


    public Page<PostSearchBackOfficeRes> getPostListInBackOffice(
            User user, TrackNameEnum trackName, Integer period, PostTypeEnum postType,
            LocalDate startDate, LocalDate endDate,
            Integer searchPeriod, Boolean isOpened, Pageable pageable
    ) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        userRoleAndTrackCheck(user, track);

        List<Post> postList = new ArrayList<>();

        if (postType == null) {
            postList = postService.findPostListInBackOfficePostTypeAll(track, startDate, endDate,
                    searchPeriod, isOpened);
        } else {
            postList = postService.findPostListInBackOffice(track, postType, startDate, endDate,
                    searchPeriod, isOpened);
        }

        List<PostSearchBackOfficeRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build()).toList();

                    return PostSearchBackOfficeRes.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .postType(post.getPostType())
                            .tutor(post.getTutor())
                            .period(post.getTrack().getPeriod())
                            .isOpened(post.getIsOpened())
                            .uploadedAt(post.getUploadedAt())
                            .tagInfoList(tagInfoList)
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    // TODO : Redis 만들기
    public Page<PostSearchByTagRes> searchPostByTag(TrackNameEnum trackName, Integer period,
            Long tagId, User user, Pageable pageable) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track);

        List<Post> postList = postService.findPostListByTagIdWithTagList(tagId, track.getId());

        List<PostSearchByTagRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build()).collect(Collectors.toList());

                    return PostSearchByTagRes.builder()
                            .title(post.getTitle())
                            .tutor(post.getTutor())
                            .uploadedAt(post.getUploadedAt())
                            .postType(post.getPostType())
                            .tagList(tagInfoList)
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    @Transactional
    public PostCreateRes createPost(PostCreateReq request) {

        Track track = findTrackByTrackNameAndPeriod(request.trackName(), request.period());

        Post createPost = postService.createPost(request, track);

        if (request.contentLink() != null) {
            createContentByPost(createPost, request.content());
        }

        savePostTagByPostAndTagNameList(createPost, request.tagNameList());

        return PostCreateRes.builder().postId(createPost.getId()).build();
    }

    @Transactional
    public PostModifyRes updatePost(Long id, PostModifyReq request) {

        Track track = null;

        if (request.period() != null) {
            track = findTrackByTrackNameAndPeriod(request.trackName(), request.period());
        }

        Post updatePost = postService.updatePost(id, request, track);

        if (request.content() != null) {
            updateContent(updatePost, request.content());
        }

        if (request.tagNameList() != null) {
            updatePostTagByPostAndTagIdList(updatePost, request.tagNameList());
        }

        return PostModifyRes.builder()
                .postId(updatePost.getId())
                .build();
    }

    public PostGetSinglePostRes getPost(User user, Long postId, TrackNameEnum trackName,
            Integer period) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track);

        Post post = postService.findPostWithDetailByPostId(postId);

        List<TagInfo> tagList = post.getPostTagList().stream()
                .map(postTag -> {
                    return TagInfo.builder()
                            .tagId(postTag.getTag().getId())
                            .tagName(postTag.getTag().getTagName())
                            .build();
                }).toList();

        String detail = "";

        if (!post.getContentList().isEmpty()) {
            detail = post.getContentList().stream()
                    .map(Content::getDetail)
                    .collect(Collectors.joining());
        }

//        List<CommentRes> commentResList = commentService.findCommentResListByPostId(postId); TODO: 추후에 댓글 추가하며 구현할 예정

        return PostGetSinglePostRes.builder()
                .title(post.getTitle())
                .videoLink(post.getVideoLink())
                .detail(detail)
                .tagList(tagList)
//                .commentResList(commentResList) // TODO: 추후에 댓글 추가하며 구현할 예정
                .build();
    }

    public Page<PostGetCategoryPostRes> getPostListByCategory(
            User user, TrackNameEnum trackName, Integer period, PostTypeEnum postType,
            Pageable pageable) {

        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track);

        List<Post> postList = postService.findPostListByPostTypeAndTrackId(user.getUserRole(),
                postType, track);

        List<PostGetCategoryPostRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build()).toList();

                    return PostGetCategoryPostRes.builder()
                            .title(post.getTitle())
                            .tutor(post.getTutor())
                            .uploadedAt(post.getUploadedAt())
                            .tagList(tagInfoList)
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    private void createContentByPost(Post createPost, String content) {
        contentService.createContent(content, createPost);
    }

    private void savePostTagByPostAndTagNameList(Post post, List<String> tagNameList) {
        List<Tag> tagList = findTagIdListByTagNameList(tagNameList);
        postTagService.savePostTagByPostAndTagIdList(post, tagList);
    }

    private void updatePostTagByPostAndTagIdList(Post updatePost, List<String> tagNameList) {
        List<Tag> tagList = findTagIdListByTagNameList(tagNameList);
        postTagService.updatePostTagByPostAndTag(updatePost, tagList);
    }

    private void updateContent(Post modifyPost, String content) {
        contentService.updateContent(content, modifyPost);
    }

    private List<Tag> findTagIdListByTagNameList(List<String> tagNameList) {
        return tagService.findTagIdListByTagNameList(tagNameList);
    }

    private Track findTrackByTrackNameAndPeriod(TrackNameEnum trackName, Integer period) {
        return trackService.findTrackByTrackNameAndPeriod(trackName, period);
    }

    private List<Long> findPostIdInRedisByRedisIdUseTagAndTrack(Tag tag,
            Track userTrack) { //TODO: 추후에 성능 개선기로 레디스 캐싱 적용예정

        List<Long> postIdList;
//        postIdList = redisService.getPostIdListInRedis(tag.getTagName(), userTrack);

//        if(!postIdList.isEmpty()) {
//            return postIdList;
//        }

        postIdList = postTagService.findPostIdByTagIdAndIsDeletedFalse(tag.getId());

//        redisService.setPostIdListInRedis(tag.getTagName(), userTrack, postIdList);

        return postIdList;
    }

    // 유저가 속해있는 트랙의 열람권한을 체크하는 로직
    private void userCheckPermission(UserRoleEnum userRole, Track track) {
        if (userRoleIsUser(userRole) || track.getPeriod() != 0) {
            if (track.getIsPermission()) {
                throw new IllegalArgumentException(); // TODO: 추후 커스텀 에러로 변경할 예정
            }
        }
    }

    private boolean userTrackRoleIsApm(TrackRoleEnum trackRole) {
        return trackRole == TrackRoleEnum.APM;
    }

    private boolean userTrackRoleIsPm(TrackRoleEnum trackRole) {
        return trackRole == TrackRoleEnum.PM;
    }

    private boolean userRoleIsUser(UserRoleEnum userRole) {
        return userRole == UserRoleEnum.USER;
    }

    private void userRoleAndTrackCheck(User user, Track track) {
        List<Role> roleList = roleService.findAllByUserIdApprove(user.getId());
        Role role = null;

        for (Role r : roleList) {
            if (userTrackRoleIsPm(r.getTrackRole()) && track.getTrackName()
                    .equals(r.getTrack().getTrackName())) {
                return;
            }

            if (r.getTrack().equals(track)) {
                return;
            }

            role = r;
        }

        if (role == null) {
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
        }

        if (!Objects.equals(role.getTrack().getId(), track.getId())) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE_NOT_ACCESS);
        }
    }

}
