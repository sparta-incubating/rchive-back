package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.bookmark.entity.Bookmark;

public interface BookmarkTest extends PostTest, UserTest{
    Long TEST_BOOKMARK_1L_ID = 1L;
    Long TEST_BOOKMARK_2L_ID = 2L;

    Bookmark TEST_BOOKMAKR_1L = Bookmark.builder()
            .user(TEST_STUDENT_USER)
            .post(TEST_POST_1L)
            .build();

    Bookmark TEST_BOOKMAKR_2L = Bookmark.builder()
            .user(TEST_STUDENT_USER)
            .post(TEST_POST_2L)
            .build();
}
