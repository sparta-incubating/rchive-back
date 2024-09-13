package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.bookmark.service.BookmarkService;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.dto.response.PostSearchBackOfficeRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.PostTagService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.post.service.TutorService;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.TrackCustomException;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.global.redis.RedisService;
import kr.sparta.rchive.test.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PostTagCoreServiceTest implements UserTest, PostTest, TrackTest, TutorTest, TagTest, PostTagTest, RoleTest {

    @InjectMocks
    private PostTagCoreService postTagCoreService;

    @Mock
    private PostService postService;
    @Mock
    private PostTagService postTagService;
    @Mock
    private TrackService trackService;
    @Mock
    private TagService tagService;
    @Mock
    private RoleService roleService;
    @Mock
    private BookmarkService bookmarkService;
    @Mock
    private RedisService redisService;
    @Mock
    private TutorService tutorService;

    @Test
    @DisplayName("백오피스에서 PM이 PostType은 null이고 게시물 리스트를 조회해오는 코어 서비스 로직 성공 테스트")
    void 백오피스_PM_PostType_null_게시물_리스트_조회_성공_테스트() {
        // Given
        User user = TEST_PM_USER;
        Track managerTrack = TEST_TRACK_ANDROID_PM;
        List<Post> postList = List.of(TEST_POST_1L, TEST_POST_2L);

        ReflectionTestUtils.setField(user, "id", 1L);
        Pageable pageable = PageRequest.of(0, 10);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(managerTrack);
        given(postService.findPostListInBackOfficePostTypeAll(any(Track.class), any(String.class), any(LocalDate.class),
                any(LocalDate.class), any(Integer.class), any(Long.class), any(Boolean.class))).willReturn(postList);
        // When
        Page<PostSearchBackOfficeRes> result = postTagCoreService.getPostListInBackOffice(user, TEST_TRACK_NAME, TEST_TRACK_PM_PERIOD,
                TEST_POST_TITLE, null, LocalDate.now(), LocalDate.now(), TEST_TRACK_1L_PERIOD, true, TEST_TUTOR_ID, pageable);
        // Then
        assertThat(result.getContent().size()).isEqualTo(postList.size());
        assertThat(result.getContent().get(0).title()).isEqualTo(postList.get(0).getTitle());
    }

    @Test
    @DisplayName("백오피스에서 PM이 특정 PostType의 게시물 리스트를 조회해오는 코어 서비스 로직 성공 테스트")
    void 백오피스_PM_특정_PostType_게시물_리스트_조회_성공_테스트() {
        // Given
        User user = TEST_PM_USER;
        Track managerTrack = TEST_TRACK_ANDROID_PM;
        List<Post> postList = List.of(TEST_POST_1L, TEST_POST_2L);

        ReflectionTestUtils.setField(user, "id", 1L);
        Pageable pageable = PageRequest.of(0, 10);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(managerTrack);
        given(postService.findPostListInBackOffice(any(Track.class), any(PostTypeEnum.class), any(String.class), any(LocalDate.class),
                any(LocalDate.class), any(Integer.class), any(Long.class), any(Boolean.class))).willReturn(postList);
        // When
        Page<PostSearchBackOfficeRes> result = postTagCoreService.getPostListInBackOffice(user, TEST_TRACK_NAME, TEST_TRACK_PM_PERIOD,
                TEST_POST_TITLE, TEST_POST_TYPE, LocalDate.now(), LocalDate.now(), TEST_TRACK_1L_PERIOD, true, TEST_TUTOR_ID, pageable);
        // Then
        assertThat(result.getContent().size()).isEqualTo(postList.size());
        assertThat(result.getContent().get(0).title()).isEqualTo(postList.get(0).getTitle());
    }

    @Test
    @DisplayName("백오피스에서 APM이 특정 PostType의 게시물 리스트를 조회해오는 코어 서비스 로직 성공 테스트")
    void 백오피스_APM_특정_PostType_게시물_리스트_조회_성공_테스트() {
        // Given
        User user = TEST_PM_USER;
        Track managerTrack = TEST_TRACK_ANDROID_PM;
        List<PostTag> postTagList = List.of(TEST_POST_TAG_1, TEST_POST_TAG_2);

        Post testPost = Post.builder()
                .postType(TEST_POST_TYPE)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(TEST_TUTOR)
                .track(TEST_TRACK_ANDROID_1L)
                .uploadedAt(LocalDate.now())
                .postTagList(postTagList)
                .build();

        List<Post> postList = List.of(testPost);
        ReflectionTestUtils.setField(user, "id", 1L);
        Pageable pageable = PageRequest.of(0, 10);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(managerTrack);
        given(postService.findPostListInBackOffice(any(Track.class), any(PostTypeEnum.class), any(String.class), any(LocalDate.class),
                any(LocalDate.class), any(Integer.class), any(Long.class), any(Boolean.class))).willReturn(postList);
        // When

        Page<PostSearchBackOfficeRes> result = postTagCoreService.getPostListInBackOffice(user, TEST_TRACK_NAME, TEST_TRACK_1L_PERIOD,
                TEST_POST_TITLE, TEST_POST_TYPE, LocalDate.now(), LocalDate.now(), TEST_TRACK_1L_PERIOD, true, TEST_TUTOR_ID, pageable);
        // Then
        assertThat(result.getContent().size()).isEqualTo(postList.size());
        assertThat(result.getContent().get(0).title()).isEqualTo(postList.get(0).getTitle());
    }

    @Test
    @DisplayName("유저가 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 성공 테스트")
    void 유저_태그로_게시물_리스트_검색_성공_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        Track track = TEST_TRACK_ANDROID_1L;
        Role role = TEST_STUDENT_ROLE;
        List<Role> roleList = List.of(role);
        List<PostTag> postTagList = List.of(TEST_POST_TAG_1, TEST_POST_TAG_2);
        List<Long> bookmarkedPostIdList = List.of(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);

        Post testPost = Post.builder()
                .postType(TEST_POST_TYPE)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(TEST_TUTOR)
                .track(TEST_TRACK_ANDROID_1L)
                .uploadedAt(LocalDate.now())
                .postTagList(postTagList)
                .build();

        List<Post> postList = List.of(testPost);
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(track, "id", 1L);
        ReflectionTestUtils.setField(testPost, "id", 1L);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(track);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(roleList);
        given(postService.findPostListByTagIdWithTagList(any(Long.class), any(Long.class), any(PostTypeEnum.class))).willReturn(postList);
        given(bookmarkService.findPostIdListByUserId(any(Long.class))).willReturn(bookmarkedPostIdList);
        // When
        Page<PostGetRes> result = postTagCoreService.searchPostByTag(TEST_TRACK_NAME, TEST_TRACK_1L_PERIOD, TEST_TAG_1L_ID,
                user, TEST_POST_TYPE, pageable);

        // Then
        assertThat(result.getContent().size()).isEqualTo(postList.size());
        assertThat(result.getContent().get(0).title()).isEqualTo(postList.get(0).getTitle());
    }

    @Test
    @DisplayName("PM이 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 성공 테스트")
    void PM_태그로_게시물_리스트_검색_성공_테스트() {
        // Given
        User user = TEST_PM_USER;
        Track track = TEST_TRACK_ANDROID_PM;
        Role role = TEST_PM_ROLE;
        List<Role> roleList = List.of(role);
        List<PostTag> postTagList = List.of(TEST_POST_TAG_1, TEST_POST_TAG_2);
        List<Long> bookmarkedPostIdList = List.of(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);

        Post testPost = Post.builder()
                .postType(TEST_POST_TYPE)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(TEST_TUTOR)
                .track(TEST_TRACK_ANDROID_1L)
                .uploadedAt(LocalDate.now())
                .postTagList(postTagList)
                .build();

        List<Post> postList = List.of(testPost);
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(track, "id", 1L);
        ReflectionTestUtils.setField(testPost, "id", 1L);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(track);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(roleList);
        given(postService.findPostListByTagIdWithTagList(any(Long.class), any(Long.class), any(PostTypeEnum.class))).willReturn(postList);
        given(bookmarkService.findPostIdListByUserId(any(Long.class))).willReturn(bookmarkedPostIdList);
        // When
        Page<PostGetRes> result = postTagCoreService.searchPostByTag(TEST_TRACK_NAME, TEST_TRACK_PM_PERIOD, TEST_TAG_1L_ID,
                user, TEST_POST_TYPE, pageable);

        // Then
        assertThat(result.getContent().size()).isEqualTo(postList.size());
        assertThat(result.getContent().get(0).title()).isEqualTo(postList.get(0).getTitle());
    }


    @Test
    @DisplayName("유저가 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 권한이 존재하지 않는 실패 테스트")
    void 유저_태그_게시물_검색_기능_권한_없음으로_인한_실패_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        Track track = TEST_TRACK_ANDROID_1L;
        Pageable pageable = PageRequest.of(0, 10);

        ReflectionTestUtils.setField(user, "id", 1L);
        List<Role> roleList = new ArrayList<>();

        given(trackService.findTrackByTrackNameAndPeriod(track.getTrackName(), track.getPeriod())).willReturn(track);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(roleList);
        // When
        RoleCustomException exception = assertThrows(
                RoleCustomException.class, () -> postTagCoreService.searchPostByTag(track.getTrackName(), track.getPeriod(), TEST_TAG_1L_ID,
                        user, null, pageable)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo("ROLE-0001");
        assertThat(exception.getMessage()).isEqualTo("유저가 속한 트랙 없음");
    }

    @Test
    @DisplayName("유저가 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 권한이 올바르지 않음으로 인한 실패 테스트")
    void 유저_태그_게시물_검색_기능_권한_올바르지_않음으로_인한_실패_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        Track track = TEST_TRACK_AI_1L;
        Pageable pageable = PageRequest.of(0, 10);

        Role role = Role.builder()
                .user(user)
                .track(track)
                .trackRole(TrackRoleEnum.STUDENT)
                .auth(AuthEnum.APPROVE)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        List<Role> roleList = List.of(role);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(TEST_TRACK_ANDROID_1L);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(roleList);
        // When
        RoleCustomException exception = assertThrows(
                RoleCustomException.class, () -> postTagCoreService.searchPostByTag(TEST_TRACK_NAME, TEST_TRACK_1L_PERIOD, TEST_TAG_1L_ID,
                        user, null, pageable)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo("ROLE-3001");
        assertThat(exception.getMessage()).isEqualTo("해당 권한 접근 불가");
    }

    @Test
    @DisplayName("PM이 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 다른 트랙 게시물 조회로 인한 실패 테스트")
    void PM_태그_게시물_검색_기능_다른_트랙_게시물_조회로_인한_실패_테스트() {
        // Given
        User user = TEST_PM_USER;
        Track track = TEST_TRACK_AI_1L;
        Pageable pageable = PageRequest.of(0, 10);

        Role role = Role.builder()
                .user(user)
                .track(TEST_TRACK_ANDROID_PM)
                .trackRole(TrackRoleEnum.PM)
                .auth(AuthEnum.APPROVE)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(track);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(List.of(role));

        // When
        RoleCustomException exception = assertThrows(
                RoleCustomException.class, () -> postTagCoreService.searchPostByTag(track.getTrackName(), track.getPeriod(), TEST_TAG_1L_ID,
                        user, null, pageable)
        );
        // Then
        assertThat(exception.getErrorCode()).isEqualTo("ROLE-3001");
        assertThat(exception.getMessage()).isEqualTo("해당 권한 접근 불가");
    }

    @Test
    @DisplayName("APM이 태그를 이용해서 게시물 검색하는 기능 코어 서비스 로직 트랙이 허용되지 않음으로 인한 실패 테스트")
    void APM_태그_게시물_검색_기능_트랙_허용되지_않음으로_인한_실패_테스트() {
        // Given
        User user = TEST_APM_USER;
        Track track = TEST_TRACK_ANDROID_1L_NOT_PERMISSION;
        Role role = Role.builder()
                .user(user)
                .track(track)
                .trackRole(TrackRoleEnum.APM)
                .auth(AuthEnum.APPROVE)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        ReflectionTestUtils.setField(user, "id", 1L);

        given(trackService.findTrackByTrackNameAndPeriod(any(TrackNameEnum.class), any(Integer.class))).willReturn(track);
        given(roleService.findRoleListByUserIdAuthApprove(any(Long.class))).willReturn(List.of(role));
        // When
        TrackCustomException exception = assertThrows(
                TrackCustomException.class, () -> postTagCoreService.searchPostByTag(track.getTrackName(), track.getPeriod(), TEST_TAG_1L_ID,
                        user, null, pageable)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo("TRACK-3001");
        assertThat(exception.getMessage()).isEqualTo("트랙 열람권한 없음");
    }
}
