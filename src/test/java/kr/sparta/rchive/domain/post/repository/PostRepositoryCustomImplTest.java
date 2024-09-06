package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.repository.TrackRepository;
import kr.sparta.rchive.global.config.QueryDslConfig;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.TagTest;
import kr.sparta.rchive.test.TrackTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({QueryDslConfig.class})  // PostRepositoryCustomImpl을 직접 로드
public class PostRepositoryCustomImplTest implements PostTest, TrackTest, TagTest {

    @Autowired
    private PostRepositoryCustomImpl postRepositoryCustom;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private PostRepository postRepository;

    Track track;
    Tutor tutor;
    Tag tag;
    Post post;


    @BeforeEach
    void setup() {
        track = trackRepository.save(TEST_TRACK_ANDROID_1L);

        Tutor buildTutor = Tutor.builder()
                .tutorName("test")
                .track(track)
                .build();

        tutor = tutorRepository.save(buildTutor);

        tag = tagRepository.save(TEST_1L_TAG);

        post = Post.builder()
                .postType(TEST_POST_TYPE)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(tutor)
                .track(track)
                .uploadedAt(LocalDate.now())
                .build();

        postRepository.save(post);
    }

    @AfterEach
    void afterEach() {
        postTagRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        tutorRepository.deleteAllInBatch();
        trackRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("백오피스에서 PM이 PostType은 All이고 모든 조건이 null이 아닌 게시물 리스트를 찾아오는 로직 테스트")
    @Order(1)
    void 백오피스_PM_전체_게시물_조건_NotNull_리스트_조회_테스트() {
        // given
        String title = "test title";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Boolean isOpened = true;
        Integer searchPeriod = 6;
        TrackNameEnum trackName = TrackNameEnum.ANDROID;
        Long tutorId = 1L;

        // when
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeAllByPm(title, startDate, endDate, isOpened, searchPeriod, trackName, tutorId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("백오피스에서 PM이 PostType은 All이고 모든 조건이 null인 게시물 리스트를 찾아오는 로직 테스트")
    @Order(2)
    void 백오피스_PM_전체_게시물_조건_Null_리스트_조회_테스트() {
        // given
        TrackNameEnum trackName = TrackNameEnum.ANDROID;

        // when
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeAllByPm(null, null, null, null, null, trackName, null);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 APM이 PostType은 All이고 모든 조건이 null이 아닌 게시물 리스트를 찾아오는 로직 테스트")
    @Order(3)
    void 백오피스_APM_전체_게시물_조건_NotNull_리스트_조회_테스트() {
        // given
        String title = TEST_POST_TITLE;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.now();
        Boolean isOpened = true;
        Long trackId = track.getId();
        Long tutorId = tutor.getId();

        // when
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeAllByApm(title, startDate, endDate, isOpened, trackId, tutorId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 APM이 PostType은 All이고 모든 조건이 null인 게시물 리스트를 찾아오는 로직 테스트")
    @Order(4)
    void 백오피스_APM_전체_게시물_조건_Null_리스트_조회_테스트() {
        // given
        Long trackId = track.getId();

        // when
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeAllByApm(null, null, null, null, trackId, null);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 PM이 특정 PostType에 특정 조건의 게시물 리스트를 찾아오는 로직 테스트")
    @Order(5)
    void 백오피스_PM_특정_PostType_게시물_특정_조건_리스트_조회_테스트() {
        // Given
        String title = TEST_POST_TITLE;
        PostTypeEnum postType = PostTypeEnum.Sparta_Lecture;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        // When
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeNotNullByPM(postType, title, startDate,
                endDate, track.getPeriod(), true, track.getTrackName(), tutor.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 PM이 특정 PostType에 조건은 null의 게시물 리스트를 찾아오는 로직 테스트")
    @Order(6)
    void 백오피스_PM_특정_PostType_게시물_조건_null_리스트_조회_테스트() {
        // Given
        PostTypeEnum postType = PostTypeEnum.Sparta_Lecture;

        // When
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeNotNullByPM(postType, null, null,
                null, null, null, track.getTrackName(), null);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 APM이 특정 PostType에 특정 조건의 게시물 리스트를 찾아오는 로직 테스트")
    @Order(7)
    void 백오피스_APM_특정_PostType_게시물_조건_null_리스트_조회_테스트() {
        // Given
        String title = TEST_POST_TITLE;
        PostTypeEnum postType = PostTypeEnum.Sparta_Lecture;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        // When
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeNotNullApm(postType, title, startDate,
                endDate, track.getId(), true, tutor.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("백오피스에서 APM이 특정 PostType에 조건은 null의 게시물 리스트를 찾아오는 로직 테스트")
    @Order(8)
    void 백오피스_APM_특정_PostType_특정_게시물_조건_리스트_조회_테스트() {
        // Given
        PostTypeEnum postType = PostTypeEnum.Sparta_Lecture;

        // When
        List<Post> result = postRepositoryCustom.findPostListInBackOfficePostTypeNotNullApm(postType, null, null,
                null, track.getId(), null, null);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("유저가 특정 PostType과 Track에 맞고 튜터 id로 필터링 한 게시물 리스트 조회해오는 로직 테스트")
    @Order(9)
    void 유저_특정_PostType_자신의_Track_튜터로_필터링한_게시물_리스트_조회_테스트() {
        // Given
        Post testPost = Post.builder()
                .postType(PostTypeEnum.Level_Basic)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(tutor)
                .track(track)
                .uploadedAt(LocalDate.now())
                .build();

        postRepository.save(testPost);

        // When
        List<Post> postListPostTypeLevelAll = postRepository.findAllByPostTypeAndTrackIdUserRoleUser(PostTypeEnum.Level_All,
                track.getId(), tutor.getId());
        List<Post> postListPostTypeNotNull = postRepository.findAllByPostTypeAndTrackIdUserRoleUser(TEST_POST_TYPE,
                track.getId(), tutor.getId());

        // Then
        assertThat(postListPostTypeLevelAll).isNotEmpty();
        assertThat(postListPostTypeNotNull).isNotEmpty();
        assertThat(postListPostTypeLevelAll.get(0).getTitle()).isEqualTo(testPost.getTitle());
        assertThat(postListPostTypeNotNull.get(0).getTitle()).isEqualTo(TEST_POST_TITLE);
    }

    @Test
    @DisplayName("유저가 PostType은 null이고 Track에 맞고 튜터 id로 필터링하지 않은 게시물 리스트 조회해오는 로직 테스트")
    @Order(10)
    void 유저_PostType_null_자신의_Track_튜터로_필터링하지_않은_게시물_리스트_조회_테스트() {
        // Given - When
        List<Post> result = postRepository.findAllByPostTypeAndTrackIdUserRoleUser(null, track.getId(), null);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo(TEST_POST_TITLE);
    }
}