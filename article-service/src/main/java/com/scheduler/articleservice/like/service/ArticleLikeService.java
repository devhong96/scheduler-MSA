package com.scheduler.articleservice.like.service;

import com.scheduler.articleservice.infra.common.Snowflake;
import com.scheduler.articleservice.like.entity.ArticleLike;
import com.scheduler.articleservice.like.entity.ArticleLikeCount;
import com.scheduler.articleservice.like.repository.ArticleLikeCountRepository;
import com.scheduler.articleservice.like.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponse read(Long articleId, String memberId) {
        return articleLikeRepository.findByArticleIdAndMemberId(articleId, memberId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    @Transactional
    public void like(Long articleId, String userId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        userId
                )
        );
    }

    public void likePessimisticLock1(Long articleId, String memberId) {
        articleLikeRepository.save(ArticleLike.create(snowflake.nextId(),
                articleId,
                memberId)
        );

        int result = articleLikeCountRepository.increase(articleId);

        if (result == 0) {
            articleLikeCountRepository.save(
                    ArticleLikeCount.init(articleId, 1L)
            );
        }
    }

    @Transactional
    public void likePessimisticLock2(Long articleId, String memberId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        memberId
                )
        );
        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findArticleLockByArticleId(articleId)
                .orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount);
    }

    @Transactional
    public void unlikePessimisticLock2(Long articleId, String memberId) {
        articleLikeRepository.findByArticleIdAndMemberId(articleId, memberId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    ArticleLikeCount articleLikeCount = articleLikeCountRepository.findArticleLockByArticleId(articleId).orElseThrow();
                    articleLikeCount.decrease();
                });
    }

    @Transactional
    public void likeOptimisticLock(Long articleId, String memberId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        memberId
                )
        );

        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
                .orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount);
    }

    @Transactional
    public void unlike(Long articleId, String userId) {
        articleLikeRepository.findByArticleIdAndMemberId(articleId, userId)
                .ifPresent(articleLikeRepository::delete);
    }
}
