package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.test.CommentTest;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostCommentCoreServiceTest implements PostTest, CommentTest, UserTest {
    @InjectMocks
    private PostCommentCoreService postCommentCoreService;

    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private TrackService trackService;

    @Test
    @DisplayName("부모 댓글 생성 코어 서비스 로직 성공 테스트")
    void 부모_댓글_생성_기능_성공_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        Post post = TEST_POST_1L;

        CommentCreateReq request = CommentCreateReq.builder()
                .content("test")
                .build();

        given(postService.findPostById(any(Long.class))).willReturn(post);

        // When
        postCommentCoreService.createComment(user, 1L, null, request);

        // Then
        verify(commentService, times(1)).createComment(any(User.class), any(Post.class), isNull(), any(CommentCreateReq.class));
    }
}
