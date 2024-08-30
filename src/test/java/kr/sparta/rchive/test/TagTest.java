package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.post.entity.Tag;

public interface TagTest {
    Long TEST_TAG_1L_ID = 1L;
    Long TEST_TAG_2L_ID = 2L;
    Long TEST_TAG_3L_ID = 3L;
    String TEST_TAG_1L_NAME = "Test 1L Tag";
    String TEST_TAG_2L_NAME = "Test 2L Tag";
    String TEST_TAG_3L_NAME = "Test 3L Tag";

    Tag TEST_1L_TAG = Tag.builder()
            .tagName(TEST_TAG_1L_NAME)
            .build();

    Tag TEST_2L_TAG = Tag.builder()
            .tagName(TEST_TAG_2L_NAME)
            .build();

    Tag TEST_3L_TAG = Tag.builder()
            .tagName(TEST_TAG_3L_NAME)
            .build();
}
