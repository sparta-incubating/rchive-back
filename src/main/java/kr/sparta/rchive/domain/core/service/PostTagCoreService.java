package kr.sparta.rchive.domain.core.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.dto.response.PostSearchByTagRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.PostTagService;
import kr.sparta.rchive.domain.post.service.PostTrackService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostTagCoreService {
    private final PostService postService;
    private final PostTagService postTagService;
    private final TrackService trackService;
    private final TagService tagService;
    private final UserService userService;
    private final RoleService roleService;
    private final PostTrackService postTrackService;
    private final RedisService redisService;

    // TODO : Redis 만들기, paging 적용하기
    public Page<PostSearchByTagRes> searchPostByTag(String tagName, User user, Pageable pageable) {
        Long trackId = userService.findUserTrackIdByUserEmail(user.getEmail());   // TODO: 로그인한 유저의 트랙 ID 확인 추후에 레디스와 연결하여
                                                            // 마지막에 들어간 트랙 받아오는 로직으로 변경
        Track userTrack = trackService.findTrackById(trackId);

        UserCheckPermission(user.getUserRole(), trackId);   // 로그인한 유저의 권한과 트랙 ID를 통해 열람 권한 확인

        Tag tag = tagService.findTagBytagName(tagName); // 검색하려는 태그 찾아오는 로직

        // 검색하려는 태그가 달려있는 교육자료 ID를 리스트로 찾아오는 로직
        List<Long> postIdList = findPostIdInRedisByRedisIdUseTagAndTrack(tag, userTrack);

        // 교육자료 ID 리스트 중 로그인한 User의 Track에 해당하는 자료만 가져오는 로직
        postIdList = filterPostByTrackId(postIdList, userTrack, trackId, user.getId());

        // 필터링이 끝난 EducationDataId를 키로 갖고 educationData에 달려있는 Tag들의 List를 value로 갖는 Map으로 변경하는 로직
        Map<Long, List<Long>> postTagMap = postTagService.findPostTagListByTagId(postIdList);

        // 위에서 Map으로 변경시켜놓은 EducationDataId / TagIdList를 실제 데이터들로 바꿔 responseList에 담는 로직
        List<PostSearchByTagRes> responseList = postTagMap.entrySet().stream()
                .map(response -> {
                    Long postId = response.getKey();
                    List<Long> tagIdList = response.getValue();

                    Post post = postService.findPostById(postId);

                    List<Tag> tagList = tagIdList.stream()
                            .map(tagService::findTagById).collect(Collectors.toList());

                    return PostSearchByTagRes.builder().title(post.getTitle()).tutor(post.getTutor())
                            .uploadedAt(post.getUploadedAt()).tagList(tagList).build();
                }).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    private List<Long> findPostIdInRedisByRedisIdUseTagAndTrack(Tag tag, Track userTrack) {

        String redisTagId = redisService.redisKeyPostIdListByTagNameAndTrack(tag.getTagName(), userTrack);

        if(redisService.redisKeyExist(redisTagId)) {
            return redisService.getListInRedisTypeLong(redisTagId);
        }

        List<Long> postIdList = postTagService.findPostIdByTagIdAndIsDeletedFalse(tag.getId());

        redisService.setListInRedisTypeLong(redisTagId, postIdList);

        return postIdList;
    }

    // 유저가 속해있는 트랙의 열람권한을 체크하는 로직
    public void UserCheckPermission(UserRoleEnum userRole, Long trackId) {
        if (UserRoleIsUser(userRole)) {
            if (!trackService.checkPermission(trackId)) {
                throw new IllegalArgumentException(); // TODO: 추후 커스텀 에러로 변경할 예정
            }
        }
    }

    // 로그인 한 유저가 속해있는 트랙의 데이터만 남기는 로직
    private List<Long> filterPostByTrackId(List<Long> postIdList, Track userTrack, Long userTrackId, Long userId) {
        TrackRoleEnum trackRole = roleService.findTrackRoleByTrackIdAndUserId(userTrackId, userId);

        if(UserTrackRoleIsPM(trackRole)) {
            return postTrackService.filterPostIdListByTrackName(postIdList, userTrack.getTrackName());
        }

        return postTrackService.filterPostIdListByTrackId(postIdList, userTrackId);
    }

    public boolean UserTrackRoleIsPM(TrackRoleEnum trackRole) {
        return trackRole == TrackRoleEnum.PM;
    }

    public boolean UserRoleIsUser(UserRoleEnum userRole) {
        return userRole == UserRoleEnum.USER;
    }
}