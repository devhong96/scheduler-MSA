package com.scheduler.articleservice.article.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.articleservice.article.service.ArticleService;
import com.scheduler.articleservice.article.service.request.ArticleCreateRequest;
import com.scheduler.articleservice.article.service.response.ArticleResponse;
import com.scheduler.articleservice.testSet.IntegrationTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;


@IntegrationTest
class ArticleControllerTest {

    @Autowired
    private WebClient webClient;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void createTest() {

        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("hi", "my content", "asdf", 1L);

        when(articleService.create(articleCreateRequest))
                .thenReturn(new ArticleResponse());

        Mono<ArticleResponse> response = create(articleCreateRequest);

        System.out.println("response = " + response.block());
    }

    Mono<ArticleResponse> create(ArticleCreateRequest request) {
        return webClient.post()
                .uri("/v1/articles")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(121530268440289280L).block();
        System.out.println("response = " + response);
    }

    Mono<ArticleResponse> read(Long articleId) {
        return webClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .bodyToMono(ArticleResponse.class);
    }
//    @Test
//    void updateTest() {
//        update(121530268440289280L);
//        ArticleResponse response = read(121530268440289280L);
//        System.out.println("response = " + response);
//    }
//
//    void update(Long articleId) {
//        webClient.put()
//                .uri("/v1/articles/{articleId}", articleId)
//                .body(new ArticleUpdateRequest("hi 2", "my content 22"))
//                .retrieve()
//                .bodyToMono();

//    }


    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

}