package kr.sparta.rchive.global.config;

import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.global.security.JwtAuthorizationFilter;
import kr.sparta.rchive.global.security.JwtUtil;
import kr.sparta.rchive.global.security.LoginFilter;
import kr.sparta.rchive.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf((auth)->auth.disable());

        http.formLogin((auth)->auth.disable());

        http.httpBasic((auth)->auth.disable());

        http.sessionManagement((session)
                ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests)->
                authorizeHttpRequests
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
//                        .requestMatchers("/api/v1/admin").hasRole(UserRoleEnum.ADMIN.toString())
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthorizationFilter(jwtUtil,userDetailsService), LoginFilter.class);

        http.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
