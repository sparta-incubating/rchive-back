package kr.sparta.rchive.global.redis;

import java.util.List;
import kr.sparta.rchive.domain.user.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

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

}
