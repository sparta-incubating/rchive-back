package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.TutorRepository;
import kr.sparta.rchive.test.TutorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest implements TutorTest {
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
}
