package com.scheduler.articleservice.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {

    private final StringRedisTemplate stringRedisTemplate;

    // 사용자 별로 락 획득
    // view::article::{article_id}::user::{user_id}::lock
    private static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

    public boolean lock(Long articleId, String memberId, Duration ttl) {
        String key = generateKey(articleId, memberId);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "", ttl));
    }

    private String generateKey(Long articleId, String memberId) {
        return KEY_FORMAT.formatted(articleId, memberId);
    }
}
