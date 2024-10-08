package kr.sparta.rchive.global.config;

import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.global.redis.RedisService;
import kr.sparta.rchive.global.security.JwtAuthorizationFilter;
import kr.sparta.rchive.global.security.JwtUtil;
import kr.sparta.rchive.global.security.LoginFilter;
import kr.sparta.rchive.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthFilter() throws Exception {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(jwtUtil, redisService);
        loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return loginFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((auth) -> auth.disable());

        http.formLogin((auth) -> auth.disable());

        http.httpBasic((auth) -> auth.disable());

        http.sessionManagement((session)
            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/apis/v1/users/reissue").permitAll()
                        .requestMatchers("/apis/v1/role/**").hasAnyRole(UserRoleEnum.USER.toString(), UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers("/apis/v1/profile/**").hasAnyRole(UserRoleEnum.USER.toString(), UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers("/apis/v1/backoffice/**").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers("/apis/v1/s3/**").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/apis/v1/posts").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH, "/apis/v1/posts/{postId}").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/apis/v1/posts/{postId}").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers("/apis/v1/posts/tags").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH, "/apis/v1/posts/open").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH, "/apis/v1/posts/close").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/apis/v1/posts/tutors").hasAnyRole(UserRoleEnum.MANAGER.toString(), UserRoleEnum.ADMIN.toString())
                        .requestMatchers("/apis/v1/users/**").permitAll()
                        .requestMatchers("/apis/v1/posts/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter(), LoginFilter.class);
        http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
