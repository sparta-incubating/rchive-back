package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.bookmark.service.BookmarkService;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.test.BookmarkTest;
import kr.sparta.rchive.test.PostTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PostBookmarkCoreServiceTest implements BookmarkTest, PostTest {

    @InjectMocks
    private PostBookmarkCoreService postBookmarkCoreService;

    @Mock
    private PostService postService;
    @Mock
    private BookmarkService bookmarkService;

    @Test
    @DisplayName("북마크 생성하는 코어 서비스 로직 성공 테스트")
    void 북마크_생성_성공_테스트() {
        // Given
        Post post = TEST_POST_1L;
        Long postId = TEST_POST_1L_ID;

        given(postService.findPostById(postId)).willReturn(post);
        // When
        postBookmarkCoreService.createBookmark(TEST_STUDENT_USER, postId);

        // Then
        verify(bookmarkService, times(1)).createBookmark(any(User.class), any(Post.class));
    }
}
