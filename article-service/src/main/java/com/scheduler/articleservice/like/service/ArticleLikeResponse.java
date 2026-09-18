package com.scheduler.articleservice.like.service;

import com.scheduler.articleservice.like.entity.ArticleLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleLikeResponse {

    private Long articleLikeId;

    private Long articleId;

    private String memberId;

    private LocalDateTime createdAt;

    public static ArticleLikeResponse from(ArticleLike articleLike) {
        ArticleLikeResponse response = new ArticleLikeResponse();
        response.articleLikeId = articleLike.getArticleLikeId();
        response.articleId = articleLike.getArticleId();
        response.memberId = articleLike.getMemberId();
        response.createdAt = articleLike.getCreatedAt();
        return response;
    }
}
