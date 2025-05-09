package com.scheduler.articleservice.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountRepository {

    private final StringRedisTemplate stringRedisTemplate;

    // view::article::{article_id}::view_count
    private static final String KEY_FORMAT = "view::article::%s::view_count";


    public Long read(Long articleId) {
        String result = stringRedisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long increase(Long articleId) {
        return stringRedisTemplate.opsForValue().increment(generateKey(articleId));
    }

    public void decrease(Long articleId) {
        stringRedisTemplate.opsForValue().decrement(generateKey(articleId));
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

}
