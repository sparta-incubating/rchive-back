package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.bookmark.service.BookmarkService;
import kr.sparta.rchive.domain.post.dto.PostTrackInfo;
import kr.sparta.rchive.domain.post.dto.PostTypeInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.request.DeleteThumbnailReq;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.dto.request.RecentSearchKeywordReq;
import kr.sparta.rchive.domain.post.dto.response.*;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.PostTagService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.post.service.TutorService;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.exception.TrackCustomException;
import kr.sparta.rchive.domain.user.exception.TrackExceptionCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
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
    private final RoleService roleService;
    private final BookmarkService bookmarkService;
    private final RedisService redisService;
    private final TutorService tutorService;


    public Page<PostSearchBackOfficeRes> getPostListInBackOffice(
            User user, TrackNameEnum trackName, Integer period, String title, PostTypeEnum postType, LocalDate startDate,
            LocalDate endDate, Integer searchPeriod, Boolean isOpened, Long tutorId, Pageable pageable
    ) {
        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
        }

        List<Post> postList;

        if (postType == null) {
            postList = postService.findPostListInBackOfficePostTypeAll(managerTrack, title, startDate, endDate,
                    searchPeriod, tutorId, isOpened);
        } else {
            postList = postService.findPostListInBackOffice(managerTrack, postType, title, startDate, endDate,
                    searchPeriod, tutorId, isOpened);
        }

        List<PostSearchBackOfficeRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build())
                            .sorted(Comparator.comparing(TagInfo::tagName))
                            .toList();

                    return PostSearchBackOfficeRes.builder()
                            .postId(post.getId())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .title(post.getTitle())
                            .postType(PostTypeInfo.of(post.getPostType()))
                            .tutor(post.getTutor().getTutorName())
                            .contentLink(post.getContentLink())
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
    public Page<PostGetRes> searchPostByTag(TrackNameEnum trackName, Integer period, Long tagId, User user,
                                            PostTypeEnum postType, Pageable pageable) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        Role role = userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track, role.getTrackRole());

        List<Post> postList = postService.findPostListByTagIdWithTagList(tagId, track.getId(), postType);

        List<Long> bookmarkedPostIdList = bookmarkService.findPostIdListByUserId(user.getId());

        List<PostGetRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build())
                            .sorted(Comparator.comparing(TagInfo::tagName))
                            .collect(Collectors.toList());

                    return PostGetRes.builder()
                            .postId(post.getId())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .postType(PostTypeInfo.of(post.getPostType()))
                            .title(post.getTitle())
                            .tutor(post.getTutor().getTutorName())
                            .uploadedAt(post.getUploadedAt())
                            .tagList(tagInfoList)
                            .isBookmarked(bookmarkedPostIdList.contains(post.getId()))
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    @Transactional
    public PostCreateRes createPost(User user, TrackNameEnum trackName, Integer period, PostCreateReq request) {

        String checkTitle = request.title().strip();

        if(checkTitle.isEmpty()) {
            throw new PostCustomException(PostExceptionCode.BAD_REQUEST_TITLE_EMPTY);
        }

        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, request.postPeriod());

        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
        }

        Tutor tutor = tutorService.checkTutor(request.tutorId(), managerTrack);

        Post createPost = postService.createPost(request, managerTrack, tutor);

        if (request.tagNameList() != null) {
            savePostTagByPostAndTagNameList(createPost, request.tagNameList());
        }

        return PostCreateRes.builder().postId(createPost.getId()).build();
    }

    @Transactional
    public PostModifyRes updatePost(User user, TrackNameEnum trackName, Integer period, Long postId,
                                    PostUpdateReq request) {

        String checkTitle = request.title().strip();

        if(checkTitle.isEmpty()) {
            throw new PostCustomException(PostExceptionCode.BAD_REQUEST_TITLE_EMPTY);
        }

        PostTrackInfo postTrackInfo = checkPostAndTrack(user, trackName, period, postId);
        Post findPost = postTrackInfo.post();
        Track managerTrack = postTrackInfo.track();

        if (request.updatePeriod() != null) {
            managerTrack = findTrackByTrackNameAndPeriod(trackName, request.updatePeriod());
        } else if (managerTrack.getPeriod() == 0) {
            managerTrack = findPost.getTrack();
        }

        Tutor tutor = null;

        if (request.tutorId() != null) {
            tutor = tutorService.checkTutor(request.tutorId(), managerTrack);
        }

        Post updatePost = postService.updatePost(findPost, request, managerTrack, tutor);

        if (request.tagNameList() != null) {
            updatePostTagByPostAndTagIdList(updatePost, request.tagNameList());
        }

        return PostModifyRes.builder()
                .postId(updatePost.getId())
                .build();
    }

    @Transactional
    public void deletePost(User user, TrackNameEnum trackName, Integer period, Long postId) {

        PostTrackInfo postTrackInfo = checkPostAndTrack(user, trackName, period, postId);

        postService.deletePost(postTrackInfo.post());
    }

    public PostGetSinglePostRes getPost(User user, Long postId, TrackNameEnum trackName,
                                        Integer period) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        Role role = userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track, role.getTrackRole());

        Post post = postService.findPostWithDetailByPostId(postId);
        Boolean isBookmarked = bookmarkService.existsBookmarkByUserIdAndPostId(user.getId(), post.getId());

        List<TagInfo> tagList = post.getPostTagList().stream()
                .map(postTag -> TagInfo.builder()
                        .tagId(postTag.getTag().getId())
                        .tagName(postTag.getTag().getTagName())
                        .build())
                .sorted(Comparator.comparing(TagInfo::tagName))
                .toList();

        return PostGetSinglePostRes.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .tutor(post.getTutor().getTutorName())
                .postType(PostTypeInfo.of(post.getPostType()))
                .videoLink(post.getVideoLink())
                .contentLink(post.getContentLink())
                .uploadedAt(post.getUploadedAt())
                .tagList(tagList)
                .isBookmarked(isBookmarked)
                .build();
    }

    public Page<PostGetRes> getPostListByCategory(
            User user, TrackNameEnum trackName, Integer period, PostTypeEnum postType, Long tutorId, Pageable pageable) {

        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        Role role = userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track, role.getTrackRole());

        List<Post> postList = postService.findPostListByPostTypeAndTrackId(postType, track, tutorId);

        List<Long> bookmarkedPostIdList = bookmarkService.findPostIdListByUserId(user.getId());

        List<PostGetRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build())
                            .sorted(Comparator.comparing(TagInfo::tagName))
                            .toList();

                    return PostGetRes.builder()
                            .postId(post.getId())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .postType(PostTypeInfo.of(post.getPostType()))
                            .title(post.getTitle())
                            .tutor(post.getTutor().getTutorName())
                            .uploadedAt(post.getUploadedAt())
                            .tagList(tagInfoList)
                            .isBookmarked(bookmarkedPostIdList.contains(post.getId()))
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    @Transactional
    public void openPost(User user, TrackNameEnum trackName, Integer period, List<Long> postIdList) {
        List<Post> postList = checkPostListAndTrack(user, trackName, period, postIdList);
        postService.openPost(postList);
    }

    @Transactional
    public void closePost(User user, TrackNameEnum trackName, Integer period, List<Long> postIdList) {
        List<Post> postList = checkPostListAndTrack(user, trackName, period, postIdList);
        postService.closePost(postList);
    }

    public List<TutorRes> searchTutor(User user, TrackNameEnum trackName, Integer period,
                                      Integer inputPeriod, String tutorName) {
        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, inputPeriod);
        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            if (!roleService.checkIsPm(user.getId(), trackName)) {
                roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
            }
        }

        return tutorService.findTutorListByTutorNameAndTrackId(tutorName, managerTrack.getId());
    }

    public Page<PostGetRes> searchPosts(User user, PostTypeEnum postType, TrackNameEnum trackName, Integer period,
                                        String keyword, Long tutorId, Pageable pageable) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        Role role = userRoleAndTrackCheck(user, track);
        userCheckPermission(user.getUserRole(), track, role.getTrackRole());

        keyword = keyword.toLowerCase().replace(" ", "");

        List<Post> postList = postService.searchPost(postType, keyword, tutorId, track.getId());

        List<Long> bookmarkedPostIdList = bookmarkService.findPostIdListByUserId(user.getId());

        List<PostGetRes> responseList = postList.stream()
                .map(post -> {
                    List<TagInfo> tagInfoList = post.getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build())
                            .sorted(Comparator.comparing(TagInfo::tagName))
                            .toList();

                    return PostGetRes.builder()
                            .postId(post.getId())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .postType(PostTypeInfo.of(post.getPostType()))
                            .title(post.getTitle())
                            .tutor(post.getTutor().getTutorName())
                            .uploadedAt(post.getUploadedAt())
                            .tagList(tagInfoList)
                            .isBookmarked(bookmarkedPostIdList.contains(post.getId()))
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    private List<Post> checkPostListAndTrack(User user, TrackNameEnum trackName, Integer period, List<Long> postIdList) {
        List<Post> findPostList = postService.findPostListByPostIdList(postIdList);
        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
        }

        return findPostList.stream()
                .filter(p -> p.getTrack().getTrackName().equals(managerTrack.getTrackName()))
                .filter(p -> managerTrack.getPeriod() == 0 || Objects.equals(p.getTrack().getPeriod(), managerTrack.getPeriod()))
                .collect(Collectors.toList());
    }

    private PostTrackInfo checkPostAndTrack(User user, TrackNameEnum trackName, Integer period,
                                            Long postId) {
        Post findPost = postService.findPostById(postId);
        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
        }

        // 게시물이 해당 트랙에 해당하는지 체크하는 로직
        if (findPost.getTrack().getTrackName() != managerTrack.getTrackName()) {
            throw new TrackCustomException(TrackExceptionCode.FORBIDDEN_TRACK);
        }

        if (managerTrack.getPeriod() != 0 && !Objects.equals(findPost.getTrack().getPeriod(), managerTrack.getPeriod())) {
            throw new TrackCustomException(TrackExceptionCode.FORBIDDEN_TRACK);
        }

        return PostTrackInfo.builder()
                .post(findPost)
                .track(managerTrack)
                .build();
    }

    private void savePostTagByPostAndTagNameList(Post post, List<String> tagNameList) {
        List<Tag> tagList = findTagListByTagNameList(tagNameList);
        postTagService.savePostTagByPostAndTagList(post, tagList);
    }

    private void updatePostTagByPostAndTagIdList(Post updatePost, List<String> tagNameList) {
        List<Tag> tagList = findTagListByTagNameList(tagNameList);
        postTagService.updatePostTagByPostAndTag(updatePost, tagList);
    }

    private List<Tag> findTagListByTagNameList(List<String> tagNameList) {
        return tagService.findTagIdListByTagNameList(tagNameList);
    }

    private Track findTrackByTrackNameAndPeriod(TrackNameEnum trackName, Integer period) {
        return trackService.findTrackByTrackNameAndPeriod(trackName, period);
    }

