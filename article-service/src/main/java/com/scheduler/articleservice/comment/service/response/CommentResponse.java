package com.scheduler.articleservice.comment.service.response;

import com.scheduler.articleservice.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {


    private Long commentId;

    private String content;

    private Long parentCommentId;

    private Long articleId;

    private String memberId;

    private Boolean deleted;

    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.commentId = comment.getCommentId();
        commentResponse.content = comment.getContent();
        commentResponse.parentCommentId = comment.getParentCommentId();
        commentResponse.articleId = comment.getArticleId();
        commentResponse.memberId = comment.getMemberId();
        commentResponse.deleted = comment.getDeleted();
        commentResponse.createdAt = comment.getCreatedAt();
        return commentResponse;
    }
}
