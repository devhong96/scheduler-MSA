package com.scheduler.articleservice.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "article_like")
@Getter
@Entity

@NoArgsConstructor(access = PROTECTED)
public class ArticleLike {

    @Id
    private Long articleLikeId;

    private Long articleId; // shard key

    private String memberId;

    private LocalDateTime createdAt;

    public static ArticleLike create(Long articleLikeId, Long articleId, String memberId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.articleLikeId = articleLikeId;
        articleLike.articleId = articleId;
        articleLike.memberId = memberId;
        articleLike.createdAt = LocalDateTime.now();
        return articleLike;
    }
}
