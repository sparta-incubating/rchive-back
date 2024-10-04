package kr.sparta.rchive.global.redis;

import java.util.List;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    //    private static final long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일
    private static final long REFRESH_TOKEN_TIME = 5 * 60 * 1000L; // 5분
    private static final long SELECT_ROLE_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일
    private static final long AUTH_PHONE_TIME = 10 * 60 * 1000L; // 10분

    private final RedisUtil redisUtil;

    private String keyPostIdListByTagNameAndTrack(String tagName, Track track) {
        return String.format("tag-%s-%s-%d", tagName, track.getTrackName(), track.getPeriod());
    }

    public boolean keyExist(String redisKey) {
        return redisUtil.hasKey(redisKey);
    }

    public List<Long> getPostIdListInRedis(String tagName, Track track) {
        String redisKey = keyPostIdListByTagNameAndTrack(tagName, track);
        return redisUtil.getListTypeLong(redisKey);
    }

    public void setPostIdListInRedis(String tagName, Track track, List<Long> postIdList) {
        String redisKey = keyPostIdListByTagNameAndTrack(tagName, track);
        redisUtil.setListTypeLong(redisKey, postIdList);
    }

    /* Refresh Token */
    private String keyRefreshToken(User user) {
        return String.format("refresh-user-%d", user.getId());
    }

    public String getRefreshToken(User user) {
        String key = keyRefreshToken(user);
        return redisUtil.get(key);
    }

    public void setRefreshToken(User user, String refresh) {
        String key = keyRefreshToken(user);
        redisUtil.set(key, refresh, REFRESH_TOKEN_TIME);
    }

    public void deleteRefreshToken(User user) {
        String key = keyRefreshToken(user);
        redisUtil.delete(key);
    }

    /* Select Role */
    private String keySelectRole(User user) {
        return String.format("select-role-user-%d", user.getId());
    }

    public Long getLastSelectRole(User user) {
        String key = keySelectRole(user);
        String stringValue = redisUtil.get(key);
        if (stringValue == null) {
            return null;
        }

        return Long.parseLong(redisUtil.get(key));
    }

    public void setLastSelectRole(User user, Long trackId) {
        String key = keySelectRole(user);
        redisUtil.set(key, trackId.toString(), SELECT_ROLE_TIME);
    }

    public void deleteLastSelectRole(User user) {
        String key = keySelectRole(user);
        redisUtil.delete(key);
    }

    /* RecentSearchKeyword */
    public void saveRecentSearchKeyword(Long userId, Long trackId, String keyword) {
        String key = keySearchKeyword(userId, trackId);

        redisUtil.removeKeywordSearch(key, keyword);
        redisUtil.setStringList(key, keyword);
        redisUtil.checkSearchLength(key);
    }

    private String keySearchKeyword(Long userId, Long trackId) {
        return String.format("search-keyword-%d-%d", userId, trackId);
    }

    public List<String> getRecentSearchKeyword(Long userId, Long trackId) {
        String key = keySearchKeyword(userId, trackId);

        return redisUtil.getSearchKeywordList(key);
    }

    public void deleteSearchKeyword(Long userId, Long trackId, String keyword) {
        String key = keySearchKeyword(userId, trackId);

        redisUtil.removeKeywordSearch(key, keyword);
    }

    /* Auth Phone */
    private String keyAuthPhone(String username, String phone) {
        return String.format("auth-phone-%s-%s", username, phone);
    }

    public String getAuthPhone(String username, String phone) {
        String key = keyAuthPhone(username, phone);
        return redisUtil.get(key);
    }

    public void setAuthPhone(String username, String phone, String authCode) {
        String key = keyAuthPhone(username, phone);
        redisUtil.set(key, authCode, AUTH_PHONE_TIME);
    }

}
