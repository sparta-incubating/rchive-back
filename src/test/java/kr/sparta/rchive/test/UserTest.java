package kr.sparta.rchive.test;

import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;

public interface UserTest {
    Long TEST_PM_USER_ID = 1L;
    String TEST_PM_USER_EMAIL = "test_pm@test.com";
    String TEST_PM_USER_NAME = "test_pm";
    String TEST_PM_USER_PASSWORD = "test1";

    User TEST_PM_USER = User.builder()
            .email(TEST_PM_USER_EMAIL)
            .password(TEST_PM_USER_PASSWORD)
            .username(TEST_PM_USER_NAME)
            .userRole(UserRoleEnum.MANAGER)
            .oAuthType(OAuthTypeEnum.LOCAL)
            .build();

    Long TEST_APM_USER_ID = 2L;
    String TEST_APM_USER_EMAIL = "test_apm@test.com";
    String TEST_APM_USER_NAME = "test_apm";
    String TEST_APM_USER_PASSWORD = "test2";

    User TEST_APM_USER = User.builder()
            .email(TEST_APM_USER_EMAIL)
            .password(TEST_APM_USER_PASSWORD)
            .username(TEST_APM_USER_NAME)
            .userRole(UserRoleEnum.MANAGER)
            .oAuthType(OAuthTypeEnum.LOCAL)
            .build();

    Long TEST_STUDENT_ID = 1L;
    String TEST_STUDENT_USER_EMAIL = "test_student@test.com";
    String TEST_STUDENT_USER_NAME = "test_student";
    String TEST_STUDENT_USER_PASSWORD = "test3";

    User TEST_STUDENT_USER = User.builder()
            .email(TEST_STUDENT_USER_EMAIL)
            .password(TEST_STUDENT_USER_PASSWORD)
            .username(TEST_STUDENT_USER_NAME)
            .userRole(UserRoleEnum.USER)
            .oAuthType(OAuthTypeEnum.LOCAL)
            .build();
}
