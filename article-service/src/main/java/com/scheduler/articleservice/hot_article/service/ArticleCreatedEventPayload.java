package com.scheduler.articleservice.hot_article.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreatedEventPayload implements EventPayload {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long boardArticleCount;
}
