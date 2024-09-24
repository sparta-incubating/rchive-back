package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.comment.entity.Comment;

public interface CommentTest extends UserTest, PostTest{
    Long TEST_COMMENT_1L_ID = 1L;
    Long TEST_COMMENT_2L_ID = 2L;
    Long TEST_COMMENT_3L_ID = 3L;
    String TEST_COMMENT_CONTENT = "Test Content";

    Comment TEST_1L_COMMENT = Comment.builder()
            .content(TEST_COMMENT_CONTENT)
            .isDeleted(false)
            .user(TEST_STUDENT_USER)
            .post(TEST_POST)
            .build();

    Comment TEST_2L_COMMENT = Comment.builder()
            .content(TEST_COMMENT_CONTENT)
            .isDeleted(false)
            .parentComment(TEST_1L_COMMENT)
            .user(TEST_STUDENT_USER)
            .post(TEST_POST)
            .build();

    Comment TEST_3L_COMMENT = Comment.builder()
            .content(TEST_COMMENT_CONTENT)
            .isDeleted(false)
            .user(TEST_STUDENT_USER)
            .post(TEST_POST)
            .build();
}
