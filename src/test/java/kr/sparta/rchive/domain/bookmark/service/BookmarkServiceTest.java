package kr.sparta.rchive.domain.bookmark.service;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.repository.BookmarkRepository;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.test.BookmarkTest;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest implements UserTest, BookmarkTest, PostTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("북마크 생성 서비스 로직 성공 테스트")
    void 북마크_생성_서비스_성공_테스트() {
        // Given
        Boolean checkBookmark = false;
        User user = TEST_STUDENT_USER;
        Post post = TEST_POST_1L;

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(bookmarkRepository.existsBookmarkByUserIdAndPostId(any(Long.class), any(Long.class))).willReturn(checkBookmark);

        // When
        bookmarkService.createBookmark(user, post);

        // Then
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 생성 서비스 로직 중복된 북마크로 인한 실패 테스트")
    void 북마크_생성_서비스_북마크_중복으로_인한_실패_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        Post post = TEST_POST_1L;
        Boolean checkBookmark = true;

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(bookmarkRepository.existsBookmarkByUserIdAndPostId(any(Long.class), any(Long.class))).willReturn(checkBookmark);

        // When
        PostCustomException exception = assertThrows(
                PostCustomException.class, () -> bookmarkService.createBookmark(user, post)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo(PostExceptionCode.CONFLICT_BOOKMARK.getMessage());
        assertThat(exception.getErrorCode()).isEqualTo(PostExceptionCode.CONFLICT_BOOKMARK.getErrorCode());
    }

    @Test
    @DisplayName("북마크 삭제 서비스 로직 성공 테스트")
    void 북마크_삭제_서비스_성공_테스트() {
        // Given
        Long userId = TEST_STUDENT_ID;
        Long postId = TEST_POST_1L_ID;
        Bookmark bookmark = TEST_BOOKMAKR_1L;

        given(bookmarkRepository.findBookmarkByUserIdAndPostId(any(Long.class), any(Long.class))).willReturn(Optional.of(bookmark));

        // When
        bookmarkService.deleteBookmark(userId, postId);

        // Then
        verify(bookmarkRepository, times(1)).delete(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 삭제 서비스 로직 북마크 존재하지 않음으로 인한 실패 테스트")
    void 북마크_삭제_서비스_존재하지_않는_북마크로_인한_실패_테스트() {
        // Given
        Long userId = TEST_STUDENT_ID;
        Long postId = TEST_POST_1L_ID;

        given(bookmarkRepository.findBookmarkByUserIdAndPostId(any(Long.class), any(Long.class))).willReturn(Optional.empty());

        // When
        PostCustomException exception = assertThrows(
                PostCustomException.class, () -> bookmarkService.deleteBookmark(userId, postId)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo(PostExceptionCode.NOT_FOUND_BOOKMARK.getMessage());
        assertThat(exception.getErrorCode()).isEqualTo(PostExceptionCode.NOT_FOUND_BOOKMARK.getErrorCode());
    }
}