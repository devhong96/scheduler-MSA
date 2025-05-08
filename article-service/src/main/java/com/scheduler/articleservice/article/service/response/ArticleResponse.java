package com.scheduler.articleservice.article.service.response;

import com.scheduler.articleservice.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private String memberId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ArticleResponse from(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.articleId = article.getArticleId();
        response.title = article.getTitle();
        response.content = article.getContent();
        response.boardId = article.getBoardId();
        response.memberId = article.getMemberId();
        response.createdAt = article.getCreatedAt();
        response.modifiedAt = article.getLastModifiedAt();
        return response;
    }
}
