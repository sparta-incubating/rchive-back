package kr.sparta.rchive.domain.core.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.dto.response.PostSearchByTagRes;
import kr.sparta.rchive.domain.post.entity.EducationData;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.service.EducationDataService;
import kr.sparta.rchive.domain.post.service.EducationDataTagService;
import kr.sparta.rchive.domain.post.service.EducationDataTrackService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationDataTagCoreService {
    private final EducationDataService educationDataService;
    private final EducationDataTagService educationDataTagService;
    private final TrackService trackService;
    private final TagService tagService;
    private final UserService userService;
    private final RoleService roleService;
    private final EducationDataTrackService educationDataTrackService;

    public List<PostSearchByTagRes> searchPostByTag(String tagName, User user) {
        Long userTrackId = userService.findUserTrackId(user);   // 로그인한 유저의 트랙 ID 확인
        Track userTrack = trackService.findTrackById(userTrackId);

        UserCheckPermission(user.getUserRole(), userTrackId);   // 로그인한 유저의 권한과 트랙 ID를 통해 열람 권한 확인

        Tag tag = tagService.findTagBytagName(tagName); // 검색하려는 태그 찾아오는 로직

        // 검색하려는 태그가 달려있는 교육자료 ID를 리스트로 찾아오는 로직
        List<Long> educationDataIdList = educationDataTagService.findEducationDataIdByTagId(tag.getId());

        // 교육자료 ID 리스트 중 로그인한 User의 Track에 해당하는 자료만 가져오는 로직
        educationDataIdList = FilterEducationDataByTrackId(educationDataIdList, userTrack, userTrackId, user.getId());

        // 위에서 필터링 된 로직 중 권한이 User일 경우 삭제된 자료를 필터링 하는 로직
        educationDataIdList = FilterEducationDataByIsDeleted(educationDataIdList, user.getUserRole());

        // 필터링이 끝난 EducationDataId를 키로 갖고 educationData에 달려있는 Tag들의 List를 value로 갖는 Map으로 변경하는 로직
        Map<Long, List<Long>> educationDataTagMap = educationDataTagService.findEducationDataTagListByTagId(educationDataIdList);


        // 위에서 Map으로 변경시켜놓은 EducationDataId / TagIdList를 실제 데이터들로 바꿔 responseList에 담는 로직
        return educationDataTagMap.entrySet().stream()
                .map(response -> {
                    Long educationDataId = response.getKey();
                    List<Long> tagIdList = response.getValue();

                    EducationData educationData = educationDataService.findEducationDataById(educationDataId);

                    List<Tag> tagList = tagIdList.stream()
                            .map(tagService::findTagById).collect(Collectors.toList());

                    return PostSearchByTagRes.builder().title(educationData.getTitle()).tutor(educationData.getTutor())
                            .uploadedAt(educationData.getUploadedAt()).tagList(tagList).build();
                }).toList();
    }

    // 유저가 속해있는 트랙의 열람권한을 체크하는 로직
    public void UserCheckPermission(UserRoleEnum userRole, Long userTrackId) {
        if (UserRoleIsUser(userRole)) {
            if (!trackService.checkPermission(userTrackId)) {
                throw new IllegalArgumentException(); // 추후 커스텀 에러로 변경할 예정
            }
        }
    }

    // 로그인 한 유저가 속해있는 트랙에 따라 필터링을 진행하는 로직
    private List<Long> FilterEducationDataByTrackId(List<Long> educationDataIdList, Track userTrack, Long userTrackId, Long userId) {
        TrackRoleEnum trackRole = roleService.findTrackRoleByTrackIdAndUserId(userTrackId, userId);

        if(UserTrackRoleIsPM(trackRole)) {
            return educationDataTrackService.filterEducationDataIdListBytrackId(educationDataIdList, userTrackId);
        }

        return educationDataTrackService.filterEducationDataIdListByTrackName(educationDataIdList, userTrack);
    }


    // 자료의 삭제 여부에 따라 필터링을 진행하는 로직
    private List<Long> FilterEducationDataByIsDeleted(List<Long> educationDataIdList, UserRoleEnum userRole) {
        // UserRole이 User가 아니라면 educationDataIdList를 그대로 반환
        if(!UserRoleIsUser(userRole)) {
            return educationDataIdList;
        }

        return educationDataService.filterEducationDataIdByIsDeleted(educationDataIdList);
    }

    public boolean UserTrackRoleIsPM(TrackRoleEnum trackRole) {
        return trackRole == TrackRoleEnum.PM;
    }

    public boolean UserRoleIsUser(UserRoleEnum userRole) {
        return userRole == UserRoleEnum.USER;
    }
}
