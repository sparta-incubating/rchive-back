package kr.sparta.rchive.domain.bookmark.repository;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.entity.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId>, BookmarkRepositoryCustom {
    Optional<Bookmark> findBookmarkByUserIdAndPostId(Long userId, Long postId);

    @Query("select b.post.id from Bookmark b where b.user.id = :userId")
    List<Long> findPostIdListByUserId(Long userId);

    Boolean existsBookmarkByUserIdAndPostId(Long userId, Long postId);
}