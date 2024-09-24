package kr.sparta.rchive.domain.bookmark.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.repository.BookmarkRepository;
import kr.sparta.rchive.domain.post.dto.PostTypeInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    public void createBookmark(User user, Post findPost) {

        Boolean checkBookmark = bookmarkRepository.existsBookmarkByUserIdAndPostId(user.getId(), findPost.getId());

        if (checkBookmark) {
            throw new PostCustomException(PostExceptionCode.CONFLICT_BOOKMARK);
        }

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

    // TODO: 페이징 적용
    public List<PostGetRes> getUserBookmark(Long userId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkListByUserId(userId);

        return getBookmarkPostRes(bookmarkList);
    }

    public List<Long> findPostIdListByUserId(Long userId) {
        return bookmarkRepository.findPostIdListByUserId(userId);
    }

    public Boolean existsBookmarkByUserIdAndPostId(Long userId, Long postId) {
        return bookmarkRepository.existsBookmarkByUserIdAndPostId(userId, postId);
    }

    // TODO: 페이징 적용
    public List<PostGetRes> searchBookmark(User user, String keyword) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkListByUserIdAndKeyword(user.getId(), keyword);

        return getBookmarkPostRes(bookmarkList);
    }

    private List<PostGetRes> getBookmarkPostRes(List<Bookmark> bookmarkList) {
        return bookmarkList.stream().map(
            bookmark -> {
                List<TagInfo> tagInfoList = bookmark.getPost().getPostTagList().stream()
                    .map(postTag -> TagInfo.builder()
                        .tagId(postTag.getTag().getId())
                        .tagName(postTag.getTag().getTagName())
                        .build()).collect(Collectors.toList());

                return PostGetRes.builder()
                    .postId(bookmark.getPost().getId())
                    .title(bookmark.getPost().getTitle())
                    .thumbnailUrl(bookmark.getPost().getThumbnailUrl())
                    .postType(PostTypeInfo.of(bookmark.getPost().getPostType()))
                    .tutor(bookmark.getPost().getTutor().getTutorName())
                    .uploadedAt(bookmark.getPost().getUploadedAt())
                    .isBookmarked(true)
                    .tagList(tagInfoList).build();
            }
        ).collect(Collectors.toList());
    }
}
