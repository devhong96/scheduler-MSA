package com.scheduler.articleservice.view.controller;

import com.scheduler.articleservice.view.service.ArticleViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleVIewController {

    private final ArticleViewService articleViewService;

    @PostMapping("/v1/article-views/articles/{articleId}/members/{memberId}")
    public Long increase(
            @PathVariable("articleId") Long articleId,
            @PathVariable("memberId") String memberId
    ) {
        return articleViewService.increase(articleId, memberId);
    }

    @GetMapping("/v1/article-views/articles/{articleId}/count")
    public Long count(@PathVariable("articleId") Long articleId) {
        return articleViewService.count(articleId);
    }

}
