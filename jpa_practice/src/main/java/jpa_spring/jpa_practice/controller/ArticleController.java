package jpa_spring.jpa_practice.controller;

import jpa_spring.jpa_practice.domain.Article;
import jpa_spring.jpa_practice.domain.Member;
import jpa_spring.jpa_practice.service.ArticleService;
import jpa_spring.jpa_practice.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    //전체 게시글 조회
    @GetMapping("/api/articles")
    public Result articles() {
        List<Article> findArticles = articleService.findArticles();
        List<ArticleDto> collect = findArticles.stream()
                .map(m -> new ArticleDto(m.getTitle(), m.getContent(), m.getCreateTime(), m.getUpdateTime())
                ).collect(Collectors.toList());

        return new Result(collect.size(), collect);

    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class ArticleDto {
        private String title;
        private String content;
        private LocalDateTime create_time;
        private LocalDateTime update_time;
    }


    //게시글 페이징

    @GetMapping("/api/articles/")
    public ResponseEntity<Page<ArticleDto>> getArticles(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        Page<Article> articles = articleService.getArticles(pageNum, pageSize);
        Page<ArticleDto> toMap = articles.map(a -> new ArticleDto(a.getTitle(), a.getContent(), a.getCreateTime(), a.getUpdateTime()));
        return ResponseEntity.ok().body(toMap);
    }





    //게시글 작성
    @PostMapping("/api/articles")
    public CreateArticleResponse saveArticle(@RequestBody CreateArticleRequest request) {
        Member findmember = memberService.findById(request.getMember_id()).get();
        Article article = new Article(request.getTitle(), request.getContent(), findmember);
        Long id = articleService.createArticle(article);
        return new CreateArticleResponse(id, request.getMember_id(), request.getTitle(), request.getContent(), article.getCreateTime(), article.getUpdateTime());


    }

    @Data
    static class CreateArticleRequest {

        private Long member_id;
        private String title;
        private String content;
    }

    @Data
    static class CreateArticleResponse {

        private Long id;
        private Long member_id;
        private String title;
        private String content;
        private LocalDateTime create_time;
        private LocalDateTime update_time;

        public CreateArticleResponse(Long id, Long member_id, String title, String content, LocalDateTime time1, LocalDateTime time2) {
            this.id = id;
            this.member_id = member_id ;
            this.title = title;
            this.content = content;
            this.create_time = time1;
            this.update_time = time2;
        }

    }

    //게시글 업데이트
    @PutMapping("/api/articles/")
    public ResponseEntity<?> updateArticle(@RequestParam(name = "articleId") Long articleId) {
        Article article = articleService.findById(articleId).get();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastUpdateTime = article.getUpdateTime();
        Duration duration = Duration.between(lastUpdateTime, currentTime);
        if (duration.toMinutes() < 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Article with ID " + articleId + " has been updated within the last 10 minutes and cannot be updated again."));

        }
        article.setUpdateTime(LocalDateTime.now());
        Long id = articleService.createArticle(article);
        return ResponseEntity.ok(new CreateArticleResponse(id, article.getMember().getId(), article.getTitle(), article.getContent(), article.getCreateTime(), article.getUpdateTime()));

    }


    @Data
    static class UpdateArticleRequest {

        private Long article_id;
    }
}
