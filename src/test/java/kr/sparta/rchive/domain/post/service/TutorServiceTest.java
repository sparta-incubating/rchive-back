package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.response.TutorRes;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.TutorRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.test.TrackTest;
import kr.sparta.rchive.test.TutorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest implements TutorTest, TrackTest {
    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Test
    @DisplayName("튜터 검색하는 기능 서비스 로직 성공 테스트")
    void 튜터_검색기능_성공_테스트() {
        // Given
        Long tutorId = 1L;

        given(tutorRepository.findById(tutorId)).willReturn(Optional.of(TEST_TUTOR));

        // When
        Tutor result = tutorService.findTutorById(tutorId);

        // Then
        assertThat(result.getTutorName()).isEqualTo(TEST_TUTOR.getTutorName());
    }

    @Test
    @DisplayName("튜터 검색하는 기능 서비스 로직 실패 테스트")
    void 튜터_검색기능_실패_테스트() {
        // Given
        Long tutorId = 1L;

        given(tutorRepository.findById(tutorId)).willReturn(Optional.empty());

        // When
        PostCustomException exception = assertThrows(
                PostCustomException.class, () -> tutorService.findTutorById(tutorId)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(PostExceptionCode.NOT_FOUND_TUTOR.getErrorCode());
        assertThat(exception.getMessage()).isEqualTo(PostExceptionCode.NOT_FOUND_TUTOR.getMessage());
    }

    @Test
    @DisplayName("튜터를 찾아오고 해당 튜터가 해당 트랙에 속해있는지 확인하는 서비스 로직 성공 테스트")
    void 튜터_트랙_체크하는_기능_성공_테스트() {
        // Given
        Long tutorId = 1L;
        Tutor tutor = TEST_TUTOR;
        Track track = TEST_TRACK_ANDROID_1L;

        given(tutorRepository.findById(tutorId)).willReturn(Optional.of(tutor));

        // When
        Tutor result = tutorService.checkTutor(tutorId, track);

        // Then
        assertThat(result.getTutorName()).isEqualTo(tutor.getTutorName());
    }

    @Test
    @DisplayName("튜터의 트랙을 체크하는 서비스 로직 트랙이 틀림으로 인한 실패 테스트")
    void 튜터_트랙_체크하는_기능_트랙_틀림으로_인한_실패_테스트() {
        // Given
        Long tutorId = 1L;
        Tutor tutor = TEST_TUTOR;
        Track track = TEST_TRACK_AI_1L;

        given(tutorRepository.findById(tutorId)).willReturn(Optional.of(tutor));

        // When
        PostCustomException exception = assertThrows(
                PostCustomException.class, () -> tutorService.checkTutor(tutorId, track)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(PostExceptionCode.BAD_REQUEST_NOT_TRACK_TUTOR.getErrorCode());
        assertThat(exception.getMessage()).isEqualTo(PostExceptionCode.BAD_REQUEST_NOT_TRACK_TUTOR.getMessage());
    }

    @Test
    @DisplayName("튜터 이름과 트랙 id로 튜터 리스트를 찾아오는 서비스 로직 성공 테스트")
    void 튜터이름_트랙ID_튜터리스트_찾아오는_기능_성공_테스트() {
        // Given
        String tutorName = TEST_TUTOR_NAME;
        Long trackId = 1L;

        List<Tutor> tutorList = List.of(TEST_TUTOR);

        given(tutorRepository.findTutorList(any(String.class), any(Long.class))).willReturn(tutorList);

        // When
        List<TutorRes> result = tutorService.findTutorListByTutorNameAndTrackId(tutorName, trackId);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).tutorName()).isEqualTo(tutorName);
    }
}
