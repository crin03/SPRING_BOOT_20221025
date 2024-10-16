package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

@Controller
public class BlogController {
    @Autowired
    BlogService blogService;
    
    @GetMapping("/article_list") // 게시판 링크 지정
    public String article_list(Model model) {
    List<Article> list = blogService.findAll(); // 게시판 리스트
    model.addAttribute("articles", list); // 모델에 추가
    return "article_list"; // HTML 연결
    }

    // 5주차 연습문제
    @PostMapping("/api/articles")
    public String addArticle(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/article_list";
    }
}
