package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.TrackTest;
import kr.sparta.rchive.test.TutorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
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
}
