package kr.sparta.rchive.global.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                res.setStatus(401);
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            try {
                setAuthentication(info);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }


    public void setAuthentication(Claims claims) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(claims);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(Claims claims) {
        HashMap<String,String> map = (HashMap<String, String>) claims.get("auth");

        OAuthTypeEnum oAuthTypeEnum = OAuthTypeEnum.valueOf(map.get("oAuthTypeEnum"));

        UserDetails userDetails = userDetailsService.loadUserByEmailAndPlatform(map.get("email"), oAuthTypeEnum);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
