package kr.sparta.rchive.domain.core.service;

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
import java.util.Map;
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
            User user, TrackNameEnum trackName, Integer period, PostTypeEnum postType, LocalDate startDate, LocalDate endDate,
            Integer searchPeriod, Boolean isOpened, Pageable pageable
    ) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        userRoleAndTrackCheck(user, track);

        List<Post> postList;

        if(postType == null) {
            postList = postService.findPostListInBackOfficePostTypeAll(track, startDate, endDate, searchPeriod, isOpened);
        } else {
            postList = postService.findPostListInBackOffice(track, postType, startDate, endDate, searchPeriod, isOpened);
        }

        List<PostSearchBackOfficeRes> responseList = postList.stream()
            .map(post -> {
                List<Tag> tagList = post.getPostTagList().stream().map(
                    postTag -> postTag.getTag()
                ).toList();

                List<TagInfo> tagInfoList = tagList.stream().map(
                    tag -> {
                        return TagInfo.builder()
                            .tagId(tag.getId())
                            .tagName(tag.getTagName())
                            .build();
                    }
                ).toList();

                return PostSearchBackOfficeRes.builder()
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

    // TODO : Redis 만들기, paging 적용하기
    public Page<PostSearchByTagRes> searchPostByTag(String tagName, User user, Pageable pageable) {
        Long trackId = userService.findUserTrackIdByUserEmail(user.getEmail());   // TODO: 로그인한 유저의 트랙 ID 확인 추후에 레디스와 연결하여
        // 마지막에 들어간 트랙 받아오는 로직으로 변경
        Track userTrack = trackService.findTrackById(trackId);

        UserCheckPermission(user.getUserRole(), trackId);   // 로그인한 유저의 권한과 트랙 ID를 통해 열람 권한 확인

        String lowerCaseTagName = tagName.toLowerCase();

        Tag tag = tagService.findTagBytagName(lowerCaseTagName); // 검색하려는 태그 찾아오는 로직

        // 검색하려는 태그가 달려있는 교육자료 ID를 리스트로 찾아오는 로직
        List<Long> postIdList = findPostIdInRedisByRedisIdUseTagAndTrack(tag, userTrack);

        // 교육자료 ID 리스트 중 로그인한 User의 Track에 해당하는 자료만 가져오는 로직
        postIdList = filterPostByTrackId(postIdList, userTrack, user.getId());

        // 필터링이 끝난 EducationDataId를 키로 갖고 educationData에 달려있는 Tag들의 List를 value로 갖는 Map으로 변경하는 로직
        Map<Long, List<Long>> postTagMap = postTagService.findPostTagListByTagId(postIdList);

        // 위에서 Map으로 변경시켜놓은 EducationDataId / TagIdList를 실제 데이터들로 바꿔 responseList에 담는 로직
        List<PostSearchByTagRes> responseList = postTagMap.entrySet().stream()
                .map(response -> {
                    Long postId = response.getKey();
                    List<Long> tagIdList = response.getValue();

                    PostSearchInfo postSearchInfo = getPostDetails(postId, tagIdList);


                    return PostSearchByTagRes.builder()
                            .title(postSearchInfo.post().getTitle())
                            .tutor(postSearchInfo.post().getTutor())
                            .uploadedAt(postSearchInfo.post().getUploadedAt())
                            .postType(postSearchInfo.post().getPostType())
                            .tagNameList(postSearchInfo.tagNameList())
                            .build();
                }).toList();

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

    public PostGetSinglePostRes getPost(User user, Long postId, TrackNameEnum trackName, Integer period) {
        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);

//        Post post = postService.findTest(postId);
//
//        List<String> contentList = post.getContentList().stream()
//                .map(content -> content.getContent())
//                .toList();
//
//        List<String> tagName = post.getPostTagList().stream()
//                .map(postTag -> postTag.getTag().getTagName())
//                .collect(Collectors.toList());

        Post post = postService.findPostById(postId);

        userRoleAndTrackCheck(user, track);

        String content = findContentByPostId(postId);

        List<Long> tagIdList = postTagService.findTagIdListByPostId(post.getId());
        List<String> tagNameList = tagService.findTagNameListBytagIdList(tagIdList);

//        List<CommentRes> commentResList = commentService.findCommentResListByPostId(postId); TODO: 추후에 댓글 추가하며 구현할 예정

        return PostGetSinglePostRes.builder()
                .title(post.getTitle())
                .videoLink(post.getVideoLink())
                .content(content)
                .tagList(tagNameList)
//                .commentResList(commentResList) // TODO: 추후에 댓글 추가하며 구현할 예정
                .build();
    }

    public List<PostGetCategoryPostRes> getPostListByCategory(
            User user, TrackNameEnum trackName, Integer period, PostTypeEnum postType
    ) {

        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        userRoleAndTrackCheck(user, track);

        List<Long> postIdList = postService.findPostIdListByPostTypeAndTrackId(postType, track.getId());

        Map<Long, List<Long>> postTagMap = postTagService.findPostTagListByTagId(postIdList);

        return postTagMap.entrySet().stream()
                .map(response -> {
                    Long postId = response.getKey();
                    List<Long> tagIdList = response.getValue();

                    PostSearchInfo postSearchInfo = getPostDetails(postId, tagIdList);

                    return PostGetCategoryPostRes.builder()
                            .title(postSearchInfo.post().getTitle())
                            .tutor(postSearchInfo.post().getTutor())
                            .uploadedAt(postSearchInfo.post().getUploadedAt())
                            .tagNameList(postSearchInfo.tagNameList())
                            .build();
                }).toList();
    }

    private PostSearchInfo getPostDetails(Long postId, List<Long> tagIdList) {
        Post post = postService.findPostById(postId);

        List<String> tagNameList = tagIdList.stream()
                .map(tagService::findTagById)
                .map(Tag::getTagName)
                .collect(Collectors.toList());

        return new PostSearchInfo(post, tagNameList);
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

    private List<Long> findPostIdInRedisByRedisIdUseTagAndTrack(Tag tag, Track userTrack) { //TODO: 추후에 성능 개선기로 레디스 캐싱 적용예정

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
    private void UserCheckPermission(UserRoleEnum userRole, Long trackId) {
        if (UserRoleIsUser(userRole)) {
            if (!trackService.checkPermission(trackId)) {
                throw new IllegalArgumentException(); // TODO: 추후 커스텀 에러로 변경할 예정
            }
        }
    }

    // 로그인 한 유저가 속해있는 트랙의 데이터만 남기는 로직
    private List<Long> filterPostByTrackId(List<Long> postIdList, Track userTrack, Long userId) {
        TrackRoleEnum trackRole = roleService.findTrackRoleByTrackIdAndUserId(userTrack.getId(), userId);

        if (UserTrackRoleIsPM(trackRole)) {
            return postService.filterPostIdListByTrackName(postIdList, userTrack.getTrackName());
        }

        return postService.filterPostIdListByTrackId(postIdList, userTrack.getId());
    }

    private boolean UserTrackRoleIsPM(TrackRoleEnum trackRole) {
        return trackRole == TrackRoleEnum.PM;
    }

    private boolean UserRoleIsUser(UserRoleEnum userRole) {
        return userRole == UserRoleEnum.USER;
    }

    private String findContentByPostId(Long postId) {
        StringBuilder sb = new StringBuilder();

        List<String> contentList = contentService.findContentByPostId(postId).stream()
                .filter(content -> content.getId() == 1)
                .map(Content::getContent)
                .toList();

        for (String s : contentList) {
            sb.append(s);
        }

        return sb.toString();
    }

    private void userRoleAndTrackCheck(User user, Track track) {
        List<Role> roleList = roleService.findAllByUserIdApprove(user.getId());
        Role role = null;

        for (Role r : roleList) {
            if (r.getTrackRole().equals(TrackRoleEnum.PM)) {
                return;
            }

            role = r;
        }

        if (role == null) {
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
        }

        if (!Objects.equals(role.getTrack().getId(), track.getId())) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE_NOT_ACCESS); //TODO: 추후에 커스텀 에러 위치 정할 예정

        }
    }


}