//    private List<Long> findPostIdInRedisByRedisIdUseTagAndTrack(Tag tag,
//                                                                Track userTrack) { //TODO: 추후에 성능 개선기로 레디스 캐싱 적용예정
//
//        List<Long> postIdList;
//        postIdList = redisService.getPostIdListInRedis(tag.getTagName(), userTrack);
//
//        if(!postIdList.isEmpty()) {
//            return postIdList;
//        }
//
//        postIdList = postTagService.findPostIdByTagIdAndIsDeletedFalse(tag.getId());
//
//        redisService.setPostIdListInRedis(tag.getTagName(), userTrack, postIdList);
//
//        return postIdList;
//    }

    // 유저가 속해있는 트랙의 열람권한을 체크하는 로직
    private void userCheckPermission(UserRoleEnum userRole, Track track, TrackRoleEnum trackRole) {
        if (userRoleIsUser(userRole) || userTrackRoleIsApm(trackRole)) {
            if (!track.getIsPermission()) {
                throw new TrackCustomException(TrackExceptionCode.FORBIDDEN_TRACK_NOT_PERMISSION);
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

    private Role userRoleAndTrackCheck(User user, Track track) {
        List<Role> roleList = roleService.findRoleListByUserIdAuthApprove(user.getId());

        if (roleList.isEmpty()) {
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
        }

        for (Role r : roleList) {
            if (userTrackRoleIsPm(r.getTrackRole()) &&
                    track.getTrackName().equals(r.getTrack().getTrackName())) {
                return r;
            }

            if (r.getTrack().equals(track)) {
                return r;
            }
        }

        throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
    }

    public void saveRecentSearchKeyword(User user, RecentSearchKeywordReq request) {
        Track track = trackService.findTrackByTrackNameAndPeriod(request.trackName(), request.period());

        redisService.saveRecentSearchKeyword(user.getId(), track.getId(), request.keyword());
    }

    public List<PostGetRecentKeywordRes> getRecentSearchKeyword(User user, TrackNameEnum trackName, Integer period) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        List<String> keywordList = redisService.getRecentSearchKeyword(user.getId(), track.getId());

        if (keywordList.isEmpty()) {
            return null;
        }

        return keywordList.stream()
                .map(
                        keyword -> PostGetRecentKeywordRes.builder()
                                .keyword(keyword)
                                .build()
                ).collect(Collectors.toList());
    }

    public void deleteRecentSearchKeyword(User user, RecentSearchKeywordReq request) {
        Track track = trackService.findTrackByTrackNameAndPeriod(request.trackName(), request.period());

        redisService.deleteSearchKeyword(user.getId(), track.getId(), request.keyword());
    }

    public PostModifyPreviewRes getPostDetailInBackOffice(User user, TrackNameEnum trackName, Integer period, Long postId) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        if (period == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), track.getId());
        }

        Post post = postService.findPostDetail(postId);

        if (post.getTrack().getTrackName() != track.getTrackName()) {
            throw new TrackCustomException(TrackExceptionCode.FORBIDDEN_TRACK);
        }

        if (track.getPeriod() != 0 && !Objects.equals(post.getTrack().getPeriod(), track.getPeriod())) {
            throw new TrackCustomException(TrackExceptionCode.FORBIDDEN_TRACK);
        }

        List<String> tagNameList = post.getPostTagList().stream()
                .map(
                        postTag -> postTag.getTag().getTagName()
                ).toList();

        TutorRes tutorRes = TutorRes.builder()
                .tutorId(post.getTutor().getId())
                .tutorName(post.getTutor().getTutorName())
                .build();

        return PostModifyPreviewRes.builder()
                .thumbnailUrl(post.getThumbnailUrl())
                .title(post.getTitle())
                .contentLink(post.getContentLink())
                .videoLink(post.getVideoLink())
                .period(post.getTrack().getPeriod())
                .postType(PostTypeInfo.of(post.getPostType()))
                .tutorRes(tutorRes)
                .tagNameList(tagNameList)
                .uploadedAt(post.getUploadedAt())
                .isOpened(post.getIsOpened())
                .build();
    }

    @Transactional
    public void deleteThumbnail(User user, Long postId, DeleteThumbnailReq request) {
        PostTrackInfo postTrackInfo = checkPostAndTrack(user, request.trackName(), request.period(), postId);

        postService.deleteThumbnail(postTrackInfo.post());
    }
}
