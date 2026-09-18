package com.scheduler.articleservice.article.service;

import com.scheduler.articleservice.article.entity.Article;
import com.scheduler.articleservice.article.entity.BoardArticleCount;
import com.scheduler.articleservice.article.repository.ArticleRepository;
import com.scheduler.articleservice.article.repository.BoardArticleCountRepository;
import com.scheduler.articleservice.article.service.request.ArticleCreateRequest;
import com.scheduler.articleservice.article.service.request.ArticleUpdateRequest;
import com.scheduler.articleservice.article.service.response.ArticlePageResponse;
import com.scheduler.articleservice.article.service.response.ArticleResponse;
import com.scheduler.articleservice.infra.common.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(
                        snowflake.nextId(),
                        request.getTitle(), request.getContent(), request.getBoardId(),
                        request.getMemberId())
        );

        int result = boardArticleCountRepository.increase(request.getBoardId());

        if (result == 0) {
            boardArticleCountRepository.save(
                    BoardArticleCount.init(request.getBoardId(), 1L)
            );
        }

        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticlePageResponse.of(
                //offset 계산공식
                articleRepository.findAll(boardId, (page - 1) * pageSize, pageSize).stream()
                        .map(ArticleResponse::from)
                        .toList(),
                articleRepository.count(
                        boardId,
                        PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
                )
        );
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
    }


    public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
        List<Article> articles = lastArticleId == null ?
                articleRepository.findAllInfiniteScroll(boardId, pageSize) : // 첫페이지
                articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
        return articles.stream().map(ArticleResponse::from).toList();
    }

    public Long count(Long boardId) {
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount)
                .orElse(0L);
    }
}
