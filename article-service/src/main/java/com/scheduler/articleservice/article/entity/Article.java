package com.scheduler.articleservice.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "article")
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Article extends BaseEntity{

    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long boardId; // shard key
    private String memberId;

    public static Article create(Long articleId, String title, String content, Long boardId, String memberId) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.memberId = memberId;
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
