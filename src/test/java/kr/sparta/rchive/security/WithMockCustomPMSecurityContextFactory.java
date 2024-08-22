package kr.sparta.rchive.security;

import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.global.security.UserDetailsImpl;
import kr.sparta.rchive.test.UserTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomPMSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomPM>, UserTest {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomPM pmUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User mockUser = User.builder()
                .email(pmUser.loginId())
                .password(pmUser.password())
                .userRole(pmUser.userRole())
                .oAuthType(OAuthTypeEnum.LOCAL)
                .build();
        UserDetails userDetails = new UserDetailsImpl(mockUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
