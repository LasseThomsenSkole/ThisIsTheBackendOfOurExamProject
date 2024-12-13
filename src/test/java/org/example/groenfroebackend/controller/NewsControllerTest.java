package org.example.groenfroebackend.controller;

import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.Post;
import org.example.groenfroebackend.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class NewsControllerTest { //todo fix det her så det passer med auth test

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Test
    public void testSearchNews() throws Exception {
        // Prepare test data
        Post post1 = new Post(1, "Title 1", "Content 1", LocalDate.now(), JobTitle.OPTICIAN);
        Post post2 = new Post(2, "Title 2", "Content 2", LocalDate.now(), JobTitle.OPTICIAN);
        List<Post> postList = Arrays.asList(post1, post2);
        Page<Post> postPage = new PageImpl<>(postList, PageRequest.of(0, 10), 2);

        // Mock the service method
        when(postService.searchNews(anyString(), anyInt(), anyInt())).thenReturn(postPage);

        // Perform the request and verify the response
        mockMvc.perform(get("/search")
                        .param("keyword", "test")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Title 1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Title 2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    public void testSearchNewsWithoutKeyword() throws Exception {
        // Prepare test data
        Page<Post> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        // Mock the service method
        when(postService.searchNews(isNull(), anyInt(), anyInt())).thenReturn(emptyPage);

        // Perform the request and verify the response
        mockMvc.perform(get("/search")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }
}