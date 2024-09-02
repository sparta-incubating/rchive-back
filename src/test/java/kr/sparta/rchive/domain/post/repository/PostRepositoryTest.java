package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.dto.response.TutorRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.repository.TrackRepository;
import kr.sparta.rchive.domain.user.repository.UserRepository;
import kr.sparta.rchive.global.config.AuditingConfig;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({QueryDslConfig.class, AuditingConfig.class})
public class PostRepositoryTest implements PostTest, TrackTest, TagTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostTagRepository postTagRepository;

    Track track;
    Tutor tutor;
    Tag tag;

    @BeforeEach
    void setup() {
        track = trackRepository.save(TEST_TRACK_ANDROID_1L);

        Tutor buildTutor = Tutor.builder()
                .tutorName("test")
                .track(track)
                .build();

        tutor = tutorRepository.save(buildTutor);

        tag = tagRepository.save(TEST_1L_TAG);
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
    @DisplayName("매개변수로 들어온 postIdList 안에 ID 중 DB안에 해당하는 ID의 게시물을 찾아오는 로직 테스트")
    @Order(1)
    void PostIdList_안에_ID_중_DB_안에_해당하는_ID의_게시물_찾아오는_로직_테스트() {
        // Given
        List<Post> responseList = new ArrayList<>();

        for (int i = 0; i <= 1; i++) {
            Post post = Post.builder()
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

            responseList.add(postRepository.save(post));
        }

        List<Long> postIdList = List.of(1L, 2L);

        List<Post> resultpostList = postRepository.saveAll(responseList);
        // When
        List<Post> postList = postRepository.findPostByIdIn(postIdList);

        // Then
        assertThat(postList.size()).isEqualTo(2);
        assertThat(postList.get(0).getTitle()).isEqualTo(responseList.get(0).getTitle());
    }

    @Test
    @DisplayName("교육자료의 디테일한 정보까지 전부 가져오는 repository 로직 테스트")
    @Order(2)
    void 교육자료_디테일_전부_가져오는_기능_테스트() {
        // Given
        Post post = Post.builder()
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

        PostTag postTag = PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
        postTagRepository.save(postTag);

        Long postId = 3L;
        // When
        Post findPost = postRepository.findPostWithDetailByPostId(postId);

        // Then
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
    }
}
