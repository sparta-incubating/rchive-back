package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.TrackTest;
import kr.sparta.rchive.test.TutorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements PostTest, TrackTest, TutorTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("게시물 생성하는 서비스 로직 성공 테스트")
    void 게시물_생성_서비스_성공_테스트() {
        // Given
        PostCreateReq request = PostCreateReq.builder()
                .postType(PostTypeEnum.Sparta_Lecture)
                .title("Test_Post_Create")
                .tutorId(1L)
                .uploadedAt(LocalDate.now())
                .thumbnailUrl("test")
                .videoLink("test")
                .contentLink("test")
                .content("test")
                .postPeriod(1)
                .isOpened(true)
                .build();
        Track track = TEST_TRACK_ANDROID;
        Tutor tutor = TEST_TUTOR;

        // When
        postService.createPost(request, track, tutor);

        // Then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 업데이트하는 서비스 로직 성공 테스트")
    void 게시물_업데이트_서비스_성공_테스트() {
        // Given
        Post post = TEST_POST;
        Track track = TEST_TRACK_ANDROID;
        Tutor tutor = TEST_TUTOR;

        PostUpdateReq request = PostUpdateReq.builder()
                .postType(PostTypeEnum.Sparta_Lecture)
                .title("Test")
                .tutorId(TEST_TUTOR_ID)
                .uploadedAt(LocalDate.now())
                .thumbnailUrl("Test")
                .videoLink("Test")
                .content("Test")
                .contentLink("Test")
                .trackName(TrackNameEnum.ANDROID)
                .updatePeriod(1)
                .isOpened(true)
                .build();
        // When
        postService.updatePost(post, request, track, tutor);

        // Then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 삭제하는 서비스 로직 성공 테스트")
    void 게시물_삭제_서비스_성공_테스트() {
        // Given
        Post post = TEST_POST;

        // When
        postService.deletePost(post);

        // Then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("교육자료를 교육자료의 ID를 이용하여 검색하는 서비스 로직 성공 테스트")
    void 게시물_아이디로_게시물_찾아오는_서비스_성공_테스트() {
        // Given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_POST));

        // When
        Post post = postService.findPostById(any(Long.class));

        // Then
        assertThat(post.getTitle()).isEqualTo(TEST_POST.getTitle());
        assertThat(post.getThumbnailUrl()).isEqualTo(TEST_POST.getThumbnailUrl());
        assertThat(post.getContent()).isEqualTo(TEST_POST.getContent());
    }

    @Test
    @DisplayName("교육자료를 교육자료의 ID를 이용하여 검색하는 서비스 로직 실패 테스트")
    void 게시물_아이디로_게시물_찾아오는_서비스_실패_테스트() {
        // Given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When
        PostCustomException exception = assertThrows(
                PostCustomException.class, () -> postService.findPostById(any(Long.class))
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo("POST-4001");
        assertThat(exception.getMessage()).isEqualTo("자료를 찾을 수 없음");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}