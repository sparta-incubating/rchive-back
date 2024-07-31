package kr.sparta.rchive.domain.bookmark.service;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.repository.BookmarkRepository;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.response.PostRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    public void createBookmark(User user, Post findPost) {
        Bookmark bookmark = Bookmark.builder()
                .post(findPost)
                .user(user)
                .build();

        bookmarkRepository.save(bookmark);
    }

    private Bookmark findBookmarkByUserIdAndPostId(Long userId, Long postId) {
        return bookmarkRepository.findBookmarkByUserIdAndPostId(userId, postId).orElseThrow(
            () -> new PostCustomException(PostExceptionCode.NOT_FOUND_BOOKMARK)
        );
    }

    public void deleteBookmark(Long userId, Long postId) {
        Bookmark findBookmark = findBookmarkByUserIdAndPostId(userId, postId);

        bookmarkRepository.delete(findBookmark);
    }

    public List<PostRes> getUserBookmark(Long userId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkListByUserId(userId);

        return bookmarkList.stream().map(
                bookmark -> {
                    List<TagInfo> tagInfoList = bookmark.getPost().getPostTagList().stream()
                            .map(postTag -> TagInfo.builder()
                                    .tagId(postTag.getTag().getId())
                                    .tagName(postTag.getTag().getTagName())
                                    .build()).collect(Collectors.toList());

                    return PostRes.builder()
                            .title(bookmark.getPost().getTitle())
                            .tutor(bookmark.getPost().getTutor().getTutorName())
                            .uploadedAt(bookmark.getPost().getUploadedAt())
                            .tagInfoList(tagInfoList).build();
                }
        ).collect(Collectors.toList());
    }

    public List<Long> findPostIdListByUserId(Long userId) {
        return bookmarkRepository.findPostIdListByUserId(userId);
    }

    public Boolean existsBookmarkByUserIdAndPostId(Long userId, Long postId) {
        return bookmarkRepository.existsBookmarkByUserIdAndPostId(userId, postId);
    }
}
