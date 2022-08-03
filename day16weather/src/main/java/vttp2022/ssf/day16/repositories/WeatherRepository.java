package vttp2022.ssf.day16.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;


@Repository
public class WeatherRepository {
    
    @Value("${weather.cache.duration}")
    private Long cacheTime;

    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String, String> redisTemplate;

    public void save(String city, String payload) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(city.toLowerCase(), payload, Duration.ofMinutes(cacheTime));
        // time expiry will remove it from redis
    }

    public Optional<String> get(String city) {
        // give the city and retrive the payload from redis
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(city.toLowerCase());
        if (null == value)
            return Optional.empty(); // empty box
        return Optional.of(value); // box with data
        
        // String redisPayload = valueOp.get(city.toLowerCase());
        // return redisPayload;

    }
}
