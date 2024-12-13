package org.example.groenfroebackend.controller;

import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.Post;
import org.example.groenfroebackend.security.UserPrincipal;
import org.example.groenfroebackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class PostController {
    @Autowired
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Post> results = postService.searchNews(keyword, page, size);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/newsfeed")
    public ResponseEntity<List<Map<String, Object>>> getRoleNews(@AuthenticationPrincipal UserPrincipal userPrincipal){
        JobTitle jobTitle = userPrincipal.getJobTitle();

        List<Post> jobTitleNews = postService.findNewsByJobTitle(JobTitle.valueOf(jobTitle.name()));

        List<Map<String, Object>> foundNews = jobTitleNews.stream()
                .distinct()
                .map(post -> {
                    Map<String, Object> newsDetails = new HashMap<>();
                    newsDetails.put("title", post.getTitle());
                    newsDetails.put("date", post.getDate());
                    newsDetails.put("description", post.getDescription());
                    return newsDetails;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(foundNews);
    }
}
