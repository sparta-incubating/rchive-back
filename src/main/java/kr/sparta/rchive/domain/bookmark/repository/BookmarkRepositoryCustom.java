package kr.sparta.rchive.domain.bookmark.repository;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {
    List<Bookmark> findBookmarkListByUserId(Long userId);
}
