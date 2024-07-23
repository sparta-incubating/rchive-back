package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.bookmark.service.BookmarkService;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostBookmarkCoreService {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    public void createBookmark(User user, Long postId) {
        Post findPost = postService.findPostById(postId);

        bookmarkService.createBookmark(user, findPost);
    }

    public void deleteBookmark(User user, Long postId) {

        bookmarkService.deleteBookmark(user.getId(), postId);
    }
}
