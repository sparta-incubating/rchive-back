package kr.sparta.rchive.global.redis;

import java.util.List;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일
    private static final long SELECT_ROLE_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일

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

    // Refresh Token
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

    // Select Role
    private String keySelectRole(User user) {
        return String.format("select-role-user-%d", user.getId());
    }

    public Long getSelectRole(User user) {
        String key = keySelectRole(user);
        return Long.parseLong(redisUtil.get(key));
    }

    public void setSelectRole(User user, Long trackId) {
        String key = keySelectRole(user);
        redisUtil.set(key, trackId.toString(), SELECT_ROLE_TIME);
    }

}
