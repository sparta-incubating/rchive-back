package kr.sparta.rchive.domain.bookmark.service;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.repository.BookmarkRepository;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public void createBookmark(User user, Post findPost) {
        Bookmark bookmark = Bookmark.builder()
            .post(findPost)
            .user(user)
            .build();

        bookmarkRepository.save(bookmark);
    }

    private Bookmark findBookmarkByUserIdAndPostId(Long userId, Long postId) {
        return bookmarkRepository.findBookmarkByUserIdAndPostId(userId, postId).orElseThrow(
            () -> new PostCustomException(PostExceptionCode.NOT_FOUND_BOOKMARK_NOT_EXIST)
        );
    }

    public void deleteBookmark(Long userId, Long postId) {
        Bookmark findBookmark = findBookmarkByUserIdAndPostId(userId, postId);

        bookmarkRepository.delete(findBookmark);
    }
}
