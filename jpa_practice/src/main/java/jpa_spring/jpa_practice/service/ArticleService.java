package jpa_spring.jpa_practice.service;

import jpa_spring.jpa_practice.domain.Article;
import jpa_spring.jpa_practice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    //게시글 아이디로 찾기
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    //게시글 작성, 업데이트
    public Long createArticle(Article article) {
        articleRepository.save((article));
        return article.getId();
    }


    //게시글 전체조회
    public List<Article> findArticles() {
        return articleRepository.findAll();
    }


    public Page<Article> getArticles(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("updateTime").descending());
        return articleRepository.findAll(pageable);
    }
}
