package kr.sparta.rchive.domain.bookmark.repository;

import java.util.Optional;
import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.entity.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    Optional<Bookmark> findBookmarkByUserIdAnAndPostId(Long userId, Long postId);
}
