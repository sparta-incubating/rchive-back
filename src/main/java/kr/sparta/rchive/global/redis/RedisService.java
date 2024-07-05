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

    private final RedisUtil redisUtil;

    public String redisKeyPostIdListByTagNameAndTrack(String tagName, Track track) {
        return String.format("tag-%s-%s-%d", tagName, track.getTrackName(), track.getPeriod());
    }

    public boolean redisKeyExist(String redisKey) {
        return redisUtil.hasKey(redisKey);
    }

    public List<Long> getListInRedisTypeLong(String redisKey) {
        return redisUtil.getListTypeLong(redisKey);
    }

    public void setListInRedisTypeLong(String redisKey, List<Long> postIdList) {
        redisUtil.setListTypeLong(redisKey, postIdList);
    }

    /** Refresh Token **/
    private String keyRefreshToken(User user){
        return String.format("refresh-user-%d",user.getId());
    }

    public String getRefreshToken(User user){
        String key = keyRefreshToken(user);
        return redisUtil.get(key);
    }

    public void setRefreshToken(User user, String refresh){
        String key = keyRefreshToken(user);
        redisUtil.set(key,refresh,REFRESH_TOKEN_TIME);
    }

}
