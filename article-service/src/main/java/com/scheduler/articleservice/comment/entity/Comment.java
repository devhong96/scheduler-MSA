package com.scheduler.articleservice.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment {

    @Id
    private Long commentId;

    private String content;

    private Long parentCommentId;

    private Long articleId; //shard key

    private String memberId;

    private Boolean deleted;

    private LocalDateTime createdAt;

    public static Comment create(Long commentId, String content, Long parentCommentId,
                                 Long articleId, String memberId) {
        Comment comment = new Comment();
        comment.commentId = commentId;
        comment.content = content;
        comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
        comment.articleId = articleId;
        comment.memberId = memberId;
        comment.deleted = false;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    // 로직 이해할 것.
    public boolean isRoot() {
        // 최초 댓글의 commentId가 모계면 root 댓글이 된다.
        return parentCommentId.longValue() == commentId.longValue();
    }

    public void delete() {
        this.deleted = true;
    }
}
