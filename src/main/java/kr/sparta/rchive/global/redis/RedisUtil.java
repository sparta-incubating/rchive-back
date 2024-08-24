package kr.sparta.rchive.global.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j(topic = "Redis Util")
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Long> longRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    public void set(String key, String value, long ms) {
        log.info("[set] key : {}, value : {}, time : {} ms", key, value, ms);
        stringRedisTemplate.opsForValue().set(key, value, ms, TimeUnit.MILLISECONDS);
    }

    public String get(String key) {
        log.info("[get] key : {}", key);
        String value = stringRedisTemplate.opsForValue().get(key);
        log.info("[get] value : {}", value);
        return value;
    }

    public void setListTypeLong(String key, List<Long> value) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();
        listOps.rightPushAll(key, value);
    }

    public List<Long> getListTypeLong(String key) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();
        List<Object> objects = listOps.range(key, 0, -1);
        assert objects != null;
        return objects.stream()
                .map(o -> (Long) o)
                .collect(Collectors.toList());
    }

    public void setStringList(String key, String value) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();
        listOps.leftPush(key, value);
    }

    public void checkSearchLength(String key) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();

        Long size = listOps.size(key);

        if(size != null && size > 5) {
            listOps.trim(key, 0, 4);
        }
    }

    public boolean delete(String key) {
        log.info("[delete] key {}", key);
        boolean isDelete = Boolean.TRUE.equals(stringRedisTemplate.delete(key));
        log.info("[delete] isDelete : {}", isDelete);
        return isDelete;
    }

    public boolean hasKey(String key) {
        log.info("[hasKey] key : {}", key);
        boolean hasKey = Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
        log.info("[hasKey] hasKey : {}", hasKey);
        return hasKey;
    }

    public void removeKeywordSearch(String key, String keyword) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();

        listOps.remove(key, 1, keyword);
    }

    public List<String> getSearchKeywordList(String key) {
        ListOperations<String, Object> listOps = objectRedisTemplate.opsForList();

        List<Object> keywordList = listOps.range(key, 0, -1);

        if(keywordList == null) {
            return null;
        }

        return keywordList.stream()
            .map(Object::toString)
            .collect(Collectors.toList());
    }
}
