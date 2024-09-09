package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;

public interface RoleTest extends UserTest, TrackTest{
    Role TEST_PM_ROLE = Role.builder()
            .user(TEST_PM_USER)
            .track(TEST_TRACK_ANDROID_PM)
            .trackRole(TrackRoleEnum.PM)
            .auth(AuthEnum.APPROVE)
            .build();

    Role TEST_APM_ROLE = Role.builder()
            .user(TEST_APM_USER)
            .track(TEST_TRACK_ANDROID_1L)
            .trackRole(TrackRoleEnum.APM)
            .auth(AuthEnum.APPROVE)
            .build();

    Role TEST_STUDENT_ROLE = Role.builder()
            .user(TEST_STUDENT_USER)
            .track(TEST_TRACK_ANDROID_1L)
            .trackRole(TrackRoleEnum.STUDENT)
            .auth(AuthEnum.APPROVE)
            .build();
}
