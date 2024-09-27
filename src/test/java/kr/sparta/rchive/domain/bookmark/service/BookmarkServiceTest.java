package kr.sparta.rchive.domain.bookmark.service;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.repository.BookmarkRepository;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.test.BookmarkTest;
import kr.sparta.rchive.test.PostTagTest;
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

import java.sql.Ref;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest implements UserTest, BookmarkTest, PostTest, PostTagTest {

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

    @Test
    @DisplayName("유저의 북마크 리스트를 조회하는 서비스 로직 성공 테스트")
    void 유저_북마크_리스트_조회_서비스_성공_테스트() {
        // Given
        Long userId = TEST_STUDENT_ID;

        Post post = Post.builder()
                .postType(TEST_POST_TYPE)
                .title(TEST_POST_TITLE)
                .thumbnailUrl(TEST_POST_THUMBNAIL)
                .videoLink(TEST_POST_VIDEO_LINK)
                .contentLink(TEST_POST_CONTENT_LINK)
                .content(TEST_POST_CONTENT)
                .tutor(TEST_TUTOR)
                .track(TEST_TRACK_ANDROID_1L)
                .postTagList(List.of(TEST_POST_TAG_1, TEST_POST_TAG_2))
                .uploadedAt(LocalDate.now())
                .build();

        Bookmark bookmark = Bookmark.builder()
                .user(TEST_STUDENT_USER)
                .post(post)
                .build();

        List<Bookmark> bookmarkList = List.of(bookmark);

        ReflectionTestUtils.setField(bookmark.getPost().getPostTagList().get(0).getTag(), "id", 1L);
        ReflectionTestUtils.setField(bookmark.getPost().getPostTagList().get(1).getTag(), "id", 2L);
        ReflectionTestUtils.setField(bookmark.getPost(), "id", 1L);

        given(bookmarkRepository.findBookmarkListByUserId(any(Long.class))).willReturn(bookmarkList);
        // When
        List<PostGetRes> result = bookmarkService.getUserBookmark(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo(post.getTitle());
        assertThat(result.get(0).thumbnailUrl()).isEqualTo(post.getThumbnailUrl());
    }

    @Test
    @DisplayName("유저가 북마크에 등록해 둔 게시물 ID 리스트를 조회해오는 서비스 로직 성공 테스트")
    void 유저_북마크_게시물_ID_리스트_조회_서비스_성공_테스트() {
        // Given
        Long userId = TEST_STUDENT_ID;
        List<Long> postIdList = List.of(1L, 2L);

        given(bookmarkRepository.findPostIdListByUserId(any(Long.class))).willReturn(postIdList);

        // When
        List<Long> result = bookmarkService.findPostIdListByUserId(userId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(1L);
        assertThat(result.get(1)).isEqualTo(2L);
    }

    @Test
    @DisplayName("유저가 등록한 북마크가 존재하는지 체크하는 서비스 로직 성공 테스트")
    void 유저_북마크_존재여부_체크하는_서비스_성공_테스트() {
        // Given
        Boolean checkBookmark = true;
        Long userId = TEST_STUDENT_ID;
        Long postId = TEST_POST_1L_ID;

        given(bookmarkRepository.existsBookmarkByUserIdAndPostId(any(Long.class), any(Long.class))).willReturn(checkBookmark);

        // When
        Boolean result = bookmarkService.existsBookmarkByUserIdAndPostId(userId, postId);

        // Then
        assertThat(result).isEqualTo(checkBookmark);
    }

    @Test
    @DisplayName("북마크에 등록해 둔 게시물 리스트에서 검색하는 서비스 로직 성공 테스트")
    void 북마크_게시물_리스트_검색_서비스_성공_테스트() {
        // Given
        User user = TEST_STUDENT_USER;
        String keyword = "test";
        List<Bookmark> bookmarkList = List.of(TEST_BOOKMAKR_1L, TEST_BOOKMAKR_2L);

        ReflectionTestUtils.setField(user, "id", 1L);

        given(bookmarkRepository.findBookmarkListByUserIdAndKeyword(any(Long.class), any(String.class))).willReturn(bookmarkList);

        // When
        List<PostGetRes> result = bookmarkService.searchBookmark(user, keyword);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo(TEST_BOOKMAKR_1L.getPost().getTitle());
        assertThat(result.get(1).title()).isEqualTo(TEST_BOOKMAKR_2L.getPost().getTitle());
    }
}