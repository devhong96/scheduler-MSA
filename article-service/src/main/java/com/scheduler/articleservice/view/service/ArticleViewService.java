package com.scheduler.articleservice.view.service;

import com.scheduler.articleservice.view.repository.ArticleViewCountRepository;
import com.scheduler.articleservice.view.repository.ArticleViewDistributedLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;

    //100개 단위로 백업
    private static final int BACK_UP_BATCH_SIZE = 100;
    private static final Duration TTL = Duration.ofMinutes(10);

    public Long increase(Long articleId, String memberId) {

        // 락 획득
        if(!articleViewDistributedLockRepository.lock(articleId, memberId, TTL)) {
            return articleViewCountRepository.read(articleId);
        }

        Long count = articleViewCountRepository.increase(articleId);

        if(count % BACK_UP_BATCH_SIZE == 0) {
            articleViewCountBackUpProcessor.backup(articleId, count);
        }

        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
