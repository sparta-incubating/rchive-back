package kr.sparta.rchive.security;

import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.test.UserTest;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomPMSecurityContextFactory.class)
public @interface WithMockCustomPM {
    String loginId() default UserTest.TEST_PM_USER_EMAIL;
    String password() default UserTest.TEST_PM_USER_PASSWORD;
    UserRoleEnum userRole() default UserRoleEnum.MANAGER;
}
