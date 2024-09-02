package kr.sparta.rchive.domain.post.repository;

import jakarta.persistence.EntityManager;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.global.config.AuditingConfig;
import kr.sparta.rchive.global.config.QueryDslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({QueryDslConfig.class})  // PostRepositoryCustomImpl을 직접 로드
public class PostRepositoryCustomImplTest {

    @Autowired
    private PostRepositoryCustomImpl postRepositoryCustom;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 데이터 준비
        // Post, Tag, Track, Tutor 등의 엔티티를 생성하고 저장
    }

    @Test
    @DisplayName("백오피스에서 PM이 PostType은 All이고 모든 조건이 null이 아닌 게시물 리스트를 찾아오는 로직 테스트")
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

    // 추가적인 테스트 메소드 작성
}