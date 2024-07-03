package kr.sparta.rchive.global.redis;

import java.util.List;
import kr.sparta.rchive.domain.user.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

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

}
