package com.scheduler.articleservice.article.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCreateRequest {
    private String title;
    private String content;
    private String memberId;
    private Long boardId;


}
