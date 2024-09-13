package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public interface TrackTest {
    Long TEST_TRACK_ID = 1L;
    TrackNameEnum TEST_TRACK_NAME = TrackNameEnum.ANDROID;
    Integer TEST_TRACK_PM_PERIOD = 0;
    Integer TEST_TRACK_1L_PERIOD = 1;
    Integer TEST_TRACK_2L_PERIOD = 2;
    Boolean TEST_TRACK_IS_PERMISSION = true;
    Boolean TEST_TRACK_IS_DELETED = false;

    Track TEST_TRACK_ANDROID_PM = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_PM_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();

    Track TEST_TRACK_ANDROID_1L = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_1L_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();

    Track TEST_TRACK_ANDROID_1L_NOT_PERMISSION = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_1L_PERIOD)
            .isPermission(false)
            .isDeleted(false)
            .build();

    Track TEST_TRACK_ANDROID_2L = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_2L_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();

    Track TEST_TRACK_AI_1L = Track.builder()
            .trackName(TrackNameEnum.AI)
            .period(TEST_TRACK_1L_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();
}
