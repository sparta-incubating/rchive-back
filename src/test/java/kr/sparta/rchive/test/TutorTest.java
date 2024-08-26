package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.post.entity.Tutor;

public interface TutorTest extends TrackTest {
    Long TEST_TUTOR_ID = 1L;
    String TEST_TUTOR_NAME = "Test Tutor";

    Tutor TEST_TUTOR = Tutor.builder()
            .tutorName(TEST_TUTOR_NAME)
            .track(TEST_TRACK_ANDROID)
            .build();
}
