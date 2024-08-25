package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;

public interface PostTest extends TrackTest, TutorTest{
    Long TEST_POST_ID = 1L;
    PostTypeEnum TEST_POST_TYPE = PostTypeEnum.Sparta_Lecture;
    String TEST_POST_TITLE = "Test Post";
    String TEST_POST_THUMBNAIL = "Test Post Thumbnail";
    String TEST_POST_CONTENT = "Test Post Content";
    String TEST_POST_VIDEO_LINK = "Test Post Video Link";
    String TEST_POST_CONTENT_LINK = "Test Post Content Link";

    Post TEST_POST = Post.builder()
            .postType(TEST_POST_TYPE)
            .title(TEST_POST_TITLE)
            .thumbnailUrl(TEST_POST_THUMBNAIL)
            .videoLink(TEST_POST_VIDEO_LINK)
            .contentLink(TEST_POST_CONTENT_LINK)
            .content(TEST_POST_CONTENT)
            .tutor(TEST_TUTOR)
            .track(TEST_TRACK_ANDROID)
            .build();
}
