package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.repository.TrackRepository;
import kr.sparta.rchive.global.config.QueryDslConfig;
import kr.sparta.rchive.test.TrackTest;
import kr.sparta.rchive.test.TutorTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({QueryDslConfig.class})
public class TutorRepositoryCustomImplTest implements TrackTest, TutorTest {

    @Autowired
    private TutorRepositoryCustomImpl tutorRepositoryCustom;
    @Autowired
    private TrackRepository trackRepository;

    Track track;
    Tutor tutor;
    @Autowired
    private TutorRepository tutorRepository;

    @BeforeEach
    void setUp() {
        track = trackRepository.save(TEST_TRACK_ANDROID_1L);
        tutor = Tutor.builder()
                .tutorName("test")
                .track(track)
                .build();
        tutorRepository.save(tutor);
        Tutor tutor2 = Tutor.builder()
                .tutorName("tutor")
                .track(track)
                .build();
        tutorRepository.save(tutor2);
    }

    @AfterEach
    void afterEach() {
        tutorRepository.deleteAllInBatch();
        trackRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("튜터 이름과 트랙 id로 튜터 리스트를 찾아오는 로직 테스트")
    @Order(1)
    void 튜터_이름_트랙_ID로_튜터_리스트_조회_테스트() {
        // Given
        String tutorName = tutor.getTutorName();
        Long trackId = track.getId();

        // When
        List<Tutor> result = tutorRepositoryCustom.findTutorList(tutorName, trackId);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTutorName()).isEqualTo(tutorName);
    }
}
