package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.post.entity.PostTag;

public interface PostTagTest extends PostTest, TagTest{
    PostTag TEST_POST_TAG_1 = PostTag.builder()
            .post(TEST_POST_1L)
            .tag(TEST_1L_TAG)
            .build();

    PostTag TEST_POST_TAG_2 = PostTag.builder()
            .post(TEST_POST_1L)
            .tag(TEST_1L_TAG)
            .build();
}
