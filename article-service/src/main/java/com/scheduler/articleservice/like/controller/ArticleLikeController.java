package com.scheduler.articleservice.like.controller;

import com.scheduler.articleservice.like.service.ArticleLikeResponse;
import com.scheduler.articleservice.like.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @GetMapping("/v1/articles-likes/articles/{articleId}/members/{memberId}")
    public ArticleLikeResponse read(
            @PathVariable("articleId") Long articleId,
            @PathVariable("memberId") String memberId
    ) {
        return articleLikeService.read(articleId, memberId);
    }

    @PostMapping("/v1/articles-likes/articles/{articleId}/members/{memberId}")
    public void like(
            @PathVariable("articleId") Long articleId,
            @PathVariable("memberId") String memberId
    ) {
        articleLikeService.like(articleId, memberId);
    }

    @DeleteMapping("/v1/articles-likes/articles/{articleId}/members/{memberId}")
    public void unlike(
            @PathVariable("articleId") Long articleId,
            @PathVariable("memberId") String memberId
    ) {
        articleLikeService.unlike(articleId, memberId);
    }
}
