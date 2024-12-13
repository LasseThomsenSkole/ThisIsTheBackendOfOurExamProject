package org.example.groenfroebackend.service;

import lombok.NoArgsConstructor;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.Post;
import org.example.groenfroebackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Page<Post> searchNews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        // Handle the case where keyword is null or empty
        if (keyword == null || keyword.trim().isEmpty()) {
            return postRepository.findAll(pageable); // Return all posts
        }

        // Otherwise, perform a search
        return postRepository.searchNews(keyword, pageable);
    }


    public List<Post> findNewsByJobTitle(JobTitle jobTitle){
        return postRepository.findNewsByJobTitle(jobTitle);
    }
}
