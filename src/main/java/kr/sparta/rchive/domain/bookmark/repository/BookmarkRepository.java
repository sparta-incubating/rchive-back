package kr.sparta.rchive.domain.bookmark.repository;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.entity.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId>, BookmarkRepositoryCustom {
    Optional<Bookmark> findBookmarkByUserIdAndPostId(Long userId, Long postId);
}
