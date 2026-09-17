package com.scheduler.articleservice.hot_article.service;

import com.scheduler.articleservice.hot_article.repository.ArticleCreatedTimeRepository;
import com.scheduler.articleservice.hot_article.repository.HotArticleListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {

    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

    public void update(Event<EventPayload> event, EventHandler<EventPayload> handler) {
        Long articleId = handler.findArticleId(event);
        LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

        if (!isArticleCreateToday(createdTime)) {
            return;
        }

        long score = hotArticleScoreCalculator.calculate(articleId);
        hotArticleListRepository.add(
                articleId,
                createdTime,
                score,
                HOT_ARTICLE_COUNT,
                HOT_ARTICLE_TTL
        );
    }

    private boolean isArticleCreateToday(LocalDateTime createdTime) {
        return createdTime != null && createdTime.toLocalDate().isEqual(LocalDate.now());
    }
}
