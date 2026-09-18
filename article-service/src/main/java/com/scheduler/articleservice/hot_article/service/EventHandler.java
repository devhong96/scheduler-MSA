package com.scheduler.articleservice.hot_article.service;

public interface EventHandler<T extends EventPayload> {
    void handle(T payload);
    boolean support(Event<T> event);
    Long findArticleId(Event<T> event);
}
