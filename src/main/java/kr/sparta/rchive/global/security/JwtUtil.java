package kr.sparta.rchive.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 5 * 60 * 1000L;  // 1시간
    // TODO: 추후에 시간 다시 변경해야함
    private static final long REFRESH_TOKEN_TIME = 10 * 60 * 1000L; // 30일

    @Value("${jwt.secret.key}")
    private String jwtKey;
    private SecretKey secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(jwtKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    public String getOAuthType(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("oAuthType", String.class);
    }

    public Date getIssuedAt(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .getIssuedAt();
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }

    public String createAccessToken(User user) {
        Date date = new Date();
        return BEARER_PREFIX
                + Jwts.builder()
                .claim("email", user.getEmail())
                .claim("role", user.getUserRole().toString())
                .claim("oAuthType", user.getOAuthType().toString())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date date = new Date();
        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("role", user.getUserRole().toString())
                .claim("oAuthType", user.getOAuthType().toString())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(secretKey)
                .compact();
    }


    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }

    public ResponseCookie addRefreshTokenToCookie(String refreshToken)
            throws UnsupportedEncodingException {
//        refreshToken = URLEncoder.encode(refreshToken, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

//        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
//        //cookie.setMaxAge(24*60*60);
//        //cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);

//        String cookieHeader = createSameSiteCookieHeader(cookie, "None");

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                //.maxAge(maxAge)
                .build();

        return cookie;
    }

    public ResponseCookie removeRefreshTokenToCookie()
            throws UnsupportedEncodingException {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, "")
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        return cookie;
    }

//    private String createSameSiteCookieHeader(Cookie cookie, String sameSiteValue) {
//        StringBuilder header = new StringBuilder();
//        header.append(cookie.getName()).append("=").append(cookie.getValue());
//
//        if (cookie.getMaxAge() >= 0) {
//            header.append("; Max-Age=").append(cookie.getMaxAge());
//        }
//        if (cookie.getSecure()) {
//            header.append("; Secure");
//        }
//        if (cookie.isHttpOnly()) {
//            header.append("; HttpOnly");
//        }
//        if (cookie.getPath() != null) {
//            header.append("; Path=").append(cookie.getPath());
//        }
//
//        if (sameSiteValue != null) {
//            header.append("; SameSite=").append(sameSiteValue);
//        }
//
//        return header.toString();
//    }
}
