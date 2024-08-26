package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public interface TrackTest {
    Long TEST_TRACK_ID = 1L;
    TrackNameEnum TEST_TRACK_NAME = TrackNameEnum.ANDROID;
    Integer TEST_TRACK_PM_PERIOD = 0;
    Integer TEST_TRACK_PERIOD = 1;
    Boolean TEST_TRACK_IS_PERMISSION = true;
    Boolean TEST_TRACK_IS_DELETED = false;

    Track TEST_TRACK_ANDROID_PM = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_PM_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();

    Track TEST_TRACK_ANDROID = Track.builder()
            .trackName(TEST_TRACK_NAME)
            .period(TEST_TRACK_PERIOD)
            .isPermission(true)
            .isDeleted(false)
            .build();
}
